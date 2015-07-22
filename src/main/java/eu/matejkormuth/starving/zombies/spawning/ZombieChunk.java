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

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

public class ZombieChunk {
    public static final int SIZE_X = 16;
    public static final int SIZE_Y = 128;
    public static final int SIZE_Z = 16;

    private int x;
    private int z;
    private World bukkitWorld;
    private Chunk bukkitChunk;
    private boolean loaded;

    public ZombieChunk(World w, int x, int z) {
        this.initialize();
    }

    private void initialize() {
        bukkitChunk = bukkitWorld.getChunkAt(x, z);
    }

    public Chunk getBukkitChunk() {
        if (!loaded) {
            throw new RuntimeException("This ZombieChunk is not loaded!");
        }

        return bukkitWorld.getChunkAt(x, z);
    }

    public void removeAllZombies() {
        if (!loaded) {
            throw new RuntimeException("This ZombieChunk is not loaded!");
        }

        for (Entity e : this.bukkitChunk.getEntities()) {
            if (e.getType() == EntityType.ZOMBIE) {
                e.remove();
            }
        }
    }

    public void storeAllZombies() {
        if (!loaded) {
            throw new RuntimeException("This ZombieChunk is not loaded!");
        }
         
        // TODO: Implement.
    }

    public void loadAllZombies() {
        // TODO: Implement.
    }

    public int getZombieCount() {
        if (!loaded) {
            throw new RuntimeException("This ZombieChunk is not loaded!");
        }

        return this.countZombies();
    }

    private int countZombies() {
        int count = 0;
        for (Entity e : this.bukkitChunk.getEntities()) {
            if (e.getType() == EntityType.ZOMBIE) {
                count++;
            }
        }
        return count;
    }

    public void load() {
        this.loadAllZombies();
        
        
    }

    public void unload() {
        if (!loaded) {
            throw new RuntimeException("This ZombieChunk is not loaded!");
        }

        this.storeAllZombies();
        this.removeAllZombies();
        this.bukkitChunk = null;
        this.bukkitWorld = null;
    }

    public Zombie spawnZombie(int x, int y, int z) {
        return (Zombie) this.spawnEntity(x, y, z, EntityType.ZOMBIE);
    }

    public Entity spawnEntity(int x, int y, int z, EntityType type) {
        if (!loaded) {
            throw new RuntimeException("This ZombieChunk is not loaded!");
        }
        return bukkitWorld.spawnEntity(new Location(bukkitWorld, this.x * 16
                + x, y, this.z * 16 + z), type);
    }
}
