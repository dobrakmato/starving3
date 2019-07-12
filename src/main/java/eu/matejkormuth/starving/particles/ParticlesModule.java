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
package eu.matejkormuth.starving.particles;

import com.darkblade12.particleeffect.ParticleEffect;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.main.DelayedTask;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.particles.tasks.ParticleEmittersEmitTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ParticlesModule extends Module {

    private List<ParticleEmitter> emitters = new ArrayList<>();

    @Override
    public void onEnable() {
        DelayedTask.of(() -> {
            // Load emitters from database.
            ParticleEmitter dym1 = new ParticleEmitter();
            dym1.setEffect(ParticleEffect.SMOKE_LARGE);
            dym1.setLocation(new Location(Bukkit.getWorld("flatworld"), 2299.5, 34.5, 2186.5));
            dym1.setAmount(20);
            dym1.setOffsets(1, 1, 1);
            dym1.setDirection(new Vector(-1, 0.1, 0));

            ParticleEmitter dym2 = new ParticleEmitter();
            dym2.setEffect(ParticleEffect.SMOKE_LARGE);
            dym2.setLocation(new Location(Bukkit.getWorld("flatworld"), 2325, 36, 2085.5));
            dym2.setAmount(20);
            dym2.setOffsets(1, 1, 1);
            dym2.setDirection(new Vector(-1, 0.2, 0));

            emitters.add(dym1);
            emitters.add(dym2);
        }).schedule(20 * 10);

        // Start tasks.
        new ParticleEmittersEmitTask(this).schedule(Time.ofTicks(1));
    }

    @Override
    public void onDisable() {
    }

    public List<ParticleEmitter> getEmitters() {
        return emitters;
    }
}
