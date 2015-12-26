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
package eu.matejkormuth.starving.physical.listeners;

import com.darkblade12.particleeffect.ParticleEffect;
import eu.matejkormuth.primitives.MutableInt;
import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.main.Time;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.WeakHashMap;

public class SprintListener implements Listener {

    private final Map<Player, Boolean> wasSprinting = new WeakHashMap<>();
    private final Map<Player, MutableInt> sprinting = new WeakHashMap<>();
    private final Map<Player, Vector> lastVelocities = new WeakHashMap<>();

    private final RepeatingTask runningCheck;

    public SprintListener() {
        this.runningCheck = new RepeatingTask() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (wasSprinting.getOrDefault(player, false)) {
                        if (sprinting.containsKey(player)) {
                            sprinting.get(player).increment();
                        } else {
                            sprinting.put(player, new MutableInt(1));
                        }
                    }
                    lastVelocities.put(player, player.getVelocity());
                }
            }
        };
        this.runningCheck.schedule(Time.ofTicks(1));
    }

    @EventHandler
    private void onSprintingToggle(final PlayerToggleSprintEvent event) {
        if (wasSprinting.getOrDefault(event.getPlayer(), false)) {
            if (!event.isSprinting()) {
                this.onStoppedSprinting(event);
                wasSprinting.put(event.getPlayer(), false);
            }
        } else {
            if (event.isSprinting()) {
                this.onStartedSprinting(event);
                wasSprinting.put(event.getPlayer(), true);
            }
        }
    }

    @EventHandler
    private void onPlayerMove(final PlayerMoveEvent event) {
        if (!event.getPlayer().isOnGround()) {
            double dist = event.getFrom().distance(event.getTo());
            //event.getPlayer().sendMessage("d:" + dist);
            Vector dir = event.getTo().clone().subtract(event.getFrom().clone()).toVector();

            if (dir.lengthSquared() < 1) {
                return;
            }

            Location l = event.getFrom().clone();
            Block b;
            for (int i = 0; i < 4; i++) {
                l.add(dir);
                b = l.getBlock();
                if (b.getType() == Material.THIN_GLASS || b.getType() == Material.GLASS || b.getType() == Material.STAINED_GLASS || b.getType() == Material.STAINED_GLASS_PANE) {
                    event.getTo().getWorld().playSound(event.getTo(), Sound.GLASS, 1, 1);
                    recurBreakGlass(b, 0);
                    return;
                }
            }

        }
    }

    private void recurBreakGlass(Block b, int i) {
        if (i > 1 && Math.random() > 0.75f) {
            return;
        }

        if (b.getType() == Material.THIN_GLASS || b.getType() == Material.GLASS || b.getType() == Material.STAINED_GLASS || b.getType() == Material.STAINED_GLASS_PANE) {

            ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(b.getType(), b.getData()), 1f, 1f, 1f, 1f, 20, b.getLocation(), Double.MAX_VALUE);
            // ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.THIN_GLASS, b.getData()), 1, 1, 1, 20, 1, b.getLocation(), Double.MAX_VALUE);

            b.setType(Material.AIR);

            recurBreakGlass(b.getRelative(BlockFace.DOWN), i + 1);
            recurBreakGlass(b.getRelative(BlockFace.UP), i + 1);
            recurBreakGlass(b.getRelative(BlockFace.EAST), i + 1);
            recurBreakGlass(b.getRelative(BlockFace.SOUTH), i + 1);
            recurBreakGlass(b.getRelative(BlockFace.WEST), i + 1);
            recurBreakGlass(b.getRelative(BlockFace.NORTH), i + 1);
        }
    }

    private void onStartedSprinting(PlayerToggleSprintEvent event) {

    }

    private void onStoppedSprinting(PlayerToggleSprintEvent event) {
        //Bukkit.broadcastMessage(ChatColor.BLUE + "Player " + event.getPlayer().getName() + " was sprinting for " + sprinting.get(event.getPlayer()).get() + " ticks.");
        sprinting.remove(event.getPlayer());
        //event.getPlayer().sendMessage(event.getPlayer().getVelocity().toString());
        //event.getPlayer().sendMessage(lastVelocities.getOrDefault(event.getPlayer(), new Vector(-1, -1, -1)).toString());
    }
}
