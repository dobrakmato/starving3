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
package eu.matejkormuth.starving.items.listeners;

import com.darkblade12.particleeffect.ParticleEffect;
import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.nms.NMS;
import eu.matejkormuth.starving.nms.NMSModule;
import eu.matejkormuth.starving.persistence.AbstractPersistable;
import eu.matejkormuth.starving.persistence.Persist;
import eu.matejkormuth.starving.sounds.Sound;
import eu.matejkormuth.starving.sounds.SoundsModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class ProjectileHitListener extends AbstractPersistable implements Listener {
    @Persist(key = "PARTICLE_AMOUNT")
    public static int PARTICLE_AMOUNT = 25;

    private Random random = new Random();
    private Sound soundSmokeShellBurn;
    private Sound soundMolotovBreak;
    private NMS nms;

    public ProjectileHitListener(SoundsModule soundsModule, NMSModule nmsModule) {
        soundSmokeShellBurn = soundsModule.createSound("pyrotechnics.smokeshell.burn");
        soundMolotovBreak = soundsModule.createSound("explosives.molotov.break");
        nms = nmsModule.getNms();
    }

    @EventHandler
    private void onProjectileHit(final ProjectileHitEvent event) {
        // Here is a switch that calls specific method to handle this event.
        if (event.getEntity() instanceof Snowball) {
            onSnowballHit(event);
        } else if (event.getEntity() instanceof ThrownPotion) {
            if (event.getEntity().hasMetadata("isMolotov")) {
                onMolotovHit(event);
            } else if (event.getEntity().hasMetadata("isSmokeShell")) {
                onSmokeShellHit(event);
            }
        }
    }

    private void onSmokeShellHit(ProjectileHitEvent event) {
        final Location location = event.getEntity().getLocation();

        // Play sound.
        event.getEntity().getNearbyEntities(32, 32, 32)
                .stream()
                .filter(e -> e.getType() == EntityType.PLAYER)
                .forEach(e -> soundSmokeShellBurn.play((Player) e, event.getEntity().getLocation(), 1.5f, 1f));

        RepeatingTask spawnParticles = new RepeatingTask() {
            private int count = 0;

            @Override
            public void run() {
                if (this.count >= 20 * 60) {
                    this.cancel();
                }
                this.spawn();
                this.count++;
            }

            private void spawn() {
                ParticleEffect.CLOUD.display(3, 2, 3, 0, 80, location, Double.MAX_VALUE);
            }
        };

        spawnParticles.schedule(Time.ofTicks(1).toLongTicks());
    }

    private void onMolotovHit(ProjectileHitEvent event) {
        // Get block where collision happened.
        Block b = event.getEntity().getLocation().getBlock();

        // Set the first fire to illuminate "explosion".
        if (b.getType() != Material.AIR) {
            b = b.getRelative(BlockFace.UP);
        }
        if (b.getType() == Material.AIR) {
            b.setType(Material.FIRE);
        }

        // Play sound.
        event.getEntity().getNearbyEntities(32, 32, 32)
                .stream()
                .filter(e -> e.getType() == EntityType.PLAYER)
                .forEach(e -> soundMolotovBreak.play((Player) e, event.getEntity().getLocation(), 1.5f, 1f));

        // Spawn fires from the center to sides
        for (int i = 0; i < 30; i++) {
            FallingBlock block = event.getEntity().getWorld().spawnFallingBlock(
                    event.getEntity().getLocation().add(0, 0.5f, 0), Material.FIRE, (byte) 0);
            block.setVelocity(new Vector(random.nextFloat() / 2 - 0.25f, 0.2f,
                    random.nextFloat() / 2 - 0.25f));
        }
    }

    private void onSnowballHit(ProjectileHitEvent event) {
        // Get block, where collision happened.
        Block b = event.getEntity().getLocation().add(event.getEntity().getVelocity().normalize()).getBlock();

        // Remove snowball
        event.getEntity().remove();

        // Display block crack particles.
        if (b.getType() != Material.AIR) {
            // Particle.
            ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(b.getType(), b.getData()),
                    0.5f, 0.5f, 0.5f, 1, PARTICLE_AMOUNT, event.getEntity().getLocation(), Double.MAX_VALUE);
            // Break block.
            nms.displayMaterialBreak(b.getLocation());
        }
    }
}
