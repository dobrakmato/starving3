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
package eu.matejkormuth.starving.missions;

import java.util.List;

public abstract class Mission {
    private final String id;
    private final String name;
    private final String description;

    private final List<Mission> requiredCompleted;
    private final List<Goal> missionGoals;
    private final List<Reward> missionRewards;

    public Mission(String id, String name, String description,
            List<Mission> requiredCompleted, List<Goal> missionGoals,
            List<Reward> missionRewards) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.requiredCompleted = requiredCompleted;
        this.missionGoals = missionGoals;
        this.missionRewards = missionRewards;
    }

    public boolean canStart(MissionPlayerContext ctx) {
        for (Mission m : requiredCompleted) {
            if (!m.isCompleted(ctx)) {
                return false;
            }
        }
        return true;
    }

    public boolean isCompleted(MissionPlayerContext ctx) {
        // First check storage.
        if (ctx.isCompleted(this)) {
            return true;
        }

        // Check mission goals.
        for (Goal g : missionGoals) {
            if (!g.isCompleted(ctx)) {
                return false;
            }
        }

        // Mission is completed, save it.
        ctx.setCompleted(this, true);
        return true;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Goal> getMissionGoals() {
        return missionGoals;
    }

    public List<Reward> getMissionRewards() {
        return missionRewards;
    }

    public List<Mission> getRequiredCompleted() {
        return requiredCompleted;
    }
}
