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
package eu.matejkormuth.starving.rockets;

import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.main.Time;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RocketUniverse {
    // List of rockets in this universe.
    private List<Rocket> rockets;
    private RepeatingTask task;

    public RocketUniverse() {
        this.rockets = new ArrayList<Rocket>();
    }

    public void startSimulation() {
        // Schedule repeating task.
        this.task = RepeatingTask.of(this::updateRockets).schedule(Time.ofTicks(1));
    }

    public void stopSimulation() {
        this.task.cancel();
    }

    public void addRocket(Rocket rocket) {
        this.rockets.add(rocket);
    }

    private void updateRockets() {
        for (Iterator<Rocket> iterator = this.rockets.iterator(); iterator.hasNext(); ) {
            Rocket r = iterator.next();

            // Remove dead rockets.
            if (r.isDead()) {
                iterator.remove();
                continue;
            }

            // Otherwise update the rocket.
            r.update();
        }
    }
}
