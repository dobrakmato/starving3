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

import eu.matejkormuth.starving.main.Named;
import eu.matejkormuth.starving.missions.storage.PlayerMissionContext;
import eu.matejkormuth.starving.missions.traits.OnCompletedTrait;
import eu.matejkormuth.starving.traits.Trait;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public abstract class Goal implements Completable, Named {

    @Getter
    @Setter
    private String name;

    /**
     * The location of this goal. Can be used to display arrows or to
     * find path from player's position to this goal and display it
     * later on map.
     */
    @Setter
    private Location targetLocation;

    /**
     * The mission this goal belongs to.
     */
    private final Mission mission;

    /**
     * Add on completed listener support.
     */
    @Delegate
    private OnCompletedTrait completedTrait = Trait.of(OnCompletedTrait.class);

    /**
     * Creates new goal with specified name for specific mission.
     *
     * @param name    name of new goal
     * @param mission parent mission of new goal
     */
    public Goal(String name, Mission mission) {
        this.name = name;
        this.mission = mission;
    }

    @Override
    public void complete(Player player) {
        PlayerMissionContext ctx = new PlayerMissionContext(player, this.mission);
        // Mark mission in context storage.
        ctx.getMissionStorage().set("completed", true);
        // Execute trait.
        this.complete0(ctx);
    }

    @Override
    public void onCompleted(PlayerMissionContext context) {
        // Do not require to implement.
    }

    @Override
    public void start(Player player) {
        // Do not require to implement.
    }

    @Override
    public void onStarted(PlayerMissionContext context) {
        // Do not require to implement.
    }

    @Override
    public boolean isCompleted(PlayerMissionContext context) {
        // dunno...
        throw new RuntimeException("Please do not call this method. Thanks :))");
    }

    /**
     * Returns the mission this goal belongs to.
     *
     * @return mission of this goal
     */
    public Mission getMission() {
        return mission;
    }

    /**
     * Returns the location of target of this goal. May be null.
     *
     * @return null of target location of this goal
     */
    @Nullable
    public Location getTargetLocation() {
        return targetLocation;
    }
}
