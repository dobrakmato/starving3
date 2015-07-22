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
package eu.matejkormuth.starving.achievements;

import eu.matejkormuth.starving.Module;
import eu.matejkormuth.starving.achievements.death.Atlantis;
import eu.matejkormuth.starving.achievements.death.Dynamite;
import eu.matejkormuth.starving.achievements.death.FindingClownfish;
import eu.matejkormuth.starving.achievements.death.Hades;
import eu.matejkormuth.starving.achievements.death.HungerGames;
import eu.matejkormuth.starving.achievements.death.IBelieveICanFly;
import eu.matejkormuth.starving.achievements.death.Ikaros;
import eu.matejkormuth.starving.achievements.death.OnADiet;
import eu.matejkormuth.starving.achievements.death.Persephone;
import eu.matejkormuth.starving.achievements.death.TheTorch;
import eu.matejkormuth.starving.achievements.death.Zeus;
import eu.matejkormuth.starving.achievements.kill.Ares;
import eu.matejkormuth.starving.achievements.kill.Baptised;
import eu.matejkormuth.starving.achievements.kill.HitLikeYouDo;
import eu.matejkormuth.starving.achievements.kill.Mercenary;
import eu.matejkormuth.starving.achievements.kill.Shooter;

public class AchievementsModule extends Module {

    @Override
    public void onEnable() {
        // Register all achievements as listeners.
        
        // Death achievements.
        listener(new Atlantis());
        listener(new Dynamite());
        listener(new FindingClownfish());
        listener(new Hades());
        listener(new HungerGames());
        listener(new IBelieveICanFly());
        listener(new Ikaros());
        listener(new OnADiet());
        listener(new Persephone());
        listener(new TheTorch());
        listener(new Zeus());

        // Kill achievements.
        listener(new Ares());
        listener(new Baptised());
        listener(new HitLikeYouDo());
        listener(new Mercenary());
        listener(new Shooter());
    }

    @Override
    public void onDisable() {

    }
}
