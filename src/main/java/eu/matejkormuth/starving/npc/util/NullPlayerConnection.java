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

import java.util.Set;

import eu.matejkormuth.starving.main.NMSHooks;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.EnumProtocolDirection;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.MinecraftServer;
import net.minecraft.server.v1_8_R2.NetworkManager;
import net.minecraft.server.v1_8_R2.Packet;
import net.minecraft.server.v1_8_R2.PacketPlayInAbilities;
import net.minecraft.server.v1_8_R2.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R2.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R2.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R2.PacketPlayInChat;
import net.minecraft.server.v1_8_R2.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R2.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R2.PacketPlayInCustomPayload;
import net.minecraft.server.v1_8_R2.PacketPlayInEnchantItem;
import net.minecraft.server.v1_8_R2.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R2.PacketPlayInFlying;
import net.minecraft.server.v1_8_R2.PacketPlayInHeldItemSlot;
import net.minecraft.server.v1_8_R2.PacketPlayInKeepAlive;
import net.minecraft.server.v1_8_R2.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_8_R2.PacketPlayInSetCreativeSlot;
import net.minecraft.server.v1_8_R2.PacketPlayInSettings;
import net.minecraft.server.v1_8_R2.PacketPlayInSpectate;
import net.minecraft.server.v1_8_R2.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_8_R2.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R2.PacketPlayInTransaction;
import net.minecraft.server.v1_8_R2.PacketPlayInUpdateSign;
import net.minecraft.server.v1_8_R2.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R2.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R2.PlayerConnection;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;

@NMSHooks(version = "v1_8_R2")
public class NullPlayerConnection extends PlayerConnection {

    public NullPlayerConnection(MinecraftServer minecraftServer,
            NetworkManager conn, EntityPlayer entityplayer) {
        super(minecraftServer, new NullNetworkManager(
                EnumProtocolDirection.SERVERBOUND), entityplayer);
    }

    @Override
    public CraftPlayer getPlayer() {
        return super.getPlayer();
    }

    @Override
    public void c() {
    }

    @Override
    public NetworkManager a() {
        return super.a();
    }

    @Override
    public void disconnect(String s) {
        throw new UnsupportedOperationException("Can't disconnect NPC!");
    }

    @Override
    public void a(PacketPlayInSteerVehicle packetplayinsteervehicle) {
    }

    @Override
    public void a(PacketPlayInFlying packetplayinflying) {
    }

    @Override
    public void a(double d0, double d1, double d2, float f, float f1) {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void a(double d0, double d1, double d2, float f, float f1, Set set) {
    }

    @Override
    public void teleport(Location dest) {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void teleport(Location dest, Set set) {
    }

    @Override
    public void a(PacketPlayInBlockDig packetplayinblockdig) {
    }

    @Override
    public void a(PacketPlayInBlockPlace packetplayinblockplace) {
    }

    @Override
    public void a(PacketPlayInSpectate packetplayinspectate) {
    }

    @Override
    public void a(PacketPlayInResourcePackStatus packetplayinresourcepackstatus) {
    }

    @Override
    public void a(IChatBaseComponent ichatbasecomponent) {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void sendPacket(Packet packet) {
    }

    @Override
    public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot) {
    }

    @Override
    public void a(PacketPlayInChat packetplayinchat) {
    }

    @Override
    public void chat(String s, boolean async) {
    }

    @Override
    public void a(PacketPlayInArmAnimation packetplayinarmanimation) {
    }

    @Override
    public void a(PacketPlayInEntityAction packetplayinentityaction) {
    }

    @Override
    public void a(PacketPlayInUseEntity packetplayinuseentity) {
    }

    @Override
    public void a(PacketPlayInClientCommand packetplayinclientcommand) {
    }

    @Override
    public void a(PacketPlayInCloseWindow packetplayinclosewindow) {
    }

    @Override
    public void a(PacketPlayInWindowClick packetplayinwindowclick) {
    }

    @Override
    public void a(PacketPlayInEnchantItem packetplayinenchantitem) {
    }

    @Override
    public void a(PacketPlayInSetCreativeSlot packetplayinsetcreativeslot) {
    }

    @Override
    public void a(PacketPlayInTransaction packetplayintransaction) {
    }

    @Override
    public void a(PacketPlayInUpdateSign packetplayinupdatesign) {
    }

    @Override
    public void a(PacketPlayInKeepAlive packetplayinkeepalive) {
    }

    @Override
    public void a(PacketPlayInAbilities packetplayinabilities) {
    }

    @Override
    public void a(PacketPlayInTabComplete packetplayintabcomplete) {
    }

    @Override
    public void a(PacketPlayInSettings packetplayinsettings) {
    }

    @Override
    public void a(PacketPlayInCustomPayload packetplayincustompayload) {
    }

    @Override
    public boolean isDisconnected() {
        return false;
    }

}
