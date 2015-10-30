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
package eu.matejkormuth.starving.zombies.groups;

import eu.matejkormuth.starving.zombies.Zombie;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ZombieGroup {

    /**
     * Maximum amount of members in one zombie group.
     */
    private static final int MAX_MEMBERS = 16;
    private static final Random random = new Random();

    private final List<Zombie> members;

    public ZombieGroup() {
        members = new ArrayList<>(MAX_MEMBERS);
    }

    public boolean add(Zombie zombie) {
        if (this.members.size() >= MAX_MEMBERS) {
            throw new IllegalStateException("This group is full.");
        }

        return members.add(zombie);
    }

    public boolean remove(Object o) {
        return members.remove(o);
    }

    /**
     * Returns random member from this group.
     *
     * @return random member from group
     */
    public Zombie getRandomMember() {
        return members.get(random.nextInt(members.size()));
    }

    /**
     * Gets location of this group. Location is computed by averaging positions of all members.
     *
     * @return location of this group
     */
    public Location getLocation() {
        // We are sure these values wont overflow because our map is within bounds of -6000 to 6000.
        int x = 0;
        int z = 0;
        int y = 0;
        int count = 0;
        for (Zombie zombie : members) {
            x += zombie.getX();
            y += zombie.getY();
            z += zombie.getZ();
            count++;
        }
        return new Location(members.get(0).getWorld().getWorld(), x / count, y / count, z / count);
    }

    public int size() {
        return members.size();
    }
}
