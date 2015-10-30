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
package eu.matejkormuth.starving.npc.types;

import com.mojang.authlib.GameProfile;
import eu.matejkormuth.starving.main.NMSHooks;
import eu.matejkormuth.starving.npc.K;
import eu.matejkormuth.starving.npc.NPC;
import eu.matejkormuth.starving.npc.NPCRegistry;
import eu.matejkormuth.starving.npc.behaviours.base.AbstractBehaviour;
import eu.matejkormuth.starving.npc.behaviours.base.BehaviourHolder;
import eu.matejkormuth.starving.npc.util.NullNetworkManager;
import eu.matejkormuth.starving.npc.util.NullPlayerConnection;
import eu.matejkormuth.starving.npc.util.NullSocket;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.PlayerInventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;


@NMSHooks(version = "v1_8_R3")
public class HumanNPC extends EntityPlayer implements NPC {

    private static final Logger log = LoggerFactory.getLogger(HumanNPC.class);

    // Reference of NPCRegistry of this NPC.
    private NPCRegistry registry;
    // Behaviour holder.
    private BehaviourHolder holder;
    // Whether this entity is active or not.
    private boolean active = true;

    public HumanNPC(NPCRegistry registry, GameProfile gameprofile,
                    Location spawnLocation) {
        super(MinecraftServer.getServer(), ((CraftWorld) spawnLocation
                        .getWorld()).getHandle(), gameprofile,
                new PlayerInteractManager(((CraftWorld) spawnLocation
                        .getWorld()).getHandle()));
        // Set NPCRegisrty.
        this.registry = registry;

        // Replace playerConnection with custom connection.
        try {
            Socket socket = new NullSocket();
            NetworkManager conn = new NullNetworkManager(
                    EnumProtocolDirection.CLIENTBOUND);
            this.playerConnection = new NullPlayerConnection(MinecraftServer
                    .getServer(),
                    conn, this);
            conn.a(playerConnection);
            socket.close();
        } catch (IOException e) {
        }
        // Allow step climbing.
        this.S = 1;
        // Set default gamemode.
        this.playerInteractManager.setGameMode(EnumGamemode.SURVIVAL);

        // Create behavior holder.
        this.holder = new BehaviourHolder();

        // Add to world.
        this.setLocation(spawnLocation.getX(), spawnLocation.getY(),
                spawnLocation.getZ(), spawnLocation.getYaw(), spawnLocation
                        .getPitch());
        world.addEntity(this);

    }

    // Override vanilla methods.
    @Override
    protected void doTick() {
        // Tick update is in tickEntity();
    }

    public void tickEntity() {
        this.registry.profiler.start(K.NPC_DO_TICK);
        // Sets head rotation to yaw;
        this.aI = this.yaw;
        this.registry.profiler.start(K.NPC_BEHAVIOURS);
        this.holder.tick();
        this.registry.profiler.start(K.NPC_BEHAVIOURS);
        this.registry.profiler.end(K.NPC_DO_TICK);
    }

    // Some extensional methods.
    public void setInvulnerable(boolean invulnerable) {
        this.abilities.isInvulnerable = invulnerable;
    }

    public void setFlying(boolean flying) {
        this.abilities.isFlying = true;
    }

    public boolean isFlying() {
        return this.abilities.isFlying;
    }

    public PlayerInventory getInventory() {
        return new CraftInventoryPlayer(this.inventory);
    }

    // Behaviour delegate methods.
    public <T extends AbstractBehaviour> T getBehaviour(Class<T> type) {
        return holder.getBehaviour(type);
    }

    public boolean hasBehaviour(Class<? extends AbstractBehaviour> type) {
        return holder.hasBehaviour(type);
    }

    public void addBehaviour(AbstractBehaviour abstractBehaviour) {
        holder.addBehaviour(abstractBehaviour);
    }

    // Other NPC methods.
    @Override
    public Location getLocation() {
        return new Location(this.world.getWorld(), this.lastX, this.lastY,
                this.lastZ, this.yaw, this.pitch);
    }

    @Override
    public boolean hasLineOfSight(LivingEntity e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setActive(boolean active) {
        if (active) {
            log.info(ChatColor.GRAY + "NPC " + this.getUniqueID() + " is now activated!");
        } else {
            log.info(ChatColor.DARK_GRAY + "NPC " + this.getUniqueID() + " is now deactivated!");
        }
        this.active = active;
    }

    @Override
    public void teleport(Location location) {
        teleport(location, TeleportCause.PLUGIN);
    }

    @Override
    public void teleport(Location location, TeleportCause cause) {
        if ((this.passenger != null) || (this.dead)) {
            throw new RuntimeException(
                    "Can't teleport HumanNPC because it is dead or has passanger on it.");
        }

        this.mount(null);

        if (!(location.getWorld().equals(getWorld()))) {
            this.teleportTo(location, cause
                    .equals(TeleportCause.NETHER_PORTAL));
            return;
        }

        this.setLocation(location.getX(), location.getY(), location
                .getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        this.setYawPitch(yaw, pitch);
    }

    public boolean v() {
        return false;
    }

    @Override
    public NPCRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public void remove0() {
        this.dead = true;
        // TODO: Inform NPCRegistry about dead.
    }
}
