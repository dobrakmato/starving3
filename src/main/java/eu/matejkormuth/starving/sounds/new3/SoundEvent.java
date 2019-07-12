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
package eu.matejkormuth.starving.sounds.new3;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Location;

@ToString
public final class SoundEvent {

    /**
     * Played sound.
     */
    @Getter
    private final Sound sound;

    /**
     * Location of played sound.
     */
    @Getter
    @Setter
    private Location location;

    /**
     * Volume of this sound event.
     */
    @Getter
    @Setter
    private float volume = 1;

    /**
     * Pitch of this sound event.
     */
    @Getter
    @Setter
    private float pitch = 1;

    /**
     * Whether this sound event has been discarded.
     */
    @Getter
    private boolean discarded;

    public SoundEvent(Sound sound, Location location) {
        this.sound = sound;
        this.location = location;
    }

    public SoundEvent(Sound sound, Location location, float volume, float pitch) {
        this.sound = sound;
        this.location = location;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static SoundEvent of(Sound sound, Location at) {
        return new SoundEvent(sound, at);
    }

    public static SoundEvent of(Sound sound, Location at, float volume, float pitch) {
        return new SoundEvent(sound, at, volume, pitch);
    }

    /**
     * Marks this sound event as discarded.
     */
    public void discard() {
        discarded = true;
    }
}
