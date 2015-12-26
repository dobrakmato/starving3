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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Data
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Sound {

    /**
     * Reference to sound module to allow sounds to play.
     */
    static SoundsModule module;

    /**
     * Internal name of currently played sound effect.
     */
    private final String name;

    /**
     * Volume of played sound.
     */
    @Wither
    private float volume = 1;

    /**
     * Pitch of played sound.
     */
    @Wither
    private float pitch = 1;

    /**
     * Maximum hearing distance of this sound.
     */
    @Wither
    private float maxHearingDistance = 64f;

    /**
     * Category of played sound.
     */
    private SoundCategory category = SoundCategory.UNCATEGORIZED;

    /**
     * Type of sound.
     */
    private final SoundType type;

    public Sound(String name, float maxHearingDistance, SoundType type) {
        this.name = name;
        this.maxHearingDistance = maxHearingDistance;
        this.type = type;
    }

    // Required by withers.
    private Sound(String name, float volume, float pitch, float maxHearingDistance, SoundCategory category, SoundType type) {
        this.name = name;
        this.volume = volume;
        this.pitch = pitch;
        this.maxHearingDistance = maxHearingDistance;
        this.category = category;
        this.type = type;
    }

    /**
     * Plays this sound for all players.
     *
     * @param location location of this sound
     */
    public void play(Location location) {
        module.playSound(this, location);
    }

    /**
     * Plays this sound for specified player.
     *
     * @param location location of this sound
     * @param player   player to play this sound to
     */
    public void play(Location location, Player player) {
        module.playSound(this, player, location);
    }

    /**
     * Plays this sound using vanilla methods for volume and pitch.
     *
     * @param location location of sound
     */
    public void playRaw(Location location) {
        module.playSoundRaw(this, location);
    }

    /**
     * Plays this sound using vanilla methods for volume and pitch.
     *
     * @param location location of sound
     * @param player   player to play this sound to
     */
    public void playRaw(Location location, Player player) {
        module.playSoundRawFor(this, player, location);
    }

    /**
     * Plays this sound using vanilla methods for volume and pitch with specified parameters.
     *
     * @param p        player to play sound to
     * @param location location of sound
     * @param volume   volume of sound
     * @param pitch    pitch of sound
     * @deprecated Use withers and play() method.
     */
    @Deprecated
    public void play(Player p, Location location, float volume, float pitch) {
        new Sound(this.name, volume, pitch, 16f, this.category, this.type).playRaw(location, p);
    }
}
