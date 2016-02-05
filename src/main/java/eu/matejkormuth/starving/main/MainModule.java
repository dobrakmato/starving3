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

import eu.matejkormuth.bmboot.Dependency;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.commands.CommandsModule;
import eu.matejkormuth.starving.filestorage.FileStorageModule;
import eu.matejkormuth.starving.main.listeners.BlockFadeListener;
import eu.matejkormuth.starving.main.listeners.MobDropsListener;
import eu.matejkormuth.starving.main.listeners.PlayerDeathListener;
import eu.matejkormuth.starving.main.listeners.PlayerJoinLeaveListener;
import eu.matejkormuth.starving.main.playtime.PlaytimeScheduler;
import eu.matejkormuth.starving.main.tasks.PlaytimeIncrementTask;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class MainModule extends Module {

    @Dependency
    private FileStorageModule fileStorageModule;

    @Dependency
    private CommandsModule commandsModule;

    private PlaytimeScheduler playtimeScheduler;

    @Override
    public void onEnable() {
        // Initialize Data class.
        Data.profilesRoot = fileStorageModule.getPath("players");
        // Initialize playtime scheduler.
        playtimeScheduler = new PlaytimeScheduler();
        // Initialize listeners.
        listener(new PlayerJoinLeaveListener());
        listener(new BlockFadeListener());
        listener(new MobDropsListener());
        listener(new PlayerDeathListener());

        //quick&dirty
        listener(new Listener() {
            @EventHandler
            private void on(final PlayerInteractAtEntityEvent event) {
                if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD) {
                    event.getPlayer().sendMessage(ChatColor.GOLD + "Entity: " + event.getRightClicked().getClass().getSimpleName() + "; ID: " + event.getRightClicked().getEntityId());
                }
            }
        });
        commandsModule.command("sit", (sender, command, label, args) -> {
            sender.sendMessage("/sit <sittedOn> <sitter>");
            int sittedId = Integer.parseInt(args[0]);
            int sitterId = Integer.parseInt(args[1]);

            Entity sitted = null;
            Entity sitter = null;

            for (Entity e : ((Player) sender).getWorld().getEntities()) {
                if (e.getEntityId() == sittedId) {
                    sitted = e;
                }
            }

            for (Entity e : ((Player) sender).getWorld().getEntities()) {
                if (e.getEntityId() == sitterId) {
                    sitter = e;
                }
            }

            if (sitted == null || sitter == null) {
                sender.sendMessage("Wrong entity id!");
                return false;
            }
            sitted.setPassenger(sitter);

            return true;
        });
        // Start repeating tasks.
        new PlaytimeIncrementTask().schedule(1L);
    }

    @Override
    public void onDisable() {

    }

    public PlaytimeScheduler getPlaytimeScheduler() {
        return playtimeScheduler;
    }
}
