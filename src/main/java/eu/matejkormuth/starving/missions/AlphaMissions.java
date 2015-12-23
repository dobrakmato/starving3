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
package eu.matejkormuth.starving.missions;

import eu.matejkormuth.starving.cinematics.Clip;
import eu.matejkormuth.starving.cinematics.ClipPlayer;
import eu.matejkormuth.starving.main.Alpha;
import eu.matejkormuth.starving.missions.rewards.ItemStackReward;
import eu.matejkormuth.starving.missions.storage.PlayerMissionContext;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Alpha
@UtilityClass
public class AlphaMissions {
    static void init(MissionsModule m) {
        // From last to first.
        Mission mission2 = new Mission("Dostan sa do mesta prezivsich", 7002);

        Goal goal0 = new Goal("Zaklop na branu mesta a dostan sa dnu", mission2) {
            {
                addOnCompletedListener(context -> {
                    // Hra dokoncena.
                    context.getPlayer().sendMessage("Thanks for playing.");
                });
            }
        };

        Mission mission1 = new Mission("Zachran sa!", 7001);

        Goal goal1 = new Goal("Chod za vedcom do kaclu", mission1) {
            {
                addReward(new ItemStackReward(new ItemStack(Material.PAPER, 10), new ItemStack(Material.RECORD_3)));
            }
        };

        Goal goal2 = new Goal("Nalod sa v pristave na cln", mission1) {
            @Override
            public void onCompleted(PlayerMissionContext context) {
                // Play cinematic.
                Clip clip = m.getCinematicsModule().getCinematics().loadClip("alpha_2");
                ClipPlayer player = m.getCinematicsModule().getCinematics().createPlayer(clip);
                player.getCamera().addObserver(context.getPlayer());
                player.addListener(new ClipPlayer.ClipPlayerListener() {
                    @Override
                    public void onStop(ClipPlayer clipPlayer) {
                        // Dajme hracovi veci.
                        context.getPlayer().getInventory().addItem();

                        // Prepnime na dalsiu misiu.
                        mission1.complete(context.getPlayer());
                        mission2.start(context.getPlayer());
                    }
                });
                player.play();
            }
        };


        // Add created missions.
        m.addMission(mission1);
        m.addMission(mission2);
    }
}
