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

import eu.matejkormuth.starving.cinematics.ClipPlayer;
import eu.matejkormuth.starving.cinematics.PlayerServer;
import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.main.Time;

import java.util.HashSet;
import java.util.Set;

public class V4ClipPlayerServer implements Runnable, PlayerServer {

    private Set<V4ClipPlayer> players;

    public V4ClipPlayerServer() {
        this.players = new HashSet<>();

        RepeatingTask.of(this).schedule(Time.ofSeconds(2).toLongTicks(),
                Time.ofTicks(1).toLongTicks());
    }

    @Override
    public void addClipPlayer(ClipPlayer player) {
        this.players.add((V4ClipPlayer) player);
    }

    @Override
    public void removeClipPlayer(ClipPlayer player) {
        this.players.remove(player);
    }

    @Override
    public void run() {
        this.tick();
    }

    private void tick() {
        this.players
                .stream()
                .filter(V4ClipPlayer::isPlaying)
                .forEach(eu.matejkormuth.starving.cinematics.v4.V4ClipPlayer::nextFrame);
    }
}
