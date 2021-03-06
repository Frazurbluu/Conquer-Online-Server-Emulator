/**
 * **********************************************************************
 * Copyright 2012 Charles Benger
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ***************************************************************************
 */



package conquerboxgame.net.handlers;

//~--- non-JDK imports --------------------------------------------------------

import conquerboxgame.MyLogger;

import conquerboxgame.core.Client;
import conquerboxgame.core.IHandler;
import conquerboxgame.core.Kernel;

import conquerboxgame.net.ServerDataEvent;

import conquerboxgame.packets.*;
import conquerboxgame.packets.npc.NpcCommand;

import conquerboxgame.structures.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

//~--- JDK imports ------------------------------------------------------------

import java.nio.ByteOrder;

import java.util.logging.Level;

/**
 *
 * @author chuck
 */
public class PacketHandler implements IHandler
{
    /**
     * Handles the input packet
     * @param event the event to handle
     */
    @Override
    public void handle(ServerDataEvent event)
    {
        long   id;
        long   timer;
        String response;
        Client client = event.getClient();

        // Create a new channel buffer
        ChannelBuffer buffer = ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN, event.getPacket().length);

        buffer.writeBytes(event.getPacket());

        // Discard the length
        buffer.readUnsignedShort();

        // Grab the packet type
        int type = buffer.readUnsignedShort();

        // Switch the packet type
        switch (type)
        {
        /*
         *   0       ushort  28
         *   2       ushort  1052
         *   4       uint    Account_ID
         *   8       uint    Login_Token
         *   12      byte[16]        Message_Bytes
         *
         *   Gets the client token and id and uses them to generate the new decryption key pair.
         *   a response of NEW_ROLE is sent if they don't have an account.
         *   ANSWER_OK is sent if they have an account. Otherwise other response is sent
         */
        case PacketTypes.AUTH_MESSAGE :
            response = "ANSWER_OK";
           
            // Setup the new decryption keys
            client.setId(buffer.readUnsignedInt());
            client.setToken(buffer.readUnsignedInt());
            client.getCrypt().SetKeys(client.getToken(), client.getId());

            // Check if they have a game account already
            if (!Kernel.DATABASE.hasGameAccount(client.getId()))
                response = "NEW_ROLE";
            else
            {
                if (!Kernel.DATABASE.loadCharacterData(client))
                {
                    client.send(AuthMessage.build(0, "SYSTEM", "ALLUSERS", "Failed to load character",
                                                  ChatTypes.LOGININFORMATION));

                    break;
                }
            }

            client.send(AuthMessage.build(0, "SYSTEM", "ALLUSERS", response, ChatTypes.LOGININFORMATION));
            if (response.equals("ANSWER_OK"))
            {
                client.send(CharacterInfo.build(client));
                
            }
            
            break;

        /*
         *   0       ushort  60
         *   2       ushort  1001
         *   4       string[16]      Account_Name
         *   20      string[16]      Character_Name
         *   36      string[16]      Account_Password
         *   52      ushort  Character_Model
         *   54      ushort  Character_Class
         *   56      uint    Account_ID
         *
         *   This packet is sent from the client when it want's to create
         *   a new account.
         */
        case PacketTypes.CHARACTER_CREATION :
            response = "ANSWER_OK";
            buffer.readerIndex(20);

            byte[] name = new byte[16];

            buffer.readBytes(name, 0, name.length);
            client.setName(new String(name).trim());
            buffer.readerIndex(52);
            client.setModel(buffer.readUnsignedShort());
            client.setPlayerClass(buffer.readUnsignedShort());
            if (!Kernel.DATABASE.createCharacter(client))
                response = "Account creation failed";
            client.send(AuthMessage.build(0, "SYSTEM", "ALLUSERS", response, ChatTypes.LOGININFORMATION));
            client.getChannel().close();

            break;

        /**
         *   0       ushort  28
         *   2       ushort  1010
         *   4       uint    Timer
         *   8       uint    Entity_ID
         *   12      ushort  Value_A
         *   14      ushort  Value_B
         *   16      ushort  Value_C
         *   20      ushort  Value_D
         *   22      ushort  Value_E
         *   24      uint   Data_Type
         * General data that is requested from the client.
         */
        case PacketTypes.GENERAL_DATA :
            GeneralHandler.handleGeneralPacket(client, buffer);

            break;

        /**
         * 0     ushort  12
         *   2   ushort  1005
         *   4   uint    Entity_ID
         *   8   byte    Direction
         *   9   bool    Speed
         * This packet is sent when the client is walking
         */
        case PacketTypes.ENTITY_MOVE :
            id = buffer.readUnsignedInt();

            if (id == client.getCharacterId())
            {
                client.setDirection((byte) ((buffer.readByte() & 0xFF) % 8));
                Client.adjustPostionBasedOnDirection(client);
               
            }

            break;

        case PacketTypes.CHAT :
            ChatHandler.handleChat(client, buffer);
            break;

        case PacketTypes.NPC_INITIAL:
            id = buffer.readUnsignedInt();
            NpcCommand.handleDialog(client, id, (byte)0);
            break;
            
        case PacketTypes.NPC_COMMAND:
            timer = buffer.readUnsignedInt();
            buffer.readerIndex(10); //Link id is at index 10
            short linkID = buffer.readUnsignedByte();
            
            //If the no link back was set for this packet don't handle it
            if(linkID == NpcCommand.NO_LINK_BACK)
                break;
            
            //Pass in 0 as npc id because the npc id was recorded from last interaction
            NpcCommand.handleDialog(client, 0, (byte)(linkID & 0xFF));
            break;
            
        case PacketTypes.ITEM_USAGE:
            ItemUsageHandler.handleItemUsage(client, buffer);
            break;
            
        default :
            MyLogger.appendLog(Level.INFO, "Unkown packet type => " + type);
            break;
        }
    }
}

