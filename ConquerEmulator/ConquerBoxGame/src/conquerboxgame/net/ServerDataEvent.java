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



package conquerboxgame.net;

//~--- non-JDK imports --------------------------------------------------------

import conquerboxgame.core.Client;

/**
 * SeverDataEvent is used to encapsulate a a client and a packet to be processed by a handler
 * @author chuck
 */
public class ServerDataEvent
{
    private Client client;    // The client itself
    private byte[] packet;    // The packet for this client

    public ServerDataEvent(byte[] packet, Client client)
    {
        this.packet = packet;
        this.client = client;
    }

    public Client getClient()
    {
        return client;
    }

    public byte[] getPacket()
    {
        return packet;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
