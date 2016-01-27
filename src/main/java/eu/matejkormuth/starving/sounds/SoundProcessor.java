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
package eu.matejkormuth.starving.sounds;

import eu.matejkormuth.starving.nms.NMS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundProcessor {

    private static final Logger log = LoggerFactory.getLogger(SoundProcessor.class);

    /**
     * Default max. hearing distance when sound is played at volume of 1.0.
     * Source: http://gaming.stackexchange.com/a/17956
     */
    private static final float DEFAULT_MAX_HEARING_DISTANCE = 16f;

    private final NMS nms;

    public SoundProcessor(NMS nms) {
        this.nms = nms;
    }

    /**
     * Plays the specified sound to all players with properties
     * set by this sound processor.
     *
     * @param sound    sound to play
     * @param location location of sound
     */
    public void playSound(Sound sound, Location location) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (sound.getVolume() > 1000) {
                playSoundMaxVolFor(sound, player);
            } else {
                if (sound.getType() != SoundType.SINGLE) {
                    log.warn("Tried to play sound {} with extended hearing range. This is discouraged because the" +
                            " extended sound range has effect while is player not moving only! This may completely break " +
                            "the sounds that are meant to change their location dynamically (for ex. long sounds).");
                    log.warn("Consider using playSoundRaw(...) method.");
                }
                playSound(sound, player, location);
            }
        }
    }

    /**
     * Plays sound using vanilla methods for volume and pitch.
     *
     * @param sound    sound to play
     * @param location location of sound
     */
    public void playSoundRaw(Sound sound, Location location) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playSoundRawFor(sound, player, location);
        }
    }

    /**
     * Plays sound using vanilla methods for volume and pitch.
     *
     * @param sound    sound to play
     * @param player   player to play sound to
     * @param location location of sound
     */
    public void playSoundRawFor(Sound sound, Player player, Location location) {
        this.nms.playNamedSoundEffect(player, sound.getName(), location, sound.getVolume(), sound.getPitch());
    }

    /**
     * Plays specified sound at maximum volume, therefore makes
     * the sound location independent.
     *
     * @param sound  sound to play
     * @param player player to play sound to
     */
    public void playSoundMaxVolFor(Sound sound, Player player) {
        this.nms.playNamedSoundEffectMaxVolume(player, sound.getName(), player.getLocation());
    }

    /**
     * Plays specified sound at specified volume, with specified
     * pitch and location of sound.
     *
     * @param sound    sound to play
     * @param player   player to play sound to
     * @param location location of sound
     */
    public void playSound(Sound sound, Player player, Location location) {
        // Check for mistake calls.
        if (sound.getVolume() > 1000) {
            this.playSoundMaxVolFor(sound, player);
            return;
        }

        float distance = (float) location.distance(player.getEyeLocation());

        // Do not play for far away players.
        if (distance > sound.getMaxHearingDistance()) {
            return;
        }

        // If totally near the sound, don't care stop computing useless things.
        if (distance < 0.1f) {
            // Actually play the sound for this player.
            this.nms.playNamedSoundEffect(player, sound.getName(), location, sound.getVolume(), sound.getPitch());
            return;
        }

        // TODO: Linear?
        float volume = 1f - (distance / sound.getMaxHearingDistance());
        float pitch = sound.getPitch();

        // Fix the stereo - location from which is the sound coming.
        Vector from = location.clone().subtract(player.getEyeLocation()).toVector().normalize();

        // TODO: Under water?
        // TODO: Different sounds from distance?

        // Actually play the sound for this player.
        this.nms.playNamedSoundEffect(player, sound.getName(), player.getEyeLocation().clone().add(from),
                volume + (1f / DEFAULT_MAX_HEARING_DISTANCE), pitch);
    }
}
