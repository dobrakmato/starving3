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
package eu.matejkormuth.starving.physical;

import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.main.Time;
import eu.matejkormuth.starving.physical.mapped.MappedValue;
import eu.matejkormuth.starving.physical.temperature.Temperature;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Body {

    /**
     * Owner of this body.
     */
    private Player owner;

    private Temperature temperature_obj;

    private MappedValue<Float, Integer> energy;
    private MappedValue<Float, Float> temperature;

    private float hunger;
    private float thirst;

    private boolean ableToSprint;

    private RepeatingTask bleeding;

    private Disease disease;

    public Body(Player player) {
        energy = new MappedValue<>(f -> (int) (f * 20F), player::setFoodLevel);
        temperature = new MappedValue<>(f -> (f - 30f) / 13f, player::setExp);

        // null means no disease
        disease = null;

        // TODO: Add listener that checks body's ableToSprint.
        // TODO: Add fracture listener.

        // Temperature modifiers
        // * biome temperature
        // * player's clothing
        // * sickness

        // Max. health decrements
        // * bleeding
        // * radiation

        // Max health increments
        // * periodic task to 20HP
    }

    public void bleed() {
        this.bleeding = RepeatingTask.of(() -> {
            this.owner.damage(1D);
        }).schedule(Time.ofSeconds(5));
    }

    public void fracture(int level) {
        switch (level) {
            case 0:
                this.fractureLvl0();
                break;
            case 1:
                this.fractureLvl1();
                break;
            case 2:
                this.fractureLvl2();
                break;
            default:
                throw new RuntimeException("Invalid fracture level! Valid levels are 0-2.");
        }
    }

    private void fractureLvl0() {

    }

    private void fractureLvl1() {

    }

    private void fractureLvl2() {

    }

    // Executed each second.
    public void live() {
        // Process temperature changes.


    }
}
