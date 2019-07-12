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
package eu.matejkormuth.starving.sounds.new3.emitters;

import eu.matejkormuth.starving.sounds.new3.Sound;
import eu.matejkormuth.starving.sounds.new3.SoundEvent;
import lombok.Data;

import javax.annotation.Nonnull;

@Data
public class SimpleEmitter extends LocationEmitter {

    /**
     * Sound that is this emitter emits.
     */
    private Sound sound;

    /**
     * Whether to check is sound is playing to prevent overlapping.
     */
    private boolean preventOverlapping = false;

    /**
     * Whether the sound is playing at this moment.
     */
    private boolean playing = false;

    /**
     * Constructs new SimpleEmitter with specified sound.
     *
     * @param sound sound
     */
    public SimpleEmitter(@Nonnull Sound sound) {
        this.sound = sound;
    }

    /**
     * Emits sound at this moment.
     */
    public void emit() {
        if (!preventOverlapping || !playing) {
            emit(SoundEvent.of(sound, location));
        }
    }
}
