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

import eu.matejkormuth.starving.items.Category;
import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.Mapping;
import eu.matejkormuth.starving.items.Rarity;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class Item extends ItemBase {
    /**
     * By Minecraft :-).
     */
    private static final int DEFAULT_MAX_STACK_AMOUNT = 64;

    private Rarity rarity = Rarity.COMMON;
    private Category category = Category.UNCATEGORIZED;
    private int maxStackAmount = DEFAULT_MAX_STACK_AMOUNT;
    private String description = "default description";

    // private float resistanceChange = 0.0f; // damage resistance in percents
    // private float biteProbabiltyChange = 00.0f;

    public Item(Mapping mapping, String name) {
        super(mapping.getMaterial(), mapping.getData(), 1, name);
    }

    public InteractResult onInteract(Player player, Action action,
                                     Block clickedBlock, BlockFace clickedFace) {
        return InteractResult.useNone();
    }

    public void onInteractWith(Player player, Entity entity) {
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    protected void setCategory(Category category) {
        this.category = category;
    }

    protected void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public Category getCategory() {
        return this.category;
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    protected void setMaxStackAmount(int amount) {
        this.maxStackAmount = amount;
    }

    public int getMaxStackAmount() {
        return this.maxStackAmount;
    }

    @Override
    public ItemStack toItemStack() {
        return this.toItemStack(1);
    }

    @Override
    public ItemStack toItemStack(int amount) {
        ItemStack is = super.toItemStack(amount);

        // Append description.
        ItemMeta meta = is.getItemMeta();
        meta.setLore(Arrays.asList(WordUtils.wrap(this.description, 20, "\n" + ChatColor.DARK_PURPLE, false).split("\n")));
        is.setItemMeta(meta);
        return is;
    }
}
