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

import eu.matejkormuth.starving.main.DelayedTask;
import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.sounds.new3.Sound;
import eu.matejkormuth.starving.sounds.new3.SoundEvent;

public class TriSoundEmitter extends LocationEmitter {
    private final Sound startSound;
    private final Sound loopSound;
    private final Sound endSound;

    private RepeatingTask task;
    private long lastPlayerMillis;

    /**
     * Constructs new TriSound emitter with specified three sounds and one attached listener.
     *
     * @param startSound start sound
     * @param loopSound  loop sound
     * @param endSound   end sound
     */
    public TriSoundEmitter(Sound startSound, Sound loopSound, Sound endSound) {
        this.startSound = startSound;
        this.loopSound = loopSound;
        this.endSound = endSound;
    }

    /**
     * Stats this TriSound with playing start sound immediately. Then continues to loop
     * sound.
     */
    public void begin() {
        // Play start sound.
        emit(SoundEvent.of(startSound, location));
        // Schedule loop start.
        DelayedTask.of(this::loop).schedule(Time.ofMilliseconds(startSound.getLength()));
    }

    /**
     * Plays the loop section over and over.
     */
    private void loop() {
        if (task == null) {
            task = RepeatingTask.of(this::loop).schedule(Time.ofMilliseconds(loopSound.getLength()));
        }
        emit(SoundEvent.of(loopSound, location));
        lastPlayerMillis = System.currentTimeMillis();
    }

    /**
     * Requests end of this TriSound. Makes sure that the currently played loop sound
     * is finished before starting end sound.
     */
    public void end() {
        long unplayedMillis = loopSound.getLength() - (System.currentTimeMillis() - lastPlayerMillis);
        task.cancel();
        DelayedTask.of(this::end0).schedule(Time.ofMilliseconds(unplayedMillis));
    }

    /**
     * Stops this TriSound without waiting for currently played loop sound to end.
     */
    public void endImmediatelly() {
        task.cancel();
        emit(SoundEvent.of(endSound, location));
    }

    /**
     * Actaully plays the end sound.
     */
    private void end0() {
        emit(SoundEvent.of(endSound, location));
    }
}
