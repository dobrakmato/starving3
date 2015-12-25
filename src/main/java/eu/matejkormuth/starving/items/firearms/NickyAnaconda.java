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
import eu.matejkormuth.starving.sounds.Sounds;
import eu.matejkormuth.starving.sounds.SoundsModule;

public class NickyAnaconda extends Firearm {

    public NickyAnaconda(ItemManager itemManager, SoundsModule soundsModule, NMSModule nmsModule) {
        this(itemManager, soundsModule, Mappings.NICKIMINAJANACONDA, "Nicky Anaconda", nmsModule);
    }

    public NickyAnaconda(ItemManager itemManager, SoundsModule soundsModule, Mapping mapping, String name, NMSModule nmsModule) {
        super(itemManager, soundsModule, mapping, name, NickyAnaconda.class, nmsModule);
        this.setAmmoType(AmmunitionType.SHORT);
        this.setClipSize(6);
        this.setFireRate(1);
        this.setInaccurancy(0.2f);
        this.setScopedInaccurancy(0.02f);
        this.setScopedInaccurancy(0.01f);
        this.setNoiseLevel(0.5f);
        this.setProjectileSpeed(3f);
        this.setRecoil(6000.6f); //0.6f
        this.setReloadTime(20);

        this.fireSound = Sounds.NICKY_ANACONDA_FIRE;
        this.reloadSound = Sounds.NICKY_ANACONDA_RELOAD;
    }
}
