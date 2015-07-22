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

import eu.matejkormuth.starving.items.Mapping;
import org.bukkit.entity.Player;

public abstract class ConsumableItem extends Item {
    private int foodLevelIncrement;
    private float saturationIncrement;

    public ConsumableItem(final int foodLevelIncrement, final float saturationIncrement,
                          final Mapping mapping, final String name) {
        super(mapping, name);
        this.foodLevelIncrement = foodLevelIncrement;
        this.saturationIncrement = saturationIncrement;
    }

    public void onConsume(final Player player) {
        player.setFoodLevel(player.getFoodLevel() + this.foodLevelIncrement);
        player.setSaturation(player.getSaturation() + this.saturationIncrement);
        this.onConsume0(player);
    }

    public int getFoodLevelIncrement() {
        return this.foodLevelIncrement;
    }

    public float getSaturationIncrement() {
        return this.saturationIncrement;
    }

    protected void setFoodLevelIncrement(int foodLevelIncrement) {
        this.foodLevelIncrement = foodLevelIncrement;
    }

    protected void setSaturationIncrement(float saturationIncrement) {
        this.saturationIncrement = saturationIncrement;
    }

    protected abstract void onConsume0(Player player);
}
