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
package eu.matejkormuth.starving.items.firearms;

import eu.matejkormuth.starving.items.AmmunitionType;
import eu.matejkormuth.starving.items.ItemManager;
import eu.matejkormuth.starving.items.Mapping;
import eu.matejkormuth.starving.items.Mappings;
import eu.matejkormuth.starving.items.base.Firearm;
import eu.matejkormuth.starving.nms.NMSModule;
import eu.matejkormuth.starving.sounds.SoundsModule;

public class M16 extends Firearm {

    public M16(ItemManager itemManager, SoundsModule soundsModule, NMSModule nmsModule) {
        this(itemManager, soundsModule, Mappings.M16, "M16", nmsModule);
    }

    public M16(ItemManager itemManager, SoundsModule soundsModule, Mapping mapping, String name, NMSModule nmsModule) {
        super(itemManager, soundsModule, mapping, name, M16.class, nmsModule);
        this.setAmmoType(AmmunitionType.LONG);
        this.setClipSize(45);
        this.setFireRate(14);
        this.setInaccurancy(0.4f);
        this.setScopedInaccurancy(0.04f);
        this.setNoiseLevel(0.7f);
        this.setProjectileSpeed(3.5f);
        this.setRecoil(0.55f);
        this.setReloadTime(40);
    }
}
