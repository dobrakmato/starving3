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
package eu.matejkormuth.starving.time.listeners;

import eu.matejkormuth.starving.time.tasks.TimeCorrectionTask;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class TimeSetCommandListener implements Listener {

    private TimeCorrectionTask timeCorrectionTask;

    public TimeSetCommandListener(TimeCorrectionTask timeCorrectionTask) {
        this.timeCorrectionTask = timeCorrectionTask;
    }

    @EventHandler
    private void onCommamdPreprocess(final PlayerCommandPreprocessEvent event) {
        // Command for settings time.
        if (event.getMessage().contains("time set")) {
            String[] parts = event.getMessage().split(" ");

            // Fix for issue #66.
            int time;
            if(parts[2].equalsIgnoreCase("day")) {
                time = 0;
            } else if(parts[2].equalsIgnoreCase("night")) {
                time = 14000;
            } else {
                time = Integer.valueOf(parts[2]);
            }

            timeCorrectionTask.vanillaSetMoveTime(time);
            event.getPlayer().sendMessage(ChatColor.GREEN + "Time set!");
        }
    }
}
