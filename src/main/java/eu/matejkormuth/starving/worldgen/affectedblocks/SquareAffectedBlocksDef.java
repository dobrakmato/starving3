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
package eu.matejkormuth.starving.worldgen.affectedblocks;

import java.util.NoSuchElementException;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class SquareAffectedBlocksDef extends PaintedAffectedBlocksDef implements
        AffectedBlocksDefinition {

    private int minX;
    private int maxX;
    @SuppressWarnings("unused")
    private int minY;
    @SuppressWarnings("unused")
    private int maxY;
    private int minZ;
    private int maxZ;

    public SquareAffectedBlocksDef(int radius, Location center) {
        super(radius, center);

        this.minX = center.getBlockX() - radius;
        this.maxX = center.getBlockX() + radius;
        if (this.isFullHeight()) {
            this.minY = center.getBlockY() - radius;
            this.maxY = center.getBlockY() + radius;
        }
        this.minZ = center.getBlockZ() - radius;
        this.maxZ = center.getBlockZ() + radius;
    }

    @Override
    public Block[] getAffectedBlocks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAffected(Block block) {
        // TODO: isAffected(Block);
        return false;
    }

    @Override
    public boolean isFullHeight() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public SquareAffectedBlocksDefIterator iterator() {
        if (isFullHeight()) {
            // return new FullHeightSquareAffectedBlocksDefIterator();
            throw new UnsupportedOperationException();
        } else {
            return new SquareAffectedBlocksDefIterator();
        }
    }

    public class SquareAffectedBlocksDefIterator implements
            AffectedBlocksIterator {

        private int currentX = minX;
        private int currentZ = minZ;

        @Override
        public boolean hasNext() {
            return !(currentX == maxX && currentZ == maxZ);
        }

        @Override
        public Block next() {
            if (currentX != maxX) {
                currentX++;
            } else {
                if (currentZ != maxZ) {
                    currentZ++;
                    currentX = minX;
                } else {
                    throw new NoSuchElementException();
                }
            }
            return center.getWorld().getHighestBlockAt(currentX, currentZ);
        }
    }

}
