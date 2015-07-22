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
import eu.matejkormuth.starving.items.Mapping;
import eu.matejkormuth.starving.items.Rarity;
import eu.matejkormuth.starving.main.Data;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

import java.util.Random;

public class DrinkItem extends ConsumableItem {

    private static final Random random = new Random();

    private int staminaIncrement;
    private float hydrationIncrement;
    private int healthIncrement;
    private int infectionChance;

    public DrinkItem(String name, int staminaIncrement,
                     float hydrationIncrement, int foodLevelIncrement,
                     int healthIncrement, int infectionChanceInPercent) {
        super(foodLevelIncrement, 0, new Mapping(Material.POTION, 0), name);
        this.staminaIncrement = staminaIncrement;
        this.hydrationIncrement = hydrationIncrement;
        this.setFoodLevelIncrement(foodLevelIncrement);
        this.healthIncrement = healthIncrement;
        this.infectionChance = infectionChanceInPercent;

        this.setCategory(Category.DRINKS);
        this.setRarity(Rarity.COMMON);
    }

    @Override
    protected void onConsume0(Player player) {
        Data d = Data.of(player);
        d.incrementStamina(staminaIncrement);
        d.incrementHydrationLevel(hydrationIncrement);

        this.incrementHealth(player);
        if (random.nextDouble() * 100 <= infectionChance) {
            d.setInfected(true);
        }
    }

    private void incrementHealth(Player player) {
        if (player.getHealth() + this.healthIncrement > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        } else {
            player.setHealth(player.getHealth() + this.healthIncrement);
        }
    }
}
