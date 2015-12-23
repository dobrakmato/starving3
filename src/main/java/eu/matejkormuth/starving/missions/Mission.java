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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Mission implements Completable, Named {

    @Getter
    @Setter
    private String name;

    /**
     * Add on completed listener support.
     */
    @Delegate
    private OnCompletedTrait completedTrait = Trait.of(OnCompletedTrait.class);

    /**
     * List of all mission goals.
     */
    private final List<Goal> goals = new ArrayList<>();

    /**
     * Id of this mission.
     */
    private final int id;

    /**
     * Creates new mission instance with specified name and  unique id.
     *
     * @param name mission name
     * @param id   mission unique id
     */
    public Mission(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Adds specific goal to this mission.
     *
     * @param goal goal to add
     */
    public void addGoal(Goal goal) {
        this.goals.add(goal);
    }

    /**
     * Returns internal list of all goals in this mission.
     *
     * @return list of all goal in this mission
     */
    public List<Goal> getGoals() {
        return goals;
    }

    @Override
    public void complete(Player player) {
        PlayerMissionContext ctx = new PlayerMissionContext(player, this);

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
        // If value does not exists, following call returns false.
        return context.getMissionStorage().getBoolean("completed");
    }

    /**
     * Returns identifier of this mission.
     *
     * @return unique identifier of this mission
     */
    public int getId() {
        return this.id;
    }
}
