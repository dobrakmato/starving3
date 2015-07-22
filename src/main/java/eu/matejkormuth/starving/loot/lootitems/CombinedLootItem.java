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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

/**
 * Combines more LootItem-s to one. Calling will choose one random child
 * LootItem to provide item stack.
 */
public class CombinedLootItem implements LootItem {

    private static final Random random = new Random();
    private List<LootItem> lootItems;

    public CombinedLootItem() {
        this.lootItems = new ArrayList<LootItem>();
    }

    public int size() {
        return lootItems.size();
    }

    public Iterator<LootItem> iterator() {
        return lootItems.iterator();
    }

    public boolean add(LootItem e) {
        return lootItems.add(e);
    }

    public boolean remove(Object o) {
        return lootItems.remove(o);
    }

    public boolean addAll(Collection<? extends LootItem> c) {
        return lootItems.addAll(c);
    }

    public void clear() {
        lootItems.clear();
    }

    @Override
    public ItemStack getItemStack() {
        return lootItems.get(random.nextInt(lootItems.size())).getItemStack();
    }

}
