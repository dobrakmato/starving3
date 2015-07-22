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
package eu.matejkormuth.starving.items.itemmeta.concrete;

import eu.matejkormuth.starving.items.itemmeta.ItemMetaWrapper;
import org.bukkit.inventory.ItemStack;

public class FirearmItemMetaWrapper extends ItemMetaWrapper {

    // Keys
    private static final String SCOPE_KEY = "Scope";
    private static final String FOREGRIP_KEY = "Foregrip";
    private static final String SILENCER_KEY = "Silencer";
    private static final String AMMO_KEY = "Ammo";

    public FirearmItemMetaWrapper(ItemStack stack) {
        super(stack);
    }

    // Ammo

    public int getCurrentAmmo() {
        return this.valueHandler.getInteger(AMMO_KEY);
    }

    public void setCurrentAmmo(int amount) {
        this.valueHandler.set(AMMO_KEY, amount);
    }

    // Attachments

    public boolean hasSilencer() {
        return this.valueHandler.getBoolean(SILENCER_KEY);
    }

    public void setSilencer(boolean silencer) {
        this.valueHandler.set(SILENCER_KEY, silencer);
    }

    public boolean hasForegrip() {
        return this.valueHandler.getBoolean(FOREGRIP_KEY);
    }

    public void setForegrip(boolean foregrip) {
        this.valueHandler.set(FOREGRIP_KEY, foregrip);
    }

    public boolean hasScope() {
        return this.valueHandler.getBoolean(SCOPE_KEY);
    }

    public void setScope(boolean scope) {
        this.valueHandler.set(SCOPE_KEY, scope);
    }
}
