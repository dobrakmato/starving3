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
package eu.matejkormuth.starving.remoteconnections;

import eu.matejkormuth.starving.main.Data;
import eu.matejkormuth.starving.remoteconnections.netty.ChannelInitializer;
import eu.matejkormuth.starving.remoteconnections.netty.DefaultProtocol;
import eu.matejkormuth.starving.remoteconnections.netty.Packet;
import eu.matejkormuth.starving.remoteconnections.netty.handlers.PacketChannelInboundHandler;
import eu.matejkormuth.starving.remoteconnections.netty.packets.DisconnectPacket;
import eu.matejkormuth.starving.remoteconnections.netty.packets.HandshakeOkPacket;
import eu.matejkormuth.starving.remoteconnections.netty.packets.HandshakePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.AttributeKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ServerChannelInitializer extends ChannelInitializer {

    private static final AttributeKey<Boolean> HANDSHAKED = AttributeKey.valueOf("handshaked");
    private static final AttributeKey<String> PLAYER = AttributeKey.valueOf("player");

    private static final Logger log = LoggerFactory.getLogger(ServerChannelInitializer.class);

    public ServerChannelInitializer() {
        super(new DefaultProtocol());
    }

    @Override
    protected void initChannel0(SocketChannel ch) {
        ch.pipeline().addLast(new DefaultPacketChannelInHandler());
        ch.attr(HANDSHAKED).set(false);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        if (ctx.channel().attr(HANDSHAKED).get()) {
            ctx.writeAndFlush(new DisconnectPacket("Internal Server Error: "
                    + cause.toString()));
        }
        ctx.close();
    }

    private final class DefaultPacketChannelInHandler extends
            PacketChannelInboundHandler {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Packet msg)
                throws Exception {
            if (msg instanceof HandshakePacket) {
                // Verify nickname and access key.
                if (this.verifyHandshake((HandshakePacket) msg)) {
                    // Handshake OK, continue.
                    ctx.channel().attr(HANDSHAKED).set(true);
                    ctx.channel().attr(PLAYER).set(
                            ((HandshakePacket) msg).nickname);
                    log.info("Creating RconAccess for player "
                            + ((HandshakePacket) msg).nickname);
                    ctx.writeAndFlush(new HandshakeOkPacket());

                    // FIXME: Both, worldgen and remoteDebug components were removed. We need to change client also.

                    // ctx.writeAndFlush(new WGFiltersPacket(Starving.getInstance().getWorldGenManager().getFilters()));
                    // Add to debug handler.
                    // Starving.getInstance().getRemoteDebugAppender().addHandler(ctx);
                } else {
                    // Handshake failed. Disconnect socket.
                    ctx.writeAndFlush(new DisconnectPacket(
                            "Player is not online or access key is invalid!"));
                    log.info(ctx.channel().remoteAddress().toString()
                            + " tried to create RconAccess for player "
                            + ((HandshakePacket) msg).nickname);
                    ctx.close();
                }
            } else {
                // Generic packet incoming.
                if (ctx.channel().attr(HANDSHAKED).get()) {
                    processPacket(ctx, msg);
                } else {
                    ctx.writeAndFlush(new DisconnectPacket("Not handshaked!"));
                    ctx.close();
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            cause.printStackTrace();
            if (ctx.channel().attr(HANDSHAKED).get()) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                cause.printStackTrace(pw);
                ctx.writeAndFlush(new DisconnectPacket(
                        "Internal Server Error: "
                                + sw.toString()));
            }
            ctx.close();
        }

        private boolean verifyHandshake(HandshakePacket msg) {
            Player p = Bukkit.getPlayer(msg.nickname);
            if (p == null) {
                return false;
            }

            String accessKey = Data.of(p).getRemoteAccessKey();
            if (accessKey == null) {
                return false;
            }

            return accessKey.equals(msg.accesskey);
        }

        private void processPacket(ChannelHandlerContext ctx, Packet msg) {
            ServerPacketProcessor.incoming(ctx,
                    ctx.channel().attr(PLAYER).get(), msg);
        }
    }
}
