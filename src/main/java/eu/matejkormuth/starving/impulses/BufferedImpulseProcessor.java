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
package eu.matejkormuth.starving.impulses;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class BufferedImpulseProcessor implements ImpulseProcessor {
    // Max hear distance = 50 blocks.
    private static final double MAX_HEAR_DISTANCE_SQUARED = 25 * 25;
    // Max power = 1.0d. (1.0 can be heard in 25 blocks radius).
    private static final double MAX_POWER = 1;

    private List<Imp> buffer;
    private TargetProvider provider;

    public BufferedImpulseProcessor() {
        this.buffer = new ArrayList<Imp>();
    }

    @Override
    public void impulse(Location location, float power) {
        if (power > MAX_POWER) {
            throw new IllegalArgumentException("power");
        }

        this.buffer.add(new Imp(location, power));
    }

    public void process() {
        // Do not allow more pushes to buffer.
        synchronized (this.buffer) {
            // Declare locals.
            ImpulseTarget target = null;
            double dSq;
            float cPower;
            // Get targets.
            List<ImpulseTarget> targets = this.provider.getTargets();
            // Finally applied impulses.
            Imp[] applied = new Imp[targets.size()];
            for (int i = 0; i < targets.size(); i++) {
                target = targets.get(i);
                if (target.canReceiveImpulse()) {
                    // Check each impulse
                    for (Imp imp : this.buffer) {
                        // Squared distance from impulse to target.
                        dSq = distSquared(imp, target);
                        // If can this target hear this impulse.
                        if (this.distSquared(imp, target) < MAX_HEAR_DISTANCE_SQUARED) {
                            // Get power of impulse at target's location.
                            cPower = (float) (dSq / MAX_HEAR_DISTANCE_SQUARED
                                    * imp.power / MAX_POWER);
                            if (applied[i] == null) {
                                applied[i] = new Imp(imp.loc, cPower);
                            } else {
                                if (applied[i].power < cPower) {
                                    applied[i].power = cPower;
                                }
                            }
                        }
                    }
                }
            }
            // Apply to targets.
            for (int i = 0; i < targets.size(); i++) {
                target = targets.get(i);
                if (applied[i] != null) {
                    target.onImpulse(applied[i].loc, applied[i].power);
                }
            }
            // Clear buffer.
            this.buffer.clear();
        }
    }

    private double distSquared(final Imp imp, final ImpulseTarget target) {
        return Math.pow(imp.loc.getX() - target.getLocation().getX(), 2)
                + Math.pow(imp.loc.getY() - target.getLocation().getY(), 2)
                + Math.pow(imp.loc.getZ() - target.getLocation().getZ(), 2);
    }

    private class Imp {
        public Location loc;
        public float power;

        public Imp(Location loc, float power) {
            this.loc = loc;
            this.power = power;
        }
    }
}
