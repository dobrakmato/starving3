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
package eu.matejkormuth.starving.items.itemmeta;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ItemMetaWrapper implements LoreAccessor {
    protected ItemStack itemStack;
    protected ItemMeta meta;
    protected KeyValueHandler valueHandler;

    public ItemMetaWrapper(ItemStack stack) {
        this.itemStack = stack;
        this.meta = stack.getItemMeta();
        this.valueHandler = new StdLoreHandler(this);
    }

    public ItemMetaWrapper(ItemStack stack, KeyValueHandler valueHandler) {
        this.meta = stack.getItemMeta();
        this.valueHandler = valueHandler;
    }

    @Override
    public List<String> getLore() {
        if (this.meta.getLore() == null) {
            return new ArrayList<>();
        }

        return this.meta.getLore();
    }

    @Override
    public void setLore(List<String> lore) {
        this.meta.setLore(lore);
    }

    public void apply(ItemStack is) {
        is.setItemMeta(this.meta);
    }

    public ItemStack apply() {
        this.itemStack.setItemMeta(this.meta);
        return this.itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
