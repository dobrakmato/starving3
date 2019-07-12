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
package eu.matejkormuth.starving.resourcepacks.listeners;

import eu.matejkormuth.starving.main.Data;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerJoinListener implements Listener {

    private static final Logger log = LoggerFactory.getLogger(PlayerJoinListener.class);

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        // no more pls.
    }

    @EventHandler
    private void onLogin(final LoginEvent event) {
        // Read data and send resource pack.
        String rp = Data.of(event.getPlayer()).getResourcePack();
        if (rp.equalsIgnoreCase("builders")) {
            event.getPlayer().setResourcePack("http://www.starving.eu/2/rp/latest_builder.zip");
        } else if (rp.equalsIgnoreCase("players")) {
            event.getPlayer().setResourcePack("http://www.starving.eu/2/rp/latest.zip");
        } else if (rp.equalsIgnoreCase("empty")) {
            event.getPlayer().setResourcePack("http://www.starving.eu/2/rp/empty.zip");
        } else {
            log.warn("Player {} is using unknown resource pack type '{}'!", event.getPlayer().getName(), rp);
        }
    }
}
