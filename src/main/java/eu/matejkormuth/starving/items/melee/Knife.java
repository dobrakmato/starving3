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
package eu.matejkormuth.starving.items.melee;

import eu.matejkormuth.starving.items.Mappings;
import eu.matejkormuth.starving.items.Rarity;
import eu.matejkormuth.starving.items.base.MeleeWeapon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Knife extends MeleeWeapon {

    public Knife() {
        super(Mappings.KNIFE, "Knife", 4, 1);
        this.setRarity(Rarity.COMMON);
        this.setMaxStackAmount(12);
    }

    @Override
    public void onAttack(Player damager, LivingEntity entity, double damage) {
        // If player is behind the zombie, he get 20 HP damage bonus.

        // Calculate difference in yaw.
        float yawDiff = Math.abs(damager.getEyeLocation().getYaw() - entity.getLocation().getYaw());

        if (Math.abs(yawDiff) < 15f) {
            // This is insta-kill from behind.
            super.onAttack(damager, entity, 20);
        } else {
            // We still have 40% chance to give insta-kill.
            if (Math.random() > .6f) {
                super.onAttack(damager, entity, 20);
            } else {
                super.onAttack(damager, entity, damage);
            }
        }
    }
}
