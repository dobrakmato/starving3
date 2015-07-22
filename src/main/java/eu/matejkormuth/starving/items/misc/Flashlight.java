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
package eu.matejkormuth.starving.items.misc;

import eu.matejkormuth.starving.items.InteractResult;
import eu.matejkormuth.starving.items.Mapping;
import eu.matejkormuth.starving.items.base.Item;
import eu.matejkormuth.starving.items.itemmeta.concrete.FlashlightItemMetaWrapper;
import eu.matejkormuth.starving.main.Data;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import eu.matejkormuth.bukkit.Actions;


public class Flashlight extends Item {

    public Flashlight() {
        super(new Mapping(Material.BLAZE_POWDER), "Flashlight");
    }

    @Override
    public InteractResult onInteract(Player player, Action action,
            Block clickedBlock, BlockFace clickedFace) {
        if (Actions.isRightClick(action)) {
            switchState(player);
            player.getWorld().playSound(player.getLocation(), Sound.CLICK,
                    0.8f, 2.0f);
        }
        return super.onInteract(player, action, clickedBlock, clickedFace);
    }

    private void switchState(Player player) {
        Data data = Data.of(player);
        FlashlightItemMetaWrapper wrapper = new FlashlightItemMetaWrapper(player.getItemInHand());
        if (data.isFlashlightOn()) {

            data.setFlashlightOn(false);
            wrapper.setSwitchedOn(false);
        } else {

            data.setFlashlightOn(true);
            wrapper.setSwitchedOn(true);
        }
        player.setItemInHand(wrapper.apply());

    }

}
