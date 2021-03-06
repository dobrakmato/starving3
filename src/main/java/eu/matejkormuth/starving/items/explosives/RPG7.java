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
package eu.matejkormuth.starving.items.explosives;

import eu.matejkormuth.bukkit.Actions;
import eu.matejkormuth.starving.items.Category;
import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.Mappings;
import eu.matejkormuth.starving.items.Rarity;
import eu.matejkormuth.starving.items.base.Item;
import eu.matejkormuth.starving.rockets.Rocket;
import eu.matejkormuth.starving.rockets.RocketUniverse;
import eu.matejkormuth.starving.rockets.RocketsModule;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class RPG7 extends Item {

    private RocketUniverse mainUniverse;

    public RPG7(RocketsModule rocketsModule) {
        super(Mappings.RPG7, "RPG-7");
        this.mainUniverse = rocketsModule.getRocketUniverse();
        this.setCategory(Category.FIREARMS);
        this.setRarity(Rarity.RARE);
        this.setMaxStackAmount(1);
    }

    @Override
    public InteractResult onInteract(Player player, Action action,
                                     Block clickedBlock, BlockFace clickedFace) {
        if (Actions.isRightClick(action)) {
            Location spawnLocation = player.getEyeLocation();
            spawnLocation.setPitch(0);
            spawnLocation.setYaw(0);

            mainUniverse.addRocket(new Rocket(player, spawnLocation, player.getEyeLocation().getDirection()));
        }
        return super.onInteract(player, action, clickedBlock, clickedFace);
    }
}
