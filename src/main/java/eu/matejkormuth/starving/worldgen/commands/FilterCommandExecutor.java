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
package eu.matejkormuth.starving.worldgen.commands;

import eu.matejkormuth.starving.worldgen.WorldGenManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.matejkormuth.starving.worldgen.filters.base.Filter;

public class FilterCommandExecutor implements CommandExecutor {

    private WorldGenManager worldGenManager;

    public FilterCommandExecutor(WorldGenManager worldGenManager) {
        this.worldGenManager = worldGenManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.isOp() || sender.hasPermission("wg")) {
                if (args.length == 1) {
                    String filter = args[0];
                    Filter f = worldGenManager.getFilter(filter);
                    if (f != null) {
                        worldGenManager.getSession(
                                (Player) sender).setFilter(f);
                        sender.sendMessage(ChatColor.GREEN + "Filter set to: "
                                + filter + ".");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Filter '" + filter
                                + "' not found!");
                    }
                } else {
                    sender.sendMessage(ChatColor.YELLOW
                            + "Current filter: "
                            + worldGenManager.getSession(
                            (Player) sender).getFilter().getName());
                    sender.sendMessage(ChatColor.YELLOW
                            + "=== Available filters ===");
                    for (Filter f : worldGenManager.getFilters()) {
                        sender.sendMessage(ChatColor.GOLD + " " + f.getName());
                    }
                    sender.sendMessage(ChatColor.YELLOW
                            + "==========================");
                    sender.sendMessage(ChatColor.RED
                            + "Usage: /f <filter>");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Not enough permissions!");
            }
        } else {
            sender.sendMessage(ChatColor.RED
                    + "This reason can be only used by players!");
        }
        return true;
    }
}
