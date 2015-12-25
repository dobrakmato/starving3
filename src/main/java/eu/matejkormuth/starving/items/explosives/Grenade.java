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

import eu.matejkormuth.bukkit.Actions;
import eu.matejkormuth.bukkit.ItemDrops;
import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.Mappings;
import eu.matejkormuth.starving.items.Rarity;
import eu.matejkormuth.starving.items.base.Item;
import eu.matejkormuth.starving.main.DelayedTask;
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

public class Grenade extends Item {

    private Sound throwSound;
    private Sound explodeSound;

    public Grenade(SoundsModule soundsModule) {
        super(Mappings.GRENADE, "Grenade");
        this.setMaxStackAmount(8);
        this.setRarity(Rarity.RARE);
        // TODO: Category.

        throwSound = Sounds.GRENADE_THROW;
        explodeSound = Sounds.GRENADE_EXPLODE;
    }

    @Override
    public InteractResult onInteract(Player player, Action action, Block clickedBlock, BlockFace clickedFace) {
        if (Actions.isRightClick(action)) {
            // Throw grenade.

            // Play sound.
            throwSound.play(player, player.getLocation(), 1.5f, 1f);
            player.getNearbyEntities(32, 32, 32)
                    .stream()
                    .filter(e -> e.getType() == EntityType.PLAYER)
                    .forEach(e -> throwSound.play((Player) e, player.getLocation(), 1.5f, 1f));

            ItemStack itemStack = new ItemStack(Mappings.GRENADE.getMaterial(), 1);
            org.bukkit.entity.Item drop = ItemDrops.dropNotMergable(
                    player.getLocation().add(player.getEyeLocation().getDirection()), itemStack,
                    Time.ofSeconds(6).toTicks());
            drop.setVelocity(player.getEyeLocation().getDirection().multiply(1.5f));

            // Schedule explosion.
            DelayedTask.of(() -> {
                // Play sound.
                explodeSound.play(player, player.getLocation(), 1.5f, 1f);
                player.getNearbyEntities(32, 32, 32)
                        .stream()
                        .filter(e -> e.getType() == EntityType.PLAYER)
                        .forEach(e -> explodeSound.play((Player) e, player.getLocation(), 1.5f, 1f));
                drop.remove();
            }).schedule(Time.ofSeconds(5));
        }
        return InteractResult.useOne();
    }
}
