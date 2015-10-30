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
package eu.matejkormuth.starving.npc.util;

import eu.matejkormuth.starving.main.NMSHooks;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.server.v1_8_R3.*;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.net.SocketAddress;


@NMSHooks(version = "v1_8_R3")
public class NullNetworkManager extends NetworkManager {

    private static final Field CHANNEL;
    private static final Field ADDRESS;
    static {
        try {

            // i in 1_8_R1
            // k in 1_8_R2
            CHANNEL = NetworkManager.class
                    .getDeclaredField("k");
            if (!CHANNEL.isAccessible()) {
                CHANNEL.setAccessible(true);
            }

            // j in 1_8_R1
            // l in 1_8_R2
            ADDRESS = NetworkManager.class
                    .getDeclaredField("l");
            if (!ADDRESS.isAccessible()) {
                ADDRESS.setAccessible(true);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Can't initialize Fields in NullNetworkManager!");
        }
    }

    public NullNetworkManager(EnumProtocolDirection enumprotocoldirection) {
        super(enumprotocoldirection);

        try {
            // Reflection.
            CHANNEL.set(this, new NullChannel(null));
            ADDRESS.set(this, new SocketAddress() {
                private static final long serialVersionUID = 1L;
            });
        } catch (Exception e) {
            throw new RuntimeException(
                    "Can't set field values in NullNetworkManager!");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext channelhandlercontext)
            throws Exception {
    }

    @Override
    public void a(EnumProtocol enumprotocol) {
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelhandlercontext) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelhandlercontext,
            Throwable throwable) {
    }

    @Override
    protected void a(ChannelHandlerContext channelhandlercontext, @SuppressWarnings("rawtypes") Packet packet) {
    }

    @Override
    public void a(PacketListener packetlistener) {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void handle(Packet packet) {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void a(Packet packet, GenericFutureListener genericfuturelistener,
            GenericFutureListener... agenericfuturelistener) {
    }

    @Override
    public void a() {
    }

    @Override
    public SocketAddress getSocketAddress() {
        return super.getSocketAddress();
    }

    @Override
    public void close(IChatBaseComponent ichatbasecomponent) {
    }

    @Override
    public boolean c() {
        return true;
    }

    @Override
    public void a(SecretKey secretkey) {
    }

    @Override
    public boolean g() {
        return true;
    }

    @Override
    public boolean h() {
        return true;
    }

    @Override
    public PacketListener getPacketListener() {
        return super.getPacketListener();
    }

    @Override
    public IChatBaseComponent j() {
        return new ChatMessage("");
    }

    @Override
    public void k() {
    }

    @Override
    public void a(int i) {
    }

    @Override
    public void l() {
    }

    @Override
    public SocketAddress getRawAddress() {
        return super.getRawAddress();
    }

}
