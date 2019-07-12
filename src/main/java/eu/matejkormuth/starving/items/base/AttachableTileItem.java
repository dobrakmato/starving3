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
package eu.matejkormuth.starving.items.base;

import eu.matejkormuth.bukkit.Actions;
import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.Mapping;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AttachableTileItem extends Item {

    private final int MAX_ITEM_FRAME_COUNT = 60;
    private final int MAX_ITEM_FRAME_RADIUS = 20;

    private Material material;
    private byte data;
    private boolean randomizeRotation;

    public AttachableTileItem(Material material, int data, boolean randomizeRotation) {
        super(new Mapping(material, data), "Attachable Tile - " + material.name() + " D" + data);
        this.material = material;
        this.data = (byte) data;
        this.randomizeRotation = randomizeRotation;
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractResult onInteract(Player player, Action action, Block clickedBlock, BlockFace clickedFace) {
        if (Actions.isRightClick(action)) {
            if (clickedBlock == null || clickedFace == null) {
                player.sendMessage(ChatColor.RED + "You must click on block.");
                return InteractResult.useNone();
            }

            if (clickedFace == BlockFace.UP || clickedFace == BlockFace.DOWN) {
                player.sendMessage(ChatColor.RED + "Can't attach tile to top or bottom of block.");
                return InteractResult.useNone();
            }

            // Spawn ItemFrame.
            Location itemFrameLoc = clickedBlock.getRelative(clickedFace).getLocation();
            ItemFrame itemFrame = (ItemFrame) player.getWorld().spawnEntity(itemFrameLoc, EntityType.ITEM_FRAME);
            itemFrame.setItem(new ItemStack(material, 1, (short) 0, data));

            // If requested randomize rotation.
            rotateItemFrame(itemFrame);

            // Display warning message if there are many item frames around.
            displayWarning(player, itemFrame);
        } else {
            if (clickedBlock != null) {
                clickedBlock.breakNaturally(null);
            }
        }
        return InteractResult.useNone();
    }

    private void rotateItemFrame(ItemFrame itemFrame) {
        if (randomizeRotation) {
            switch ((int) (Math.random() * 4)) {
                case 0:
                    itemFrame.setRotation(Rotation.CLOCKWISE_45);
                    break;
                case 1:
                    itemFrame.setRotation(Rotation.CLOCKWISE_135);
                    break;
                case 2:
                    itemFrame.setRotation(Rotation.FLIPPED_45);
                    break;
                default:
                case 3:
                    itemFrame.setRotation(Rotation.COUNTER_CLOCKWISE_45);
                    break;
            }
        }
    }

    private void displayWarning(Player player, ItemFrame itemFrame) {
        long itemFrameCount = itemFrame
                .getNearbyEntities(MAX_ITEM_FRAME_RADIUS, MAX_ITEM_FRAME_RADIUS, MAX_ITEM_FRAME_RADIUS)
                .stream()
                .count();
        if (itemFrameCount > MAX_ITEM_FRAME_COUNT) {
            player.sendMessage(ChatColor.GOLD + "There are many item frames around you!" +
                    " The game may become laggy if you continue with placing more item frames!");
        }
    }

    @Override
    public ItemStack toItemStack() {
        return this.toItemStack(1);
    }

    @Override
    public ItemStack toItemStack(int amount) {
        ItemStack is = super.toItemStack(amount);
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(ChatColor.GREEN + "Tiled block: " + material.name(),
                randomizeRotation ? "Rotation randomized" : "Rotation not randomized"));
        is.setItemMeta(im);
        return is;
    }
}
