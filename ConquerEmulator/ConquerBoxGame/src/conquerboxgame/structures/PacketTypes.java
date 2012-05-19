package conquerboxgame.structures;

/**
 *
 * Copyright 2012 Charles Benger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/**
 * This class contains all the packet types used by the game server
 * @author chuck
 */
public class PacketTypes
{
    public static final short AUTH_MESSAGE       = 1052;
    public static final short CHARACTER_CREATION = 1001;
    public static final short CHARACTER_DATA     = 1006;
    public static final short CHAT               = 1004;
    public static final short ENTITY_MOVE        = 1005;
    public static final short GENERAL_DATA       = 1010;
    public static final short NPC_SPAWN          = 2030;
    public static final short NPC_INITIAL        = 2031;
}


//~ Formatted by Jindent --- http://www.jindent.com
