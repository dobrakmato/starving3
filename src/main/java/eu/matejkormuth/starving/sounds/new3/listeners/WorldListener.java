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
package eu.matejkormuth.starving.sounds.new3.listeners;

import eu.matejkormuth.starving.main.NMSHooks;
import eu.matejkormuth.starving.sounds.new3.Listener;
import eu.matejkormuth.starving.sounds.new3.SoundEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * Represents listener in world.
 */
@Slf4j
@Data
public class WorldListener implements Listener {

    /**
     * World the listener is listening in.
     */
    private final World world;

    /**
     * This allows us to filter players in this world without creating
     * more objects (Location, ...).
     */
    @NMSHooks(version = "v1_8_R3")
    private final transient Predicate<Player> OPTIMIZED_NMS_WORLD_MATCHER;

    /**
     * Constructs new WorldListener in specified world.
     *
     * @param world world this listener is listening to
     */
    public WorldListener(@Nonnull World world) {
        this.world = world;
        OPTIMIZED_NMS_WORLD_MATCHER = p -> ((CraftPlayer) p).getHandle().getWorld().getWorld() == this.world;
    }

    @Override
    public void listen(@Nonnull SoundEvent event) {
        if (!event.isDiscarded()) {
            if (checkWorld(event)) {
                broadcast(event);
            } else {
                log.warn("Listener {} got {} from different world!", this, event);
            }
        }
    }

    /**
     * Broadcasts this sound event to all entities in this world.
     *
     * @param event sound event
     */
    private void broadcast(@Nonnull SoundEvent event) {
        // Only player can hear sounds in our World.
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(OPTIMIZED_NMS_WORLD_MATCHER)
                .forEach(p -> this.sendSound(p, event));
    }

    /**
     * Plays the specified sound to specified player.
     *
     * @param player player to play this sound to
     * @param event  sound to play
     */
    private void sendSound(@Nonnull Player player, @Nonnull SoundEvent event) {
        // TODO: Perform hearing distance extending and sound effects based
        // TODO: on multiple sound files distributed to clients.

        // For now just simply send sound event.
        player.playSound(event.getLocation(), event.getSound().getName(),
                event.getVolume(), event.getPitch());
    }

    /**
     * Checks if specified sound event belongs to this world.
     *
     * @param sound sound event to check
     * @return true if event belongs to this world, false otherwise
     */
    private boolean checkWorld(@Nonnull SoundEvent sound) {
        return this.world == sound.getLocation().getWorld();
    }
}
