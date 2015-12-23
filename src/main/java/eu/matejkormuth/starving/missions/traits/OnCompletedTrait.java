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
package eu.matejkormuth.starving.missions.traits;

import eu.matejkormuth.starving.main.Trait;
import eu.matejkormuth.starving.missions.listeners.MissionListener;
import eu.matejkormuth.starving.missions.storage.PlayerMissionContext;
import eu.matejkormuth.starving.missions.rewards.Reward;

import java.util.ArrayList;
import java.util.List;

@Trait
public class OnCompletedTrait {

    /**
     * Used to store actions that are executed when this goal is
     * completed. This may be for example rewards or other things.
     * <p>
     * This field is lazy loaded.
     */
    private List<MissionListener> onCompletedListeners;

    /**
     * Registers specific reward to be given to player who
     * completes this goal.
     * <p>
     * This is just a readability method with same effect as
     * addOnCompletedListener(MissionListener).
     *
     * @param reward the reward
     */
    public void addReward(Reward reward) {
        this.addOnCompletedListener(reward);
    }

    /**
     * Adds listener which will be notified when this goal is
     * completed by someone.
     *
     * @param listener listener to register
     */
    public void addOnCompletedListener(MissionListener listener) {
        if (this.onCompletedListeners == null) {
            this.onCompletedListeners = new ArrayList<>();
        }
        this.onCompletedListeners.add(listener);
    }

    public void complete0(PlayerMissionContext context) {
        if (this.onCompletedListeners != null) {
            runOnCompletedListeners(context);
        }
    }

    /**
     * Runs all registered listeners and catches possible exceptions.
     *
     * @param context context to use
     */
    private void runOnCompletedListeners(PlayerMissionContext context) {
        for (MissionListener listener : this.onCompletedListeners) {
            try {
                listener.run(context);
            } catch (Exception e) {
                throw new RuntimeException("Mission listener " + listener.getClass().getName() + " threw exception!", e);
            }
        }
    }
}
