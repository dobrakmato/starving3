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

import com.darkblade12.particleeffect.ParticleEffect;
import eu.matejkormuth.bmboot.Dependency;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.bukkit.Worlds;
import eu.matejkormuth.starving.commands.CommandFilters;
import eu.matejkormuth.starving.commands.CommandsModule;
import eu.matejkormuth.starving.filestorage.FileStorageModule;
import eu.matejkormuth.starving.main.commands.RenderDistanceExecutor;
import eu.matejkormuth.starving.main.commands.SpeedCommandExecutor;
import eu.matejkormuth.starving.main.commands.TpToCommandExecutor;
import eu.matejkormuth.starving.main.listeners.BlockFadeListener;
import eu.matejkormuth.starving.main.listeners.MobDropsListener;
import eu.matejkormuth.starving.main.listeners.PlayerDeathListener;
import eu.matejkormuth.starving.main.listeners.PlayerJoinLeaveListener;
import eu.matejkormuth.starving.main.playtime.PlaytimeScheduler;
import eu.matejkormuth.starving.main.tasks.PlaytimeIncrementTask;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.v1_8_R3.FileIOThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHashSet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;

@Slf4j
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
        listener(new Listener() {
            @EventHandler
            private void on(final EntityDamageEvent event) {
                if (event.getEntity() instanceof Player) {
                    if (event.getEntity().getLocation().getBlock().getType() == Material.BED_BLOCK) {
                        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        });

        CraftBlock

        RepeatingTask.of(() -> {
            Block b;
            for (Player player : Bukkit.getOnlinePlayers()) {

                b = player.getLocation().getBlock();
                if (b != null && b.getType() == Material.BED_BLOCK) {
                    if (player.getVelocity().getY() < 0.1) {
                        //Bukkit.broadcastMessage(player.getName() + " jumped!");
                        Vector vec = player.getVelocity();
                        vec.setY(vec.getY() * -1.2f);
                        player.setVelocity(vec);
                    } else {
                        //Bukkit.broadcastMessage(player.getName() + "Can't jump right now!");
                        continue;
                    }
                }
            }
        }).name("Jumping Beds").schedule(0, 1);

        //quick&dirty
        listener(new Listener() {
            @EventHandler
            private void on(final PlayerInteractAtEntityEvent event) {
                if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD) {
                    event.getPlayer().sendMessage(ChatColor.GOLD + "Entity: " + event.getRightClicked().getClass().getSimpleName() + "; ID: " + event.getRightClicked().getEntityId());
                }
            }
        });

        // ItemFrame locator
        RepeatingTask.of(() -> {
            for (World w : Worlds.all()) {
                for (Entity e : w.getEntitiesByClass(ItemFrame.class)) {
                    if (((ItemFrame) e).getItem() == null || ((ItemFrame) e).getItem().getType() == Material.AIR) {
                        ParticleEffect.REDSTONE.display(0.1f, 0.1f, 0.1f, 0, 3, e.getLocation(), Double.MAX_VALUE);
                    }
                }
            }
        }).schedule(Time.ofSeconds(10), Time.ofTicks(5L));

        commandsModule.command("speed", CommandFilters.opOnly(new SpeedCommandExecutor()));
        commandsModule.command("tpto", CommandFilters.opOnly(new TpToCommandExecutor()));
        commandsModule.command("renderdistance", CommandFilters.opOnly(new RenderDistanceExecutor()));
        commandsModule.command("debugblock", (sender, command, label, args) -> {
            Player player = (Player) sender;
            int blockId = Integer.parseInt(args[0]);
            byte dataId = 0;
            for (int x = 0; x < 4; x++) {
                for (int z = 0; z < 4; z++) {
                    player.getLocation().getBlock().getRelative(x * 2, 0, z * 2).setTypeIdAndData(blockId, dataId++, false);
                }
            }
            return true;
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
