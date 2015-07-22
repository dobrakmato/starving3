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
package eu.matejkormuth.starving.loot.lootitems;

import eu.matejkormuth.starving.items.Category;
import eu.matejkormuth.starving.items.ItemManager;
import eu.matejkormuth.starving.items.Rarity;
import eu.matejkormuth.starving.items.base.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents random loot item that has at least category or rarity specified.
 * Returned item stack it random.
 */
public class CategoryLootItem implements LootItem {

    private static final Random random = new Random();
    private final ItemManager itemManager;
    private Category category;
    private Rarity rarity;

    public CategoryLootItem(ItemManager itemManager, Category category) {
        this(itemManager, category, null);
    }

    public CategoryLootItem(ItemManager itemManager, Rarity rarity) {
        this(itemManager, null, rarity);
    }

    public CategoryLootItem(ItemManager itemManager, Category category, Rarity rarity) {
        this.itemManager = itemManager;
        this.category = category;
        this.rarity = rarity;
    }

    @Override
    public ItemStack getItemStack() {
        if (category == null) {
            return findRandomByRarity();
        }

        if (rarity == null) {
            return findRandomByCategory();
        }

        return findRandomByCategoryAndRarity();
    }

    private ItemStack findRandomByCategoryAndRarity() {
        return find(this.category, this.rarity);
    }

    private ItemStack findRandomByCategory() {
        Rarity randomRarity = randomRarity();
        return find(this.category, randomRarity);
    }

    private Rarity randomRarity() {
        return Rarity.values()[random.nextInt(Rarity.values().length)];
    }

    private ItemStack findRandomByRarity() {
        Category randomCategory = randomCategory();
        return find(randomCategory, this.rarity);
    }

    private Category randomCategory() {
        return Category.values()[random.nextInt(Category.values().length)];
    }

    private ItemStack find(Category category, Rarity rarity) {
        List<Item> itemsMeetingCriteria = itemManager.getItems()
                .stream()
                .filter(item -> item.getCategory() == category)
                .filter(item -> item.getRarity() == rarity)
                .collect(Collectors.toCollection(ArrayList::new));

        return itemsMeetingCriteria.get(
                random.nextInt(itemsMeetingCriteria.size())).toItemStack();
    }

}
