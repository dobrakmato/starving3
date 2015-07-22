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
package eu.matejkormuth.starving.achievements;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Abstract class for simplified implementation of achievements.
 */
public abstract class Achievement implements Listener {
    private final String name;
    private final String description;
    private final int maxProgress;

    public Achievement(String name, String description) {
        this(name, description, 1);
    }

    public Achievement(String name, String description, int maxProgress) {
        this.name = name;
        this.description = description;
        this.maxProgress = maxProgress;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    /**
     * Completes the achievement. Has same effect as calling progress(player,
     * getMaxProgress()).
     *
     * @param player player
     */
    public void complete(Player player) {
        this.progress(player, this.maxProgress);
    }

    /**
     * Makes a progress in achievement for specified player by specified amount.
     *
     * @param player player
     * @param amount amount
     */
    public void progress(Player player, int amount) {
        // TODO: Get last current progress.
        int currentProgress = 0;
        int nextProgress = Math.min(currentProgress + amount, this.maxProgress);

        // TODO: Save next current progress.

        if (nextProgress == this.maxProgress) {
            // TODO: Make better message using on hover, etc...

            String str = ChatColor.GREEN + "Achievement " + ChatColor.YELLOW + this.name + ChatColor.GREEN
                    + " completed!";
            player.sendMessage(str);
            this.onCompleted(player);
        }
    }

    /**
     * Gives reward (if any) to player.
     *
     * @param player player to give reward to
     */
    private void onCompleted(Player player) {
        // To be implemented by subclasses...
    }
}
