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
package eu.matejkormuth.starving.worldgen.listeners;

import java.util.Set;

import eu.matejkormuth.starving.worldgen.PlayerSession;
import eu.matejkormuth.starving.worldgen.WorldGenManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import eu.matejkormuth.bukkit.Actions;
import eu.matejkormuth.starving.worldgen.affectedblocks.AffectedBlocksDefinition;

public class WandListener implements Listener {

    private static final Material WAND_MATERIAL = Material.BLAZE_ROD;
    private WorldGenManager manager;

    public WandListener(WorldGenManager manager) {
        this.manager = manager;
    }

    @EventHandler
    private void onWandInteract(final PlayerInteractEvent event) {
        if (Actions.isRightClick(event.getAction())) {
            if (event.getItem() != null
                    && event.getItem().getType().equals(WAND_MATERIAL)) {
                performAction(event.getPlayer());
            }
        }
    }

    private void performAction(Player player) {
        // Retrieve session.
        PlayerSession session = manager.getSession(player);

        // Create definition.
        AffectedBlocksDefinition definition = session.getBrush().createDefinition(
                player.getTargetBlock((Set<Material>) null,
                        session.getMaxDistance()));

        session.getFilter().apply(definition, session.getFilterProperties());
    }
}
