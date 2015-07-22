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
package eu.matejkormuth.starving.sounds.ambient;

import java.util.Random;

import org.bukkit.entity.Player;

public class PlayerChannel {

    private Player player;
    private Random random;
    private Atmosphere currentAtmosphere;

    private long repeatingLastPlay;

    public PlayerChannel(Player player) {
        this.player = player;
        this.random = new Random();
    }

    public void setAtmosphere(Atmosphere atmosphere) {
        this.currentAtmosphere = atmosphere;
        this.playRepeating();
    }

    private void playRepeating() {
        repeatingLastPlay = timeStamp();
        for (RepeatingSound rs : currentAtmosphere.getRepeatingSounds()) {
            rs.play(this.player, this.player.getLocation(), Float.MAX_VALUE, 1);
        }
    }

    private void playRandom() {
       for (RandomSound rs : currentAtmosphere.getRandomSounds()) {
            if (random.nextFloat() <= rs.getChance()) {
                rs.play(this.player, this.player.getLocation(),
                        Float.MAX_VALUE, 1);
            }
        }
    }

    public void update() {
        this.playRandom();

        if (timeStamp() >= repeatingLastPlay + currentAtmosphere.getLength()
                - Atmosphere.CROSSFADE_LENGTH) {
            this.playRepeating();
        }
    }

    private static long timeStamp() {
        return System.currentTimeMillis();
    }

    public Atmosphere getAtmosphere() {
        return this.currentAtmosphere;
    }
}
