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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ItemBase {

    protected final ItemStack itemStack;

    @SuppressWarnings("deprecation")
    public ItemBase(final Material material, final byte data, final int amount, final String name) {
        this.itemStack = new ItemStack(material, amount, (short) 0, data);
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + name);
        this.itemStack.setItemMeta(meta);
    }

    /**
     * Checks whether specified {@link ItemStack} matches this {@link ItemBase}.
     *
     * @param obj ItemStack to compare to this ItemBase
     * @return true if this ItemBase does have same material and name as
     * specified ItemStack, false otherwise
     */
    public boolean matches(final ItemStack obj) {
        if (obj == null) {
            return false;
        }

        if (this.itemStack == obj) {
            return true;
        }

        if (obj instanceof ItemStack) {
            return obj.getType().equals(this.itemStack.getType())
                    && obj.hasItemMeta()
                    && obj
                    .getItemMeta()
                    .getDisplayName()
                    .equals(this.itemStack.getItemMeta()
                            .getDisplayName());
        }
        return false;
    }

    public String getName() {
        return this.itemStack.getItemMeta()
                .getDisplayName();
    }

    public ItemStack toItemStack() {
        return this.toItemStack(1);
    }

    public ItemStack toItemStack(int amount) {
        ItemStack is = this.itemStack.clone();
        is.setAmount(amount);
        return is;
    }
}
