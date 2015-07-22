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
package eu.matejkormuth.starving.worldgen.accessors;

import org.bukkit.Material;
import org.bukkit.World;

import eu.matejkormuth.starving.worldgen.WorldAccessor;

public class BukkitWorldAccessor extends WorldAccessor {

    private World w;

    public BukkitWorldAccessor(World world) {
        super(world);
        this.w = world;
    }

    @Override
    public Material getMaterialAt(int x, int y, int z) {
        return w.getBlockAt(x, y, z).getType();
    }

    @SuppressWarnings("deprecation")
    @Override
    public byte getDataAt(int x, int y, int z) {
        return w.getBlockAt(x, y, z).getData();
    }

    @Override
    public void setMaterialAt(int x, int y, int z, Material material) {
        w.getBlockAt(x, y, z).setType(material);
    }

    @Override
    public void setMaterialAt(int x, int y, int z, Material material,
            boolean applyPhysics) {
        w.getBlockAt(x, y, z).setType(material, applyPhysics);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setDataAt(int x, int y, int z, byte data) {
        w.getBlockAt(x, y, z).setData(data);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setDataAt(int x, int y, int z, byte data, boolean applyPhysics) {
        w.getBlockAt(x, y, z).setData(data, applyPhysics);
    }

    @Override
    public int getHighestBlockAt(int x, int z) {
        return w.getHighestBlockYAt(x, z);
    }

}
