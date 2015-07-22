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
package eu.matejkormuth.starving.resourcepacks.commands;

import eu.matejkormuth.starving.main.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class RpCommandExecutor implements CommandExecutor {
    private static final String DEFAULT_RP_RESOLUTION = "_128";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                String type = args[0];

                String res = DEFAULT_RP_RESOLUTION;
                if (args.length == 2) {
                    res = "_" + Integer.valueOf(args[1]);
                }

                // TODO: Add resolutions.

                if (type.equalsIgnoreCase("players")) {
                    Data.of((Player) sender).setResourcePack("players");
                    ((Player) sender).setResourcePack("http://www.starving.eu/2/rp/latest.zip");
                    sender.sendMessage(ChatColor.GREEN + "Resource pack type set to 'players'!");
                } else if (type.equalsIgnoreCase("builders")) {
                    Data.of((Player) sender).setResourcePack("builders");
                    ((Player) sender).setResourcePack("http://www.starving.eu/2/rp/latest_builder.zip");
                    sender.sendMessage(ChatColor.GREEN + "Resource pack type set to 'builders'!");
                } else if (type.equalsIgnoreCase("empty")) {
                    Data.of((Player) sender).setResourcePack("empty");
                    ((Player) sender).setResourcePack("http://www.starving.eu/2/rp/empty.zip");
                    sender.sendMessage(ChatColor.GREEN + "Resource pack type set to 'empty'!");
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid resource pack type!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /rp <players/builders/empty>");
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                String c = args[0];
                if (c.equalsIgnoreCase("reloadall")) {
                    // Reload resource pack for all online players.
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (Data.of(p).getResourcePack().equals("builders")) {
                            p.setResourcePack("http://www.starving.eu/2/rp/latest_builder.zip");
                        } else if (Data.of(p).getResourcePack().equals("players")) {
                            p.setResourcePack("http://www.starving.eu/2/rp/latest.zip");
                        } else {
                            p.setResourcePack("http://www.starving.eu/2/rp/empty.zip");
                        }
                    }
                } else {
                    sender.sendMessage("Unsupported command!");
                }
            } else {
                sender.sendMessage("Invalid command!");
            }
        } else {

        }
        return true;
    }
}
