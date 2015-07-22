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
package eu.matejkormuth.starving.npc;

import eu.matejkormuth.starving.main.NMSHooks;
import eu.matejkormuth.starving.npc.behaviours.base.AbstractBehaviour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.ArrayList;
import java.util.Collection;

public interface NPC {
    boolean isActive();

    void setActive(boolean active);

    Location getLocation();

    void teleport(Location location);

    void teleport(Location location, TeleportCause cause);

    default void remove() {
        this.getRegistry().removeNPC(this);
        this.remove0();
    }

    void remove0();

    NPCRegistry getRegistry();

    boolean hasBehaviour(Class<? extends AbstractBehaviour> abstractBehaviour);

    <T extends AbstractBehaviour> T getBehaviour(
            Class<T> abstractBehaviour);

    void addBehaviour(AbstractBehaviour behaviour);

    default Collection<Player> getNearbyPlayers(double maxDistance) {
        ArrayList<Player> al = new ArrayList<>();
        Location l = this.getLocation();
        double squaredDistMax = maxDistance * maxDistance;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (l.distanceSquared(p.getLocation()) < squaredDistMax) {
                al.add(p);
            }
        }

        return al;
    }

    boolean hasLineOfSight(LivingEntity e);

    void setInvulnerable(boolean invulnerable);

    void setYaw(float yaw);

    void setPitch(float pitch);

    void setRotation(float yaw, float pitch);

    @NMSHooks(version = "v1_8_R2")
    default void spawnFor(Player player) {
        throw new UnsupportedOperationException("Method not implemented!");
        //Starving.NMS.sendPacket(player, new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER,(EntityPlayer) this));
        //Starving.NMS.sendPacket(player, new PacketPlayOutNamedEntitySpawn((EntityHuman) this));
    }

    @NMSHooks(version = "v1_8_R2")
    default void despawnFor(Player player) {
        throw new UnsupportedOperationException("Method not implemented!");
        //Starving.NMS.sendPacket(player, new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER,(EntityPlayer) this));
        //Starving.NMS.sendPacket(player, new PacketPlayOutEntityDestroy(this.getId()));
    }

    int getId();

    void tickEntity();
}
