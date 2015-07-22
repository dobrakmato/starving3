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
package eu.matejkormuth.starving.cinematics.v4;

import eu.matejkormuth.starving.cinematics.Camera;
import eu.matejkormuth.starving.main.bukkitfixes.Metadata;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class V4Camera implements Camera {

    public static final String LAST_GAME_MODE = "lastGameMode";

    private Set<Player> observers;
    private Location location;

    public V4Camera() {
        this.observers = new HashSet<>();
    }

    @Override
    public void addObserver(Player observer) {
        this.observers.add(observer);

        // Saves his game mode.
        observer.setMetadata(LAST_GAME_MODE, new Metadata(observer.getGameMode()));
        // Update to spectator.
        observer.setGameMode(GameMode.SPECTATOR);
    }

    @Override
    public void removeObserver(Player observer) {
        this.observers.remove(observer);

        // Revert his previous game mode.
        observer.setGameMode((GameMode) observer.getMetadata(LAST_GAME_MODE).get(0).value());
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Collection<Player> getObservers() {
        return this.observers;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
        this.broadcastNewLoc();
    }

    private void broadcastNewLoc() {
        for (Player observer : this.observers) {
            observer.teleport(this.location);
        }
    }

}
