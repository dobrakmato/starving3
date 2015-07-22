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
import eu.matejkormuth.starving.worldgen.filters.base.FilterProperty;

/**
 * <p>
 * Generates grass on top of grass blocks with specified proeprties (eg. whether
 * to generate flowers).
 * </p>
 * <p>
 * Properties:
 * <ul>
 * <li>cover: 0.00 (0%) to 1.00 (100%)</li>
 * <li>longGrass: false / true</li>
 * <li>flowers: false / true</li>
 * <li>grass type: fern / grass / dead bush / all
 * <li>clear existing grass: false / true</li>
 * </ul>
 * </p>
 * 
 * @author Matej Kormuth
 */
public class GrassFilter implements Filter {

    private static final FilterProperties PROPS = new FilterProperties();
    public static final String PROPERTY_COVER = "COVER";
    public static final String PROPERTY_LONGGRASS = "LONGGRASS";
    public static final String PROPERTY_FLOWERS = "FLOWERS";
    public static final String PROPERTY_CLEAR_EXISTING_GRASS = "CLEAREXISTING";
    private static final String NAME = "GrassFilter";
    static {
        PROPS.set(new FilterProperty(PROPERTY_COVER, 0.5f));
        PROPS.set(new FilterProperty(PROPERTY_LONGGRASS, false));
        PROPS.set(new FilterProperty(PROPERTY_FLOWERS, false));
        PROPS.set(new FilterProperty(PROPERTY_CLEAR_EXISTING_GRASS, true));
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
        float chance = properties.getProperty(PROPERTY_COVER).asFloat();
        boolean longgrass = properties.getProperty(PROPERTY_LONGGRASS).asBoolean();
        boolean clearExistingGrass = properties.getProperty(
                PROPERTY_CLEAR_EXISTING_GRASS).asBoolean();
        Random r = new Random();
        for (Block b : definition) {
            // clear
            if (clearExistingGrass
                    && (b.getType() == Material.LONG_GRASS || b.getType() == Material.DOUBLE_PLANT)) {
                b.setType(Material.AIR);
            }

            if (r.nextFloat() < chance) {
                if (longgrass && r.nextBoolean()) {
                    b.setTypeIdAndData(Material.DOUBLE_PLANT.getId(), (byte) 2,
                            false);
                    b.getRelative(0, 1, 0).setTypeIdAndData(
                            Material.DOUBLE_PLANT.getId(), (byte) 10, false);
                } else {
                    b.setType(Material.LONG_GRASS);
                    if (r.nextBoolean()) {
                        b.setData((byte) 2);
                    } else {
                        b.setData((byte) 1);
                    }
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
        return property.equalsIgnoreCase(PROPERTY_COVER)
                || property.equalsIgnoreCase(PROPERTY_CLEAR_EXISTING_GRASS)
                || property.equalsIgnoreCase(PROPERTY_FLOWERS)
                || property.equalsIgnoreCase(PROPERTY_LONGGRASS);
    }

}
