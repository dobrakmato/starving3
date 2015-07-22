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

import eu.matejkormuth.starving.main.Region;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class RegionAffectedBlocksDef implements AffectedBlocksDefinition {

    private Region region;

    public RegionAffectedBlocksDef(Region region) {
        this.region = region;
    }

    @Override
    public Location getCenter() {
        return null;
    }

    @Override
    public boolean hasCenter() {
        return false;
    }

    @Override
    public Block[] getAffectedBlocks() {
        throw new UnsupportedOperationException("please use iterator");
    }

    @Override
    public boolean isAffected(Block block) {
        return this.region.isInside(block.getLocation()
                                         .toVector());
    }

    @Override
    public boolean isFullHeight() {
        return true;
    }

    @Override
    public RegionAffectedBlocksIterator iterator() {
        return new RegionAffectedBlocksIterator();
    }

    public HighestBlockRegionAffectedBlocksIterator highestBlocksIterator() {
        return new HighestBlockRegionAffectedBlocksIterator();
    }

    public class RegionAffectedBlocksIterator implements
            AffectedBlocksIterator {

        protected int currentX = region.getMinXFloor();
        protected int currentY = region.getMinYFloor();
        protected int currentZ = region.getMinZFloor();

        @Override
        public boolean hasNext() {
            return !(region.getMaxXFloor() == currentX
                    && region.getMaxYFloor() == currentY
                    && region.getMaxZFloor() == currentZ);
        }

        @Override
        public Block next() {
            updateCurrentBlock();
            return region
                         .getWorld()
                         .getBlockAt(currentX, currentY, currentZ);
        }

        protected void updateCurrentBlock() {
            if (currentX != region.getMaxXFloor()) {
                currentX++;
            } else {
                if (currentZ != region.getMaxZFloor()) {
                    currentZ++;
                    currentX = region.getMinXFloor();
                } else {
                    if (currentY != region.getMaxYFloor()) {
                        currentY++;
                        currentX = region.getMinXFloor();
                        currentZ = region.getMinZFloor();
                    } else {
                        throw new NoSuchElementException("Region ends at "
                                + region.getMaxVector()
                                        .toString() + " and current pos is: ["
                                + currentX + ", " + currentY + ", " + currentZ
                                + "]");
                    }
                }
            }
        }
    }

    public final class HighestBlockRegionAffectedBlocksIterator extends
            RegionAffectedBlocksIterator {
        @Override
        public Block next() {
            updateCurrentBlock();
            return region
                         .getWorld()
                         .getHighestBlockAt(currentX, currentZ);
        }

        @Override
        protected void updateCurrentBlock() {
            if (currentX != region.getMaxXFloor()) {
                currentX++;
            } else {
                if (currentZ != region.getMaxZFloor()) {
                    currentZ++;
                    currentX = region.getMinXFloor();
                } else {
                    throw new NoSuchElementException("Region ends at "
                            + region.getMaxVector()
                                    .toString() + " and current pos is: ["
                            + currentX + ", " + currentY + ", " + currentZ
                            + "]");
                }
            }
        }
    }

}
