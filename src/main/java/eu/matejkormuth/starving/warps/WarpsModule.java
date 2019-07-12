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
package eu.matejkormuth.starving.warps;

import eu.matejkormuth.bmboot.Dependency;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.commands.CommandFilters;
import eu.matejkormuth.starving.commands.CommandsModule;
import eu.matejkormuth.starving.filestorage.FileStorageModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WarpsModule extends Module {

    private static final Logger log = LoggerFactory.getLogger(WarpsModule.class);

    @Dependency
    private FileStorageModule fileStorageModule;
    @Dependency
    private CommandsModule commandsModule;

    // Path to warps folder.
    private Path basePath;

    @Override
    public void onEnable() {
        // Initialize this.
        basePath = fileStorageModule.getPath("warps");

        // Register commands.
        commandsModule.command("warp", this::warpTo);
        commandsModule.command("setwarp", CommandFilters.opOnly(this::setWarp));
        commandsModule.command("warps", this::listWarps);
    }

    private boolean listWarps(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Warps: ");
        for(String warp : basePath.toFile().list()) {
            stringBuilder.append(warp.replace(".yml", ""));
            stringBuilder.append(", ");
        }
        sender.sendMessage(stringBuilder.toString());

        return true;
    }

    private boolean setWarp(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String warpName = args[0];
            if (warpExists(warpName)) {
                Warp warp = new Warp(warpName, ((Entity) commandSender).getLocation());
                saveWarp(warp);
                commandSender.sendMessage(ChatColor.GREEN + " Warp '" + warpName + "' has been overwritten!");
            } else {
                Warp warp = new Warp(warpName, ((Entity) commandSender).getLocation());
                saveWarp(warp);
                commandSender.sendMessage(ChatColor.GREEN + " Warp '" + warpName + "' has been set!");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "Usage: /setwarp <name>");
        }
        return true;
    }

    private void saveWarp(Warp warp) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("x", warp.getX());
        yamlConfiguration.set("y", warp.getY());
        yamlConfiguration.set("z", warp.getZ());
        yamlConfiguration.set("pitch", warp.getPitch());
        yamlConfiguration.set("yaw", warp.getYaw());
        yamlConfiguration.set("world", warp.getWorld());
        try {
            yamlConfiguration.save(basePath.resolve(warp.getName() + ".yml").toFile());
        } catch (IOException e) {
            log.error("Can't save warp {} because {}!", warp, e);
        }
    }

    private boolean warpTo(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String warpName = args[0];
            if (warpExists(warpName)) {
                // Load warp.
                Warp warp = loadWarp(warpName);
                // Teleport.
                commandSender.sendMessage(ChatColor.GREEN + " Teleporting to warp '" + warpName + "' !");
                warp.teleport((Entity) commandSender);
            } else {
                commandSender.sendMessage(ChatColor.RED + " Warp '" + warpName + "' does not exists!");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "Usage: /warp <name>");
        }
        return true;
    }

    private Warp loadWarp(String warpName) {
        YamlConfiguration yamlConfiguration = YamlConfiguration
                .loadConfiguration(basePath.resolve(warpName + ".yml").toFile());
        Warp warp = new Warp();
        warp.setX(yamlConfiguration.getDouble("x"));
        warp.setY(yamlConfiguration.getDouble("y"));
        warp.setZ(yamlConfiguration.getDouble("z"));
        warp.setPitch((float) yamlConfiguration.getDouble("pitch"));
        warp.setYaw((float) yamlConfiguration.getDouble("yaw"));
        warp.setWorld(yamlConfiguration.getString("world"));
        return warp;
    }

    private boolean warpExists(String warpName) {
        return Files.exists(basePath.resolve(warpName + ".yml"));
    }

    @Override
    public void onDisable() {

    }
}
