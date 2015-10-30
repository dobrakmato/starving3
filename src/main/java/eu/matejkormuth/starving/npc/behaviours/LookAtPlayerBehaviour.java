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
package eu.matejkormuth.starving.npc.behaviours;

import eu.matejkormuth.starving.main.NMSHooks;
import eu.matejkormuth.starving.npc.behaviours.base.AbstractBehaviour;
import eu.matejkormuth.starving.persistence.Persist;
import org.bukkit.craftbukkit.v1_8_R3.TrigMath;
import org.bukkit.entity.Player;

@NMSHooks(version = "v1_8_R3")
public class LookAtPlayerBehaviour extends AbstractBehaviour {

    @Persist(key = "MAX_FOLLOW_DIST")
    private static final double MAX_FOLLOW_DIST = 8;
    private static final double MAX_FOLLOW_DIST_SQUARED = MAX_FOLLOW_DIST
            * MAX_FOLLOW_DIST;
    private Player target;

    @Override
    public void tick(long currentTick) {
        if (currentTick % 10 == 0) {
            checkDistance();
            findTarget();
        }
        rotate();
    }

    private void checkDistance() {
        if (target != null) {
            if (this.owner.getLocation().distanceSquared(target.getLocation()) > MAX_FOLLOW_DIST_SQUARED) {
                this.target = null;
            }
        }
    }

    private void findTarget() {
        if (target == null) {
            this.owner.getNearbyPlayers(MAX_FOLLOW_DIST)
                    .stream()
                    .filter(p -> this.owner.hasLineOfSight(p))
                    .forEach(p -> this.target = p);
        }
    }

    private void rotate() {
        float yaw = -1
                * (float) (TrigMath.atan2(target.getLocation().getX()
                        - this.owner.getLocation().getX(), target.getLocation()
                        .getZ()
                        - this.owner.getLocation().getZ()) * 180 / Math.PI);
        this.owner.setYaw(yaw);
    }

}
