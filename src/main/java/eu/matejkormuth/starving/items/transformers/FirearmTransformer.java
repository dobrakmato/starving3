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
package eu.matejkormuth.starving.items.transformers;

import eu.matejkormuth.starving.items.ItemManager;
import eu.matejkormuth.starving.items.base.Firearm;
import eu.matejkormuth.starving.items.base.Item;
import eu.matejkormuth.starving.items.firearms.*;
import eu.matejkormuth.starving.items.firearms.scoped.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class FirearmTransformer {

    private ItemManager itemManager;

    public FirearmTransformer(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public ItemStack toScoped(ItemStack unscoped) {
        // Create new scoped and copy properties.
        Item item = itemManager.findItem(unscoped);

        if (AK47.class.equals(item.getClass())) {
            return createWithLore(ScopedAK47.class, unscoped);
        } else if (Revolver.class.equals(item.getClass())) {
            return createWithLore(ScopedRevolver.class, unscoped);
        } else if (Dragunov.class.equals(item.getClass())) {
            return createWithLore(ScopedDragunov.class, unscoped);
        } else if (Glock.class.equals(item.getClass())) {
            return createWithLore(ScopedGlock.class, unscoped);
        } else if (M16.class.equals(item.getClass())) {
            return createWithLore(ScopedM16.class, unscoped);
        } else if (Mossberg500.class.equals(item.getClass())) {
            return createWithLore(ScopedMossberg500.class, unscoped);
        } else if (MP5.class.equals(item.getClass())) {
            return createWithLore(ScopedMP5.class, unscoped);
        } else if (NickyAnaconda.class.equals(item.getClass())) {
            return createWithLore(ScopedNickyAnaconda.class, unscoped);
        } else {
            throw new IllegalArgumentException(
                    "Itemstack must be supported firearm. Found: "
                            + item.getClass().getSimpleName());
        }
    }

    private ItemStack createWithLore(Class<? extends Firearm> clazz,
            ItemStack metadataSource) {
        ItemStack newItemStack = createItemStack(clazz);
        ItemMeta newItemMeta = newItemStack.getItemMeta();
        String name = newItemMeta.getDisplayName();
        newItemMeta.setLore(metadataSource.getItemMeta().getLore());
        newItemMeta.setDisplayName(name);
        newItemStack.setItemMeta(newItemMeta);
        
        return newItemStack;
    }

    private ItemStack createItemStack(Class<? extends Firearm> clazz) {
        return itemManager.newItemStack(clazz);
    }

    public ItemStack fromScoped(ItemStack scoped) {
        // Create new unscoped and copy properties.
        Item item = itemManager.findItem(scoped);

        if (ScopedAK47.class.equals(item.getClass())) {
            return createWithLore(AK47.class, scoped);
        } else if (ScopedRevolver.class.equals(item.getClass())) {
            return createWithLore(Revolver.class, scoped);
        } else if (ScopedDragunov.class.equals(item.getClass())) {
            return createWithLore(Dragunov.class, scoped);
        } else if (ScopedGlock.class.equals(item.getClass())) {
            return createWithLore(Glock.class, scoped);
        } else if (ScopedM16.class.equals(item.getClass())) {
            return createWithLore(M16.class, scoped);
        } else if (ScopedMossberg500.class.equals(item.getClass())) {
            return createWithLore(Mossberg500.class, scoped);
        } else if (ScopedMP5.class.equals(item.getClass())) {
            return createWithLore(MP5.class, scoped);
        } else if (ScopedNickyAnaconda.class.equals(item.getClass())) {
            return createWithLore(NickyAnaconda.class, scoped);
        } else {
            throw new IllegalArgumentException(
                    "Itemstack must be supported firearm. Found: "
                            + item.getClass().getSimpleName());
        }
    }
}
