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

import eu.matejkormuth.starving.zombies.Zombie;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Random;

public class ZombieSpawnListener implements Listener {

    private static Random random = new Random();

    private static final int NEARBY_SIZE = 64;

    @EventHandler
    private void onChunkLoad(final ChunkLoadEvent event) {
        int zombiesInChunk = countZombies(event.getChunk());
        int zombiesNearby = countZombiesNearby(event.getChunk().getBlock(7, 64, 7).getLocation());

        if (zombiesNearby < nearbyNeededZombieCount()) {
            if (random.nextBoolean()) {
                // Spawn in this chunk.
                int count = random.nextInt(3) + 1;
                spawnInChunk(event.getChunk(), count);
            }
        }
    }

    private static int nearbyNeededZombieCount() {
        return NEARBY_SIZE;
    }

    private static void spawnInChunk(Chunk chunk, int count) {
        // Find starting position.
        int startingX = random.nextInt(15);
        int startingZ = random.nextInt(15);

        // Spawn amount.
        int localX, localZ;
        for (int i = 0; i < count; i++) {
            localX = random.nextInt(10) - 5;
            localZ = random.nextInt(10) - 5;

            // Clamp values to valid chunk positions.
            int x = clamp(startingX + localX, 0, 15);
            int z = clamp(startingZ + localZ, 0, 15);

            spawnZombie(chunk, x, z);
        }
    }

    private static int clamp(int x, int min, int max) {
        return Math.min(Math.max(x, min), max);
    }

    private static void spawnZombie(Chunk chunk, int x, int z) {
        // Find Y value.
        Block b;
        for (int y = 255; y > 0; y++) {
            b = chunk.getWorld().getBlockAt(x, y, z);

            // If this is solid block and it is not a LEAVES block.
            if (b.getType().isSolid() && b.getType() != Material.LEAVES && b.getType() != Material.LEAVES_2) {
                // Spawn in one zombie.
                new Zombie(new Location(chunk.getWorld(), chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z));
            }
        }
        // We couldn't spawn entity.
    }

    private static int countZombiesNearby(Location location) {
        return location.getWorld().getNearbyEntities(location, NEARBY_SIZE, NEARBY_SIZE, NEARBY_SIZE).size();
    }

    private static int countZombies(Chunk chunk) {
        int count = 0;
        for (Entity e : chunk.getEntities()) {
            if (e.getType() == EntityType.ZOMBIE) {
                count++;
            }
        }
        return count;
    }
}
