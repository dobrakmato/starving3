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

import eu.matejkormuth.bmboot.Dependency;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.main.DelayedTask;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.nms.NMSModule;
import eu.matejkormuth.starving.sounds.emitters.SoundEmitter;
import eu.matejkormuth.starving.sounds.tasks.SoundEmittersEmitTask;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SoundsModule extends Module {

    @Dependency
    private NMSModule nmsModule;

    @Delegate
    private SoundProcessor soundProcessor;

    /**
     * All sound emmiters.
     */
    private List<SoundEmitter> emitters = new ArrayList<>();

    @Override
    public void onEnable() {
        soundProcessor = new SoundProcessor(nmsModule.getNms());

        Sound.module = this;

        DelayedTask.of(() -> {
            log.info("Registering sound emitters...");
            // Load emitters from database.
            SoundEmitter soundEmitter = new SoundEmitter();
            soundEmitter.setSound(Sounds.DRAMATIC);
            soundEmitter.setLocation(new Location(Bukkit.getWorld("flatworld"), 2308, 25, 2090));
            soundEmitter.setSoundLength(6000);

            emitters.add(soundEmitter);

        }).schedule(Time.ofSeconds(15));
        // Static initializtion.

        // Start tasks.
        new SoundEmittersEmitTask().schedule(Time.ofTicks(1L));
    }

    @Override
    public void onDisable() {
        Sound.module = null;
    }

    public SoundProcessor getSoundProcessor() {
        return soundProcessor;
    }

    public List<SoundEmitter> getEmitters() {
        return emitters;
    }
}
