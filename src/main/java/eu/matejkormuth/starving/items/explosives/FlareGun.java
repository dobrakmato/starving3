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
import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.Mappings;
import eu.matejkormuth.starving.items.base.Item;
import eu.matejkormuth.starving.main.RepeatingTask;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class FlareGun extends Item {
    public FlareGun() {
        super(Mappings.FLAREGUN, "FlareGun");
    }

    @Override
    public InteractResult onInteract(Player player, Action action, Block clickedBlock, BlockFace clickedFace) {
        if (Actions.isRightClick(action)) {
            new FlareSimTask(player.getEyeLocation(), player.getEyeLocation().getDirection().multiply(1.2)).schedule(1L);
        }
        return InteractResult.useNone();
    }

    public class FlareSimTask extends RepeatingTask {
        private int lifeTime = 0;
        private boolean burning = false;

        private Location loc;
        private Vector vel;

        public FlareSimTask(Location spawn, Vector vel) {
            this.loc = spawn;
            this.vel = vel;
        }

        @Override
        public void run() {
            if (this.lifeTime >= 10 * 20) {
                this.cancel();
            }

            if (!burning && this.lifeTime >= 20 * 2) {
                burning = true;
            }

            boolean insideBlock = this.loc.getBlock().getType().isSolid();

            if (!burning && insideBlock) {
                burning = true;
            }

            if (!burning && this.loc.getBlockY() > 120) {
                burning = true;
            }

            if (burning && lifeTime % 3 == 0) {
                Firework f = (Firework) loc.getWorld().spawnEntity(loc,
                        EntityType.FIREWORK);

                FireworkMeta fm = f.getFireworkMeta();
                if (Math.random() > 0.9) {
                    fm.addEffect(FireworkEffect.builder().withColor(Color.RED).withFade(
                            Color.WHITE).build());
                } else {
                    fm.addEffect(FireworkEffect.builder().withColor(Color.RED).withFade(
                            Color.WHITE).with(
                            FireworkEffect.Type.BALL_LARGE).build());
                }
                f.setFireworkMeta(fm);
            } else {
                ParticleEffect.FLAME.display(0, 0, 0, 0, 1, this.loc,
                        Double.MAX_VALUE);
            }

            if (!insideBlock && this.loc.getY() < 256 + 64) {
                if (burning) {
                    this.loc.subtract(new Vector(0, 0.2f, 0));
                } else {
                    this.loc.add(vel);
                }
            }

            this.lifeTime++;
        }
    }
}
