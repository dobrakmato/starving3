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
package eu.matejkormuth.starving.zombies.old;

import eu.matejkormuth.starving.main.Region;
import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.zombies.Zombie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Random;

public class ZSpawnTask_BetaDedina extends RepeatingTask {
    private World world = Bukkit.getWorld("Beta");
    private Vector regionStart = new Vector(558, 25, -269);
    private Vector regionEnd = new Vector(758, 70, -69);
    private Region region = new Region(regionStart, regionEnd, world);
    private Random random = new Random();

    @Override
    public void run() {
        if (this.world == null) {
            this.world = Bukkit.getWorld("Beta");
        }

        if (Bukkit.getOnlinePlayers().size() < 1) {
            return;
        }

        int zcount = countZombies();
        int target = 30;
        int needed = target - zcount;
        for (int i = 0; i < needed; i++) {
            spawn();
        }
    }

    private void spawn() {
        Location loc = randLoc();
        if (loc != null) {
            // Spawn one zombie.
            new Zombie(loc);
        }
    }

    private Location randLoc() {
        int x = this.region.getMinXFloor() + random.nextInt(200);
        int z = this.region.getMinZFloor() + random.nextInt(200);

        int y = findY(x, z);
        if (y == -1) {
            return null;
        }
        // int y = 80;

        return new Location(world, x, y, z);
    }

    private int findY(int x, int z) {
        int i = region.getMinYFloor();
        Block b = null;
        for (; i < 255; i++) {
            b = world.getBlockAt(x, i, z);
            if (b.getType() == Material.GRASS) {
                return i + 2;
            }
        }
        return -1;
    }

    private int countZombies() {
        return world.getEntitiesByClasses(org.bukkit.entity.Zombie.class).size();
    }
}
