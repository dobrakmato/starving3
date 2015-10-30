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
package eu.matejkormuth.starving.main;

import eu.matejkormuth.bmboot.Dependency;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.filestorage.FileStorageModule;
import eu.matejkormuth.starving.main.listeners.BlockFadeListener;
import eu.matejkormuth.starving.main.listeners.MobDropsListener;
import eu.matejkormuth.starving.main.listeners.PlayerJoinLeaveListener;
import eu.matejkormuth.starving.main.playtime.PlaytimeScheduler;
import eu.matejkormuth.starving.main.tasks.PlaytimeIncrementTask;

public class MainModule extends Module {

    @Dependency
    private FileStorageModule fileStorageModule;

    private PlaytimeScheduler playtimeScheduler;

    @Override
    public void onEnable() {
        // Initialize Data class.
        Data.profilesRoot = fileStorageModule.getPath("players");
        // Initialize playtime scheduler.
        playtimeScheduler = new PlaytimeScheduler();
        // Initialize listeners.
        listener(new PlayerJoinLeaveListener());
        listener(new BlockFadeListener());
        listener(new MobDropsListener());
        // Start repeating tasks.
        new PlaytimeIncrementTask().schedule(1L);
    }

    @Override
    public void onDisable() {

    }

    public PlaytimeScheduler getPlaytimeScheduler() {
        return playtimeScheduler;
    }
}
