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
package eu.matejkormuth.starving.items.consumables;

import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.Mapping;
import eu.matejkormuth.starving.items.base.ConsumableItem;
import eu.matejkormuth.starving.main.DelayedTask;
import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.main.Time;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

public class MagicMushroom extends ConsumableItem {
    public MagicMushroom() {
        super(0, 0, new Mapping(Material.RED_MUSHROOM), "Magic mushroom");
    }

    @Override
    protected void onConsume0(Player player) {
    }

    @Override
    public InteractResult onInteract(final Player player, Action action, Block clickedBlock, BlockFace clickedFace) {

        // Simulate trip.
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 30, 2));

        // Find teleport location.
        int x = player.getLocation().getBlockX() + RandomUtils.nextInt(200) - 100;
        int z = player.getLocation().getBlockZ() + RandomUtils.nextInt(200) - 100;
        int y = player.getWorld().getHighestBlockYAt(x, z);
        final Location targetLocation = new Location(player.getWorld(), x, y, z);

        // Play weird sounds.
        final RepeatingTask wsrId = RepeatingTask.of(new WeirdSoundsRunnable(player, targetLocation))
                .schedule(20L, 8L);

        // Schedule blindness effect.
        DelayedTask.of(() -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 3));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 7, 3));
        }).schedule(Time.ofSeconds(15));

        // Schedule teleport.
        DelayedTask.of(() -> {
            targetLocation.setPitch(player.getLocation().getPitch());
            targetLocation.setYaw(player.getLocation().getYaw());
            player.teleport(targetLocation);
            // Cancel weird sounds.
            DelayedTask.of(wsrId::cancel).schedule(Time.ofSeconds(5));
        }).schedule(Time.ofSeconds(20));

        return InteractResult.useOne();
    }

    private static class WeirdSoundsRunnable implements Runnable {
        private static final Random random = new Random();
        private static final Sound[] sounds = Sound.values();

        private final Player player;
        private int count = 0;
        private Location tl;

        public WeirdSoundsRunnable(Player player, Location targetLoc) {
            this.player = player;
            this.tl = targetLoc;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void run() {
            this.player.playSound(
                    player.getLocation().add(Vector.getRandom().multiply(2)),
                    randomSound(), 1F, (float) (1F + Math.random()));

            if ((this.count % 3) == 0) {
                this.player.playSound(player.getLocation(),
                        Sound.PORTAL_TRAVEL, 1, 1F);
                this.player.playSound(tl, Sound.PORTAL_TRAVEL, 1, 1F);
            }

            // Also apply weird blindness
            if (this.count < 7) {
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.BLINDNESS, 20, 0));
            }

            this.player.playEffect(player.getLocation(), Effect.VOID_FOG, 100);

            if (this.count > 10 && this.count < 28) {
                DelayedTask.of(() -> player.playSound(
                        player.getLocation().add(Vector.getRandom().multiply(2)), randomSound(), 1F,
                        (float) (1F + Math.random() * 0.5))).schedule(5L);
            }

            if (this.count > 15 && this.count < 23) {
                DelayedTask.of(() -> player.playSound(
                        player.getLocation().add(
                                Vector.getRandom().multiply(2)),
                        randomSound(), 1F,
                        (float) (1F + Math.random() * 0.5))).schedule(3L);

            }
            this.count++;
        }

        private Sound randomSound() {
            return sounds[random.nextInt(sounds.length)];
        }
    }
}
