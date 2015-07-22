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
package eu.matejkormuth.starving.rollbacker;

import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RollbackStorage {

    // List of changes.
    private final List<RollbackEntry> oldStates;

    public RollbackStorage() {
        oldStates = new ArrayList<>(512);
    }

    public boolean add(RollbackEntry rollbackEntry) {
        return oldStates.add(rollbackEntry);
    }

    public Collection<RollbackEntry> getFromChunk(Chunk chunk) {
        List<RollbackEntry> entries = new ArrayList<>(16);
        int minX = chunk.getX() * 16;
        int minZ = chunk.getZ() * 16;
        int maxX = minX + 16;
        int maxZ = minZ + 16;

        boolean valid = false;
        RollbackEntry current;

        for (int i = 0; i < oldStates.size(); i++) {
            current = oldStates.get(i);

            // Check for valid X and Z.
            valid = between(minX, maxX, current.blockState.getX()) &&
                    between(minZ, maxZ, current.blockState.getZ());

            if(valid) {
                entries.add(current);
            }
        }
        return entries;
    }

    private static boolean between(int min, int max, int val) {
        return val >= min && val <= max;
    }
}
