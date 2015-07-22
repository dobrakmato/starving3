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
package eu.matejkormuth.starving.main.playtime;

import com.google.common.base.Preconditions;
import eu.matejkormuth.starving.main.Data;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.main.RepeatingTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spigotmc.CustomTimingsHandler;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides play-time base scheduler. Scheduled tasks are persisted through multiple sessions and are executed
 * only if the player is online.
 */
public class PlaytimeScheduler {

    private static final Logger log = LoggerFactory.getLogger(PlaytimeScheduler.class);

    private final CustomTimingsHandler delayedTasksTimings = new CustomTimingsHandler("Delayed playtime tasks");
    private final CustomTimingsHandler repeatingTasksTimings = new CustomTimingsHandler("Repeating playtime tasks");

    private final Map<UUID, List<PlaytimeDelayedTask>> delayedTaskMap;
    private final Map<UUID, List<PlaytimeRepeatingTask>> repeatingTaskMap;

    public PlaytimeScheduler() {
        delayedTaskMap = new HashMap<>();
        repeatingTaskMap = new HashMap<>();

        PlaytimeDelayedTask.scheduler = this;
        PlaytimeRepeatingTask.scheduler = this;

        // Create internal scheduler.
        RepeatingTask.of(this::internalTick).schedule(1L);
    }

    // Called each tick.
    private void internalTick() {
        // Create list of online players.
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        List<UUID> online = new ArrayList<>(players.size());
        online.addAll(players
                .stream()
                .map(Player::getUniqueId)
                .collect(Collectors.toList()));

        // Process delayed tasks.
        delayedTasksTimings.startTiming();
        Data data = null;
        for (Iterator<Map.Entry<UUID, List<PlaytimeDelayedTask>>> itr = delayedTaskMap.entrySet().iterator(); itr.hasNext(); ) {
            Map.Entry<UUID, List<PlaytimeDelayedTask>> entry = itr.next();

            // Get data for this player.
            data = Data.of(Bukkit.getPlayer(entry.getKey()));

            // Process only tasks of online players.
            if (!online.contains(entry.getKey())) {
                continue;
            }

            // Loop trough all tasks of this player.
            for (Iterator<PlaytimeDelayedTask> itr2 = entry.getValue().iterator(); itr.hasNext(); ) {
                PlaytimeDelayedTask task = itr2.next();
                if (task.nextRun <= data.getPlayTime()) {
                    // Check if the task was not canceled.
                    if(task.isCanceled) {
                        // Remove canceled task.
                        itr2.remove();
                        continue;
                    }

                    // Fire the task.
                    try {
                        task.run();
                    } catch (Exception e) {
                        log.error("Delayed task caused an exception!", e);
                    }
                    // Remove fired task.
                    itr2.remove();
                }
            }

            // If there are no more tasks for this player, remove his entry in map.
            if (entry.getValue().size() == 0) {
                itr.remove();
            }
        }
        delayedTasksTimings.stopTiming();

        // Process repeating tasks.
        repeatingTasksTimings.startTiming();
        for (Iterator<Map.Entry<UUID, List<PlaytimeRepeatingTask>>> itr = repeatingTaskMap.entrySet().iterator(); itr.hasNext(); ) {
            Map.Entry<UUID, List<PlaytimeRepeatingTask>> entry = itr.next();

            // Get data for this player.
            data = Data.of(Bukkit.getPlayer(entry.getKey()));

            // Process only tasks of online players.
            if (!online.contains(entry.getKey())) {
                continue;
            }

            // Loop trough all tasks of this player.
            for (Iterator<PlaytimeRepeatingTask> itr2 = entry.getValue().iterator(); itr.hasNext(); ) {
                PlaytimeRepeatingTask task = itr2.next();
                if (task.nextRun <= data.getPlayTime()) {
                    // Check if the task was not canceled.
                    if(task.isCanceled) {
                        // Remove canceled task.
                        itr2.remove();
                        continue;
                    }

                    // Fire the task.
                    try {
                        task.run();
                    } catch (Exception e) {
                        log.error("Repeated task caused an exception!", e);
                    }
                    // Calculate time of next run.
                    task.nextRun = data.getPlayTime() + task.period;
                }
            }

            // If there are no more tasks for this player, remove his entry in map.
            if (entry.getValue().size() == 0) {
                itr.remove();
            }
        }
        repeatingTasksTimings.stopTiming();
    }

    /**
     * Schedules one time delayed task.
     *
     * @param player player that this task is connected to
     * @param task   task to schedule
     * @param delay  delay of the execution
     */
    public void scheduleDelayed(Player player, PlaytimeDelayedTask task, Time delay) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(task);
        Preconditions.checkNotNull(delay);

        task.nextRun = Data.of(player).getPlayTime() + delay.toLongTicks();

        addDelayed(player, task);
    }

    private void addDelayed(Player player, PlaytimeDelayedTask task) {
        if (this.delayedTaskMap.containsKey(player.getUniqueId())) {
            this.delayedTaskMap.get(player.getUniqueId()).add(task);
        } else {
            ArrayList<PlaytimeDelayedTask> taskList = new ArrayList<>();
            taskList.add(task);
            this.delayedTaskMap.put(player.getUniqueId(), taskList);
        }
    }

    /**
     * Schedules repeating task with or without delay of first run.
     *
     * @param player player that this task is connected to
     * @param task   task to schedule
     * @param delay  delay of the execution or null
     * @param period period of execution
     */
    public void scheduleRepeating(Player player, PlaytimeRepeatingTask task, @Nullable Time delay, Time period) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(task);
        Preconditions.checkNotNull(period);

        if (delay == null || delay.toLongTicks() == 0) {
            task.period = period.toLongTicks();
            // Execute next iteration.
            task.nextRun = Data.of(player).getPlayTime();

            addRepeating(player, task);
        } else {
            // Schedule delayed task to schedule repeating.
            this.scheduleDelayed(player, new PlaytimeDelayedTask() {
                @Override
                public void run() {
                    // We already waited the delay, so we are passing null as delay.
                    task.schedule(player, null, period);
                }
            }, delay);
        }

    }

    private void addRepeating(Player player, PlaytimeRepeatingTask task) {
        if (this.repeatingTaskMap.containsKey(player.getUniqueId())) {
            this.repeatingTaskMap.get(player.getUniqueId()).add(task);
        } else {
            ArrayList<PlaytimeRepeatingTask> taskList = new ArrayList<>();
            taskList.add(task);
            this.repeatingTaskMap.put(player.getUniqueId(), taskList);
        }
    }

}
