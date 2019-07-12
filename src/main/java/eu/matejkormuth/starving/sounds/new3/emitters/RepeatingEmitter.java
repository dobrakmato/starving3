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

import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.sounds.new3.Sound;
import eu.matejkormuth.starving.sounds.new3.SoundEvent;
import eu.matejkormuth.starving.sounds.new3.exceptions.SoundLengthUndefinedException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

@Slf4j
@ToString
public class RepeatingEmitter extends LocationEmitter {

    /**
     * Sound that is this emitter emits.
     */
    @Getter
    @Setter
    private Sound sound;

    /**
     * Task that takes care of repeating.
     */
    private RepeatingTask task;

    /**
     * Constructs new RepeatingEmitter with specified sound.
     *
     * @param sound sound
     */
    public RepeatingEmitter(@Nonnull Sound sound) {
        this.sound = sound;

        if (sound.getLength() == Sound.LENGTH_UNDEFINED) {
            throw new SoundLengthUndefinedException(sound.toString());
        }
    }

    /**
     * Starts emitting sound repeatably.
     */
    public void beginEmitting() {
        // Emit sound first time.
        emit();
        // Schedule task.
        if (task == null) {
            Time length = Time.ofMilliseconds(sound.getLength());
            task = RepeatingTask.of(this::emit).schedule(length, length);
        } else {
            log.warn("RepeatingEmitter {} already emitting repeating sound!", this);
        }
    }

    /**
     * Actually emits the sound.
     */
    private void emit() {
        emit(SoundEvent.of(sound, location));
    }

    /**
     * Stops emitting sound.
     */
    public void endEmitting() {
        if (task != null) {
            task.cancel();
            task = null;
        } else {
            log.warn("RepeatingEmitter {} is not emitting repeating sound!", this);
        }
    }
}
