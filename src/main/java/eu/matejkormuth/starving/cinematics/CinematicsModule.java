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
package eu.matejkormuth.starving.cinematics;

import eu.matejkormuth.bmboot.Dependency;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.cinematics.studio.Session;
import eu.matejkormuth.starving.filestorage.FileStorageModule;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.WeakHashMap;

public class CinematicsModule extends Module {

    @Dependency
    private FileStorageModule fileStorageModule;

    /**
     * Map of user sessions.
     */
    private Map<Player, Session> playerSessions = new WeakHashMap<>();

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    /**
     * Returns existing player session or creates new if not found.
     *
     * @param player player as session context
     * @return new or existing session assigned to specified player
     */
    public Session getPlayerSession(@Nonnull Player player) {
        playerSessions.putIfAbsent(player, createSession());
        return playerSessions.get(player);
    }

    /**
     * Creates a new session.
     *
     * @return new session
     */
    public Session createSession() {
        return new Session();
    }

    public Clip loadCompiledClip(String name) {
        return null;
    }
}
