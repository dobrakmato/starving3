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
package eu.matejkormuth.starving.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Represents repeating task that cloud be canceled.
 */
public abstract class RepeatingTask implements Runnable {

    // Reference to plugin.
    private static final Plugin plugin = Bukkit.getPluginManager().getPlugin("Starving");

    // Id of current task.
    private int id;

    /**
     * Creates new RepeatingTask from specified runnable or method reference.
     *
     * @param runnable runnable or method reference
     * @return repeating task instance
     */
    public static RepeatingTask of(Runnable runnable) {
        return new RepeatingTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    /**
     * Cancels further execution of this repeating task.
     *
     * @return instance of itself useful for method chaining
     */
    public RepeatingTask cancel() {
        Bukkit.getScheduler().cancelTask(this.id);
        return this;
    }

    /**
     * Schedules this task to execute each period.
     *
     * @param period execution period
     * @return instance of itself useful for method chaining
     */
    public RepeatingTask schedule(Time period) {
        schedule(0, period.toLongTicks());
        return this;
    }

    /**
     * Schedules this task to execute each period after specified delay.
     *
     * @param delay  delay
     * @param period execution period
     * @return instance of itself useful for method chaining
     */
    public RepeatingTask schedule(Time delay, Time period) {
        schedule(delay.toLongTicks(), period.toLongTicks());
        return this;
    }

    /**
     * Schedules this task to execute each period.
     *
     * @param period execution period in server ticks
     * @return instance of itself useful for method chaining
     */
    public RepeatingTask schedule(long period) {
        schedule(0, period);
        return this;
    }

    /**
     * Schedules this task to execute each period after specified delay.
     *
     * @param delay  delay in server ticks
     * @param period execution period in server ticks
     * @return instance of itself useful for method chaining
     */
    public RepeatingTask schedule(long delay, long period) {
        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, period);
        return this;
    }

    /**
     * Returns ID of this task if the task is scheduled. Returns zero, if the task was not scheduled and minus one,
     * if the task was scheduled and then canceled.
     *
     * @return id of this task
     */
    public int getId() {
        return id;
    }
}
