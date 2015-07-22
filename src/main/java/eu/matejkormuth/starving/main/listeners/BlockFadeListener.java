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
package eu.matejkormuth.starving.main.listeners;

import eu.matejkormuth.starving.persistence.Persist;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;


public class BlockFadeListener implements Listener {

    @Persist(key = "DISABLE_BLOCK_FADE")
    private static boolean DISABLE_BLOCK_FADE = true;

    @Persist(key = "DISABLE_BLOCK_FORM")
    private static boolean DISABLE_BLOCK_FORM = true;

    @Persist(key = "DISABLE_BLOCK_SPREAD")
    private static boolean DISABLE_BLOCK_SPREAD = true;

    @Persist(key = "DISABLE_BLOCK_FROMTO")
    private static boolean DISABLE_BLOCK_FROMTO = true;

    @Persist(key = "DISABLE_BLOCK_GROW")
    private static boolean DISABLE_BLOCK_GROW = true;

    @Persist(key = "DISABLE_LEAVES_DECAY")
    private static boolean DISABLE_LEAVES_DECAY = true;

    @EventHandler
    private void onBlockFade(final BlockFadeEvent event) {
        if (DISABLE_BLOCK_FADE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockForm(final BlockFormEvent event) {
        if (DISABLE_BLOCK_FORM) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockSpread(final BlockSpreadEvent event) {
        if (DISABLE_BLOCK_SPREAD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockFromTo(final BlockFromToEvent event) {
        if (DISABLE_BLOCK_FROMTO) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockGrow(final BlockGrowEvent event) {
        if (DISABLE_BLOCK_GROW) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onLeavesDecay(final LeavesDecayEvent event) {
        if (DISABLE_LEAVES_DECAY) {
            event.setCancelled(true);
        }
    }
}
