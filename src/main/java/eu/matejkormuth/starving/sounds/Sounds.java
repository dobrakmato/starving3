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
package eu.matejkormuth.starving.sounds;

import org.bukkit.entity.Player;

/**
 * Enumeration of all available sounds.
 */
public final class Sounds {
    private Sounds() {
    }

    // Random items.
    public static final Sound GUITAR_PLAY = new Sound("guitar.play", 16f, SoundType.SINGLE_LONG);
    public static final Sound GUITAR_HIT = new Sound("guitar.hit", 16f, SoundType.SINGLE);
    public static final Sound GUITAR_BREAK = new Sound("guitar.break", 16f, SoundType.SINGLE);

    public static final Sound GUITAR_SONG1 = new Sound("guitar.song1", SoundType.SINGLE_LONG);
    public static final Sound GUITAR_SONG2 = new Sound("guitar.song2", SoundType.SINGLE_LONG);
    public static final Sound GUITAR_SONG3 = new Sound("guitar.song3", SoundType.SINGLE_LONG);
    public static final Sound GUITAR_SONG4 = new Sound("guitar.song4", SoundType.SINGLE_LONG);

    // Explosives.
    public static final Sound C4_EXPLODE = new Sound("explosives.c4.explode", SoundType.SINGLE);
    public static final Sound C4_PLACE = new Sound("explosives.c4.place", SoundType.SINGLE);

    public static final Sound FLARE_GUN_FIRE = new Sound("explosives.c4.fire", SoundType.SINGLE);

    public static final Sound GRENADE_EXPLODE = new Sound("pyrotechnics.grenade.explode", SoundType.SINGLE);
    public static final Sound GRENADE_THROW = new Sound("pyrotechnics.grenade.throw", SoundType.SINGLE);


    public static final Sound MOLOTOV_THROW = new Sound("pyrotechnics.molotov.throw", SoundType.SINGLE);
    public static final Sound MOLOTOV_BREAK = new Sound("pyrotechnics.molotov.break", SoundType.SINGLE);

    public static final Sound PETARD_BURN = new Sound("pyrotechnics.petard.burn", SoundType.SINGLE);
    public static final Sound PETARD_EXPLODE = new Sound("pyrotechnics.petard.explosion", SoundType.SINGLE);

    public static final Sound SMOKESHELL_THROW = new Sound("pyrotechnics.smokeshell.throw", SoundType.SINGLE);
    public static final Sound SMOKESHELL_BREAK = new Sound("pyrotechnics.smokeshell.break", SoundType.SINGLE);
    public static final Sound SMOKESHELL_BURN = new Sound("pyrotechnics.smokeshell.burn", SoundType.SINGLE);

    // Firearms.
    public static final Sound AK47_FIRE = new Sound("firearms.ak47.fire", SoundType.SINGLE);
    public static final Sound AK47_RELOAD = new Sound("firearms.ak47.reload", SoundType.SINGLE);

    public static final Sound DRAGUNOV_FIRE = new Sound("firearms.dragunov.fire", SoundType.SINGLE);
    public static final Sound DRAGUNOV_RELOAD = new Sound("firearms.dragunov.reload", SoundType.SINGLE);

    public static final Sound GLOCK_FIRE = new Sound("firearms.glock.fire", SoundType.SINGLE);
    public static final Sound GLOCK_RELOAD = new Sound("firearms.glock.reload", SoundType.SINGLE);

    public static final Sound M16_FIRE = new Sound("firearms.m16.fire", SoundType.SINGLE);
    public static final Sound M16_RELOAD = new Sound("firearms.m16.reload", SoundType.SINGLE);

    public static final Sound MOSSBERG500_FIRE = new Sound("firearms.mossberg500.fire", SoundType.SINGLE);
    public static final Sound MOSSBERG500_RELOAD = new Sound("firearms.mossberg500.reload", SoundType.SINGLE);

    public static final Sound MP5_FIRE = new Sound("firearms.mp5.fire", SoundType.SINGLE);
    public static final Sound MP5_RELOAD = new Sound("firearms.mp5.reload", SoundType.SINGLE);

    public static final Sound NICKY_ANACONDA_FIRE = new Sound("firearms.nickyanaconda.fire", SoundType.SINGLE);
    public static final Sound NICKY_ANACONDA_RELOAD = new Sound("firearms.nickyanaconda.reload", SoundType.SINGLE);

    public static final Sound REVOLVER_FIRE = new Sound("firearms.revolver.fire", SoundType.SINGLE);
    public static final Sound REVOLVER_RELOAD = new Sound("firearms.revolver.reload", SoundType.SINGLE);

    // Flamethrower.
    public static final Sound FLAMETHROWER_START = new Sound("flamethrower.start", 24f, SoundType.SINGLE);
    public static final Sound FLAMETHROWER_LOOP = new Sound("flamethrower.loop", 24f, SoundType.SINGLE);
    public static final Sound FLAMETHROWER_END = new Sound("flamethrower.end", 24f, SoundType.SINGLE);

    // Melee.
    public static final Sound WOODEN_STICK_HIT = new Sound("melee.stick.hit", 16f, SoundType.SINGLE);
    public static final Sound WOODEN_STICK_BREAK = new Sound("melee.stick.break", 16f, SoundType.SINGLE);

    public static final Sound WOODEN_STICK_RODS_HIT = new Sound("melee.stickrods.hit", 16f, SoundType.SINGLE);
    public static final Sound WOODEN_STICK_RODS_BREAK = new Sound("melee.stickrods.break", 16f, SoundType.SINGLE);

    public static final Sound WOODEN_STICK_RODS_KNIVES_HIT = new Sound("melee.stickknives.hit", 16f, SoundType.SINGLE);
    public static final Sound WOODEN_STICK_RODS_KNIVES_BREAK = new Sound("melee.stickknives.break", 16f, SoundType.SINGLE);

    public static final Sound MACHETE_HIT = new Sound("melee.machete.hit", 32f, SoundType.SINGLE);

    public static final Sound SUITCASE_HIT = new Sound("melee.suitcase.hit", 16f, SoundType.SINGLE);

    // Boxing.
    public static final Sound HAND_HIT = new Sound("melee.hand.hit", 16f, SoundType.SINGLE);
    public static final Sound HAND_SWING = new Sound("melee.hand.swing", 8f, SoundType.SINGLE);

    public static final Sound DEATH = new Sound("death", 16f, SoundType.SINGLE);
    public static final Sound DRAMATIC = new Sound("dramatic", 16f, SoundType.SINGLE);
    public static final Sound BLANK = new Sound("blank", 16f, SoundType.SINGLE);

    /**
     * Stops all playing sounds for specified player.
     *
     * @param player player to stop all sounds to
     */
    public static void stopAll(Player player) {
        int CLIENT_MAX_SOUNDS = 45;
        for (int i = 0; i < CLIENT_MAX_SOUNDS; i++) {
            BLANK.play(player.getEyeLocation(), player);
        }
    }

}
