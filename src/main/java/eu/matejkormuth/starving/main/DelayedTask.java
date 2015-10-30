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

import eu.matejkormuth.bmboot.facades.Container;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Represents work that should be executed with delay.
 */
public abstract class DelayedTask implements Runnable {

    // Reference to plugin.
    private static final Plugin plugin = Container.get(Plugin.class);

    /**
     * Constructs DelayedTask of specified runnable / method reference.
     *
     * @param runnable runnable or method refernce
     * @return created delayed task
     */
    public static DelayedTask of(Runnable runnable) {
        return new DelayedTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    /**
     * Schedules this task to execute after specified delay.
     *
     * @param delay delay
     * @return instance of itself useful for method chaining
     */
    public DelayedTask schedule(Time delay) {
        return schedule(delay.toLongTicks());
    }

    /**
     * Schedules this task to execute after specified delay.
     *
     * @param delay delay in server ticks
     * @return instance of itself useful for method chaining
     */
    public DelayedTask schedule(long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, delay);
        return this;
    }
}
