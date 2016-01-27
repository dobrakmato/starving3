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
package eu.matejkormuth.starving.sounds.emitters;

import eu.matejkormuth.bmboot.facades.Container;
import eu.matejkormuth.starving.nms.NMS;
import eu.matejkormuth.starving.sounds.Sound;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents entity which emits sound to all listening entities around.
 */
@ToString
@Slf4j
public class SoundEmitter {

    @Getter
    private Location location;
    @Getter
    @Setter
    private Sound sound;

    /**
     * Length of the sound in milliseconds.
     */
    @Getter
    @Setter
    private long soundLength; // in ms
    @Getter
    @Setter
    private long lastEmitted; // in ms

    /**
     * Whether this emitter should emit sound in this tick or not.
     *
     * @return true if it should, false otherwise
     */
    public boolean shouldEmit() {
        return System.currentTimeMillis() + 100 >= lastEmitted + soundLength;
    }

    public void setLocation(@Nonnull Location location) {
        log.info("Setting location of {} to {}.", this, location);

        if (location.getWorld() == null) {
            this.location = location;
        }
        this.location = location;
    }

    /**
     * Plays the sound at specified location.
     */
    public void emit() {
        lastEmitted = System.currentTimeMillis();
        for (Player player : Bukkit.getOnlinePlayers()) {
            World playerWorld = player.getWorld();
            World thisWorld = location.getWorld();

            if (playerWorld != thisWorld) {
                continue;
            }

            if (player.getLocation().distance(location) < 32) {
                Container.get(NMS.class).playNamedSoundEffect(player, this.sound.getName(), location, sound.getVolume(), sound.getPitch());
            }
        }
    }
}
