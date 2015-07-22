/**
 * Starving - Bukkit API server mod with Zombies.
 * Copyright (c) 2015, Matej Kormuth <http://www.github.com/dobrakmato>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package eu.matejkormuth.starving.remoteconnections.netty.packets;

import io.netty.buffer.ByteBuf;
import eu.matejkormuth.starving.remoteconnections.netty.Packet;

public class HandshakePacket extends Packet {

    public String nickname;
    public String accesskey;

    @Override
    public void writeTo(ByteBuf toBuffer) {
        toBuffer.writeByte(nickname.length());
        toBuffer.writeBytes(nickname.getBytes(PROTOCOL_ENCODING));
        toBuffer.writeByte(accesskey.length());
        toBuffer.writeBytes(accesskey.getBytes(PROTOCOL_ENCODING));
    }

    @Override
    public void readFrom(ByteBuf fromBuffer) {
        byte[] nickname = new byte[fromBuffer.readByte()];
        fromBuffer.readBytes(nickname);
        byte[] accesskey = new byte[fromBuffer.readByte()];
        fromBuffer.readBytes(accesskey);
        this.nickname = new String(nickname, PROTOCOL_ENCODING);
        this.accesskey = new String(accesskey, PROTOCOL_ENCODING);
    }

}
