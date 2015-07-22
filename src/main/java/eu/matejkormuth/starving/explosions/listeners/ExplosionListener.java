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
package eu.matejkormuth.starving.explosions.listeners;

import com.darkblade12.particleeffect.ParticleEffect;
import eu.matejkormuth.starving.main.bukkitfixes.FlagMetadataValue;
import eu.matejkormuth.starving.rollbacker.RollbackEntry;
import eu.matejkormuth.starving.rollbacker.RollbackerModule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

public class ExplosionListener implements Listener {
    private static final Vector FALLING_BLOCK_ADD_VECTOR = new Vector(0, 2, 0);

    private final RollbackerModule rollbackerModule;

    public ExplosionListener(RollbackerModule rollbackerModule) {
        this.rollbackerModule = rollbackerModule;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    private void onExposion(final EntityExplodeEvent event) {
        // Display bunch of effects.
        ParticleEffect.SMOKE_LARGE.display(5, 2, 5, 0.5f, 700, event.getLocation(), Double.MAX_VALUE);
        ParticleEffect.SMOKE_LARGE.display(7, 2, 7, 0.01f, 1000, event.getLocation(), Double.MAX_VALUE);
        ParticleEffect.EXPLOSION_NORMAL.display(5, 2, 5, 0.5f, 200, event.getLocation(), Double.MAX_VALUE);
        ParticleEffect.SUSPENDED_DEPTH.display(10, 5, 10, 0, 800, event.getLocation(), Double.MAX_VALUE);
        // Make blocks fly to air.
        World w = event.getLocation().getWorld();
        for (Block b : event.blockList()) {

            // Add rollback record.
            rollbackerModule.add(new RollbackEntry(b.getState()));

            if (b.getType().isBlock()) {
                Material material = b.getType();

                if (b.getType() == Material.GRASS) {
                    material = Material.DIRT;
                }

                FallingBlock fb = w.spawnFallingBlock(b.getLocation().add(FALLING_BLOCK_ADD_VECTOR),
                        material, b.getData());

                fb.setMetadata("fromExplosion", new FlagMetadataValue());
                fb.setDropItem(false);
            }
            b.setType(Material.AIR);
        }
        // Clear damaged blocks.
        event.blockList().clear();
    }

    @EventHandler
    private void onFallingBlockLands(final EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK) {
            if(event.getEntity().hasMetadata("fromExplosion")) {
                this.rollbackerModule.add(new RollbackEntry(event.getBlock().getState()));
            }
        }
    }
}
