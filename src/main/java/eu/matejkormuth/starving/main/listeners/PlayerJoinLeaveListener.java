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

import eu.matejkormuth.starving.main.Data;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerJoinLeaveListener implements Listener {

    private static final Logger log = LoggerFactory.getLogger(PlayerJoinLeaveListener.class);

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        // Create or load data for player.
        Data.of(event.getPlayer());

        //event.setJoinMessage(ChatColor.RED + "[★★★] Sudruh " + event.getPlayer().getName() + " sa pripojil!");

        //if (Math.random() > 0.8f) {
        //    Bukkit.broadcastMessage(ChatColor.RED + "=====================================");
        //    Bukkit.broadcastMessage("V komunistickej spoločnosti, kde nikto nemá výhradné pole pôsobnosti, ale každý sa môže stať vynikajúcim v hociktorom odbore, v ktorom si želá, spoločnosť reguluje všeobecnú produkciu a toto mi robí príležitosť robiť jednu vec dnes a inú vec zajtra, poľovať ráno, rybárčiť popoludní, večer pásť dobytok, kritizovať po obede tak ako chcem, bez toho aby som sa stal poľovníkom, rybárom, pastierom alebo kritikom.");
        //    Bukkit.broadcastMessage(ChatColor.RED + "=====================================");
        //}
    }

    @EventHandler
    private void onPlayerLeave(final PlayerQuitEvent event) {
        // Save data of player.
        log.info("Saving data of player {}.", event.getPlayer().getName());
        Data.of(event.getPlayer()).save();

        //event.setQuitMessage(ChatColor.RED + "[★★★] Sudruh " + event.getPlayer().getName() + " sa odpojil!");
    }
}
