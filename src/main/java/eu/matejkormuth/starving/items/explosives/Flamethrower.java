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
package eu.matejkormuth.starving.items.explosives;

import com.darkblade12.particleeffect.ParticleEffect;
import eu.matejkormuth.bukkit.Actions;
import eu.matejkormuth.bukkit.Blocks;
import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.Mappings;
import eu.matejkormuth.starving.items.base.Item;
import eu.matejkormuth.starving.main.DelayedTask;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.sounds.Sounds;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

import java.util.List;

public class Flamethrower extends Item {
    public Flamethrower() {
        super(Mappings.FLAMETHROWER, "Flamethrower");
    }

    @Override
    public InteractResult onInteract(Player player, Action action, Block clickedBlock, BlockFace clickedFace) {
        if (Actions.isRightClick(action)) {
            //this.emitFire(player);
            this.emitFire2(player);
            this.emitSound(player);
        }

        return InteractResult.useNone();
    }

    private void emitFire2(Player player) {
        ParticleEffect effect = ParticleEffect.CRIT;

        Vector fix = new Vector(0.5, 0.5, 0.5);

        Vector direction = player.getEyeLocation().getDirection().multiply(1.25D);
        Location spawn = player.getLocation().add(direction.clone()).add(0, 1.30D, 0);

        Runnable work = () -> {
            for (int i = 0; i < 35; i++) {
                Vector direction2 = player.getEyeLocation().getDirection().multiply(1.25D);
                Vector random = Vector.getRandom().subtract(fix).multiply(0.25f);
                effect.display(direction2.clone().add(random), (float) (1 + Math.random() * 2), spawn, Double.MAX_VALUE);
            }
        };

        work.run();
        DelayedTask.of(work)
                .schedule(1L)
                .schedule(2L)
                .schedule(3L)
                .schedule(4L)
                .schedule(5L)
                .schedule(6L);
    }

    private void emitSound(Player player) {
        Sounds.FLAMETHROWER_LOOP.play(player.getEyeLocation());
    }

    private void emitFire(Player player) {
        Location loc = player.getEyeLocation();
        Vector dir = loc.getDirection().normalize().multiply(0.5f);
        loc.add(loc.getDirection());

        final float[] a = {0.1f};

        Runnable emit = () -> {

            // Fizz on water.
            if (Blocks.isWater(loc.getBlock())) {
                loc.getWorld().playSound(loc, Sound.FIZZ, 1, 1);
            }

            // Set entities on fire.
            Vector x = loc.toVector();
            Vector dir2 = player.getEyeLocation().getDirection().normalize();
            float h = 10f; // 10 blocks
            float r = 4f; // 4 blocks

            List<Entity> possibleTargets = player.getNearbyEntities(10, 10, 10);

            possibleTargets
                    .stream()
                    .filter(e -> e instanceof LivingEntity)
                    .filter(e -> {
                        Vector p = e.getLocation().add(0.5, 1, 0.5).toVector();
                        float cone_dist = (float) p.clone().subtract(x).dot(dir2);
                        float cone_radius = (cone_dist / h) * r;
                        float orth_distance = (float) p.subtract(x).subtract(dir2.clone().multiply(cone_dist)).length();
                        return orth_distance < cone_radius;
                    })
                    .forEach(e -> {
                        e.setFireTicks(20 * 6);
                        ((LivingEntity) e).damage(5D);
                    });

            ParticleEffect.FLAME.display(a[0], a[0], a[0], 0f, 5, loc, Double.MAX_VALUE);
            loc.add(dir);
            ParticleEffect.FLAME.display(a[0], a[0], a[0], 0f, 5, loc, Double.MAX_VALUE);
            loc.add(dir);
            a[0] += 0.1f;
            if (a[0] > 0.7f) {
                ParticleEffect.SMOKE_LARGE.display(0.5f, 0.5f, 0.5f, 0f, 4, loc.clone().add(0, 1, 0), Double.MAX_VALUE);
            }
        };
        emit.run();
        DelayedTask.of(emit).schedule(Time.ofTicks(1));
        DelayedTask.of(emit).schedule(Time.ofTicks(2));
        DelayedTask.of(emit).schedule(Time.ofTicks(3));
        DelayedTask.of(emit).schedule(Time.ofTicks(4));
        DelayedTask.of(emit).schedule(Time.ofTicks(5));
        DelayedTask.of(emit).schedule(Time.ofTicks(6));
        DelayedTask.of(emit).schedule(Time.ofTicks(7));
        DelayedTask.of(emit).schedule(Time.ofTicks(8));
    }
}
