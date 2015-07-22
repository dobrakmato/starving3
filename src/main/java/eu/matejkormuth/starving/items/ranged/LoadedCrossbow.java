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
package eu.matejkormuth.starving.items.ranged;

import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.ItemManager;
import eu.matejkormuth.starving.items.Mappings;
import eu.matejkormuth.starving.items.base.Item;
import eu.matejkormuth.starving.items.transformers.CrossbowTransformer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class LoadedCrossbow extends Item {
    private float projectileSpeed = 2f;
    private CrossbowTransformer crossbowTransformer;

    public LoadedCrossbow(ItemManager itemManager) {
        super(Mappings.CROSSBOWLOADED, "Crossbow (loaded)");
        this.crossbowTransformer = new CrossbowTransformer(itemManager);
    }

    @Override
    public InteractResult onInteract(Player player, Action action, Block clickedBlock, BlockFace clickedFace) {
        // Fire arrow.
        this.fire(player);
        // Transform.
        ItemStack unloaded = crossbowTransformer.toUnloaded();
        player.setItemInHand(unloaded);
        return InteractResult.transform();
    }

    private void fire(Player player) {
        // Compute values.
        Location projectileSpawn = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(2));
        Vector projectileVelocity = player.getEyeLocation().getDirection().multiply(this.projectileSpeed);
        // Create entity and set velocity.
        Arrow arrow = (Arrow) player.getWorld().spawnEntity(projectileSpawn, EntityType.ARROW);
        arrow.setVelocity(projectileVelocity);
        // Play sound.
        player.getWorld().playSound(projectileSpawn, Sound.IRONGOLEM_THROW, 2.5f, 1f);
    }

}
