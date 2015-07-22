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
package eu.matejkormuth.starving.worldgen.filters;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;

import eu.matejkormuth.starving.worldgen.affectedblocks.AffectedBlocksDefinition;
import eu.matejkormuth.starving.worldgen.filters.base.Filter;
import eu.matejkormuth.starving.worldgen.filters.base.FilterProperties;

public class FieldFilter implements Filter {

    private static final FilterProperties PROPS = new FilterProperties();
    private static final String NAME = "FieldFilter";
    static {
        PROPS.lock();
    }

    @Override
    public FilterProperties getDefaultProperties() {
        return PROPS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void apply(AffectedBlocksDefinition definition,
            FilterProperties properties) {
        Random r = new Random();
        for (Block b : definition) {
            b.setTypeIdAndData(0, (byte) 0, false);
            b.getRelative(0, 1, 0).setTypeIdAndData(0, (byte) 0, false);

            if (r.nextFloat() < 0.1f) {
                b.getRelative(0, -1, 0).setType(Material.DIRT);

                if (r.nextBoolean()) {
                    b.setTypeIdAndData(Material.LONG_GRASS.getId(),
                            (byte) r.nextInt(2),
                            false);
                }

            } else {
                b.getRelative(0, -1, 0).setType(Material.SOIL);

                switch (r.nextInt(7)) {
                    case 0:
                        b.setTypeIdAndData(Material.CROPS.getId(),
                                (byte) r.nextInt(7),
                                false);
                        break;
                    case 1:
                        b.setTypeIdAndData(Material.PUMPKIN_STEM.getId(),
                                (byte) r.nextInt(7),
                                false);
                        break;
                    case 2:
                        b.setTypeIdAndData(Material.MELON_STEM.getId(),
                                (byte) r.nextInt(7),
                                false);
                        break;
                    case 3:
                        b.setTypeIdAndData(Material.POTATO.getId(),
                                (byte) r.nextInt(7),
                                false);
                        break;
                    case 4:
                        b.setTypeIdAndData(Material.CARROT.getId(),
                                (byte) r.nextInt(7),
                                false);
                        break;
                    case 5:
                        b.setTypeIdAndData(Material.LONG_GRASS.getId(),
                                (byte) r.nextInt(2),
                                false);
                        break;
                    case 6:
                        b.setTypeIdAndData(Material.DOUBLE_PLANT.getId(),
                                (byte) 2,
                                false);
                        b.getRelative(0, 1, 0).setTypeIdAndData(
                                Material.DOUBLE_PLANT.getId(),
                                (byte) 10,
                                false);
                        break;
                }
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isPropertySupported(String property) {
        return false;
    }

}
