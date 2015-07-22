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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class Crossbow extends Item {

    private CrossbowTransformer crossbowTransformer;

    public Crossbow(ItemManager itemManager) {
        super(Mappings.CORSSBOWUNLOADED, "Crossbow");
        this.crossbowTransformer = new CrossbowTransformer(itemManager);
    }

    @Override
    public InteractResult onInteract(Player player, Action action, Block clickedBlock, BlockFace clickedFace) {
        // Fire only when player has arrows.
        if (player.getInventory().contains(Material.ARROW)) {
            player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.5f);
            // Remove one arrow.
            player.getInventory().remove(new ItemStack(Material.ARROW, 1));

            ItemStack loaded = crossbowTransformer.toLoaded();
            player.setItemInHand(loaded);
        } else {
            // Player has no arrows.
            player.sendMessage(ChatColor.RED + "You don't have any arrows!");
        }

        return InteractResult.transform();
    }

}
