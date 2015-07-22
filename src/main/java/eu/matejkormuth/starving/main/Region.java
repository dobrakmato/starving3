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
package eu.matejkormuth.starving.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

@SerializableAs("Region")
public class Region implements ConfigurationSerializable {
    static {
        ConfigurationSerialization.registerClass(Region.class);
    }

    private Vector minVector;
    private Vector maxVector;
    private World world;
    private Random random;

    public Region(Map<String, Object> serialized) {
        this.minVector = (Vector) serialized.get("minVector");
        this.maxVector = (Vector) serialized.get("maxVector");
        this.world = Bukkit.getWorld(String.valueOf(serialized.get("world")));
        this.random = new Random();
    }

    public Region(Location center, int size) {
        this.minVector = center.toVector()
                               .subtract(
                                       new Vector(size, size, size));
        this.maxVector = center.toVector()
                               .subtract(
                                       new Vector(size, size, size));
        this.world = center.getWorld();
        this.random = new Random();
    }

    private double randX() {
        return this.random.nextDouble()
                * (this.maxVector.getX() - this.minVector.getX())
                + this.minVector.getX();
    }

    private double randY() {
        return this.random.nextDouble()
                * (this.maxVector.getY() - this.minVector.getY())
                + this.minVector.getY();
    }

    private double randZ() {
        return this.random.nextDouble()
                * (this.maxVector.getZ() - this.minVector.getZ())
                + this.minVector.getZ();
    }

    public Region(Vector minVector, Vector maxVector, World world) {
        this.minVector = new Vector(
                Math.min(minVector.getX(), minVector.getX()), Math.min(
                        minVector.getY(), minVector.getY()), Math.min(
                        minVector.getZ(), minVector.getZ()));
        this.maxVector = new Vector(
                Math.max(minVector.getX(), minVector.getX()), Math.max(
                        minVector.getY(), minVector.getY()), Math.max(
                        minVector.getZ(), minVector.getZ()));
        this.world = world;
        this.random = new Random();
    }

    public World getWorld() {
        return world;
    }

    public Location getRandomLocation() {
        return new Vector(this.randX(), this.randY(), this.randZ())
                                                                   .toLocation(this.world);
    }

    public boolean isInside(Vector vector) {
        return (this.minVector.getX() < vector.getX() && vector.getX() < this.maxVector
                                                                                       .getX())
                && (this.minVector.getY() < vector.getY() && vector.getY() < this.maxVector
                                                                                           .getY())
                && (this.minVector.getZ() < vector.getZ() && vector.getZ() < this.maxVector
                                                                                           .getZ());
    }

    public void forEachBlock(BlockFunction function) {
        int maxX = this.maxVector.getBlockX();
        int maxY = this.maxVector.getBlockY();
        int maxZ = this.maxVector.getBlockZ();
        for (int x = this.minVector.getBlockX(); x <= maxX; x++) {
            for (int y = this.minVector.getBlockY(); y <= maxY; y++) {
                for (int z = this.minVector.getBlockZ(); z <= maxZ; z++) {
                    function.block(this.world.getBlockAt(x, y, z));
                }
            }
        }
    }

    public double getMaxX() {
        return this.maxVector.getX();
    }

    public double getMaxY() {
        return this.maxVector.getY();
    }

    public double getMaxZ() {
        return this.maxVector.getZ();
    }

    public double getMinX() {
        return this.minVector.getX();
    }

    public double getMinY() {
        return this.minVector.getY();
    }

    public double getMinZ() {
        return this.minVector.getZ();
    }

    public int getMaxXFloor() {
        return (int) Math.floor(this.maxVector.getX());
    }

    public int getMaxYFloor() {
        return (int) Math.floor(this.maxVector.getY());
    }

    public int getMaxZFloor() {
        return (int) Math.floor(this.maxVector.getZ());
    }

    public int getMinXFloor() {
        return (int) Math.floor(this.minVector.getX());
    }

    public int getMinYFloor() {
        return (int) Math.floor(this.minVector.getY());
    }

    public int getMinZFloor() {
        return (int) Math.floor(this.minVector.getZ());
    }

    public void forEachEntity(EntityFunction function) {
        // Get chunks.
        throw new NotImplementedException();
    }

    public static interface BlockFunction {
        void block(Block block);
    }

    public static interface EntityFunction {
        void entity(Entity entity);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<String, Object>();
        serialized.put("minVector", this.minVector);
        serialized.put("maxVector", this.maxVector);
        serialized.put("world", this.world.getName());
        return serialized;
    }

    public Vector getMaxVector() {
        return this.maxVector.clone();
    }
}
