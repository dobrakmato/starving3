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
import eu.matejkormuth.bukkit.ItemDrops;
import eu.matejkormuth.bukkit.Items;
import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.Mappings;
import eu.matejkormuth.starving.items.base.Item;
import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.sounds.Sound;
import eu.matejkormuth.starving.sounds.Sounds;
import eu.matejkormuth.starving.sounds.SoundsModule;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class Petard extends Item {

    private Sound soundBurn;
    private Sound soundExplode;

    public Petard(SoundsModule soundsModule) {
        super(Mappings.PETARD, "Petard");

        soundBurn = Sounds.PETARD_BURN;
        soundExplode = Sounds.PETARD_BURN;
    }

    @Override
    public InteractResult onInteract(Player player, Action action, Block clickedBlock, BlockFace clickedFace) {
        if (Actions.isRightClick(action)) {
            ItemStack is = Items.of(Mappings.PETARD.getMaterial());
            org.bukkit.entity.Item i = ItemDrops.drop(player.getEyeLocation(),
                    is);
            i.setPickupDelay(Time.ofSeconds(6).toTicks());
            i.setVelocity(player.getEyeLocation().getDirection());
            // Play sound.
            player.getNearbyEntities(32, 32, 32)
                    .stream()
                    .filter(e -> e.getType() == EntityType.PLAYER)
                    .forEach(e -> soundBurn.play((Player) e, player.getLocation(), 1.5f, 1f));

            soundBurn.play(player, player.getLocation(), 1.5f, 1f);

            new RepeatingTask() {
                private int ticks = 0;

                @Override
                public void run() {
                    if (ticks >= 20 * 5) {
                        this.cancel();
                        this.explode();
                    }
                    ParticleEffect.FLAME.display(0.1f, 0.1f, 0.1f, 0, 1,
                            i.getLocation().add(0, 0.3, 0), Double.MAX_VALUE);
                    ticks++;
                }

                private void explode() {
                    ParticleEffect.FLAME.display(0.1f, 0.1f, 0.1f, 0.5f, 100,
                            i.getLocation(), Double.MAX_VALUE);
                    // Play sound.
                    soundExplode.play(player, player.getLocation(), 1.5f, 1f);
                    player.getNearbyEntities(32, 32, 32)
                            .stream()
                            .filter(e -> e.getType() == EntityType.PLAYER)
                            .forEach(e -> soundExplode.play((Player) e, player.getLocation(), 1f, 1f));
                    i.remove();
                }
            }.schedule(1L);
        }

        return InteractResult.useOne();
    }
}
