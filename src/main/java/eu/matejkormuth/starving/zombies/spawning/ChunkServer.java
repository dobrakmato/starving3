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
package eu.matejkormuth.starving.zombies.spawning;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ChunkServer implements Runnable {

    public static final int LOAD_RADIUS = 4;

    private ChunkProvider provider;

    public ChunkServer(World world) {
        this.provider = new ChunkProvider(world);
    }

    @Override
    public void run() {
        this.tick();
    }

    private void tick() {
        // Compute loaded chunks.
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        IntStack loadedChunks = new IntStack(
                (int) (players.size() * 2 * Math.pow(
                        LOAD_RADIUS * 2, 2)));
        for (Player p : players) {
            this.addLoadedChunks(loadedChunks, p);
        }
        // Load computed, unload others.
        int x, z;
        boolean isLoaded;
        while (!loadedChunks.isEmpty()) {
            z = loadedChunks.pop();
            x = loadedChunks.pop();
            isLoaded = this.provider.isLoaded(x, z);
            if (!isLoaded) {
                this.provider.loadChunk(x, z);
            }
        }
    }

    private void addLoadedChunks(IntStack coll, Player p) {
        Location loc = p.getLocation();
        int x = (int) (loc.getX() % 16);
        int z = (int) (loc.getZ() % 16);

        push(coll, x, z);
        for (int i = -LOAD_RADIUS; i < LOAD_RADIUS; i++) {
            for (int j = -LOAD_RADIUS; j < LOAD_RADIUS; j++) {
                push(coll, x + i, z + j);
            }
        }
    }

    private void push(IntStack coll, int x, int z) {
        coll.push(x);
        coll.push(z);
    }

    private void loadChunk(int x, int z) {

    }

}
