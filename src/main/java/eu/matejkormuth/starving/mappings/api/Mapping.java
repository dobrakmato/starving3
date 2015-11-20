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
package eu.matejkormuth.starving.mappings.api;

import com.google.gson.annotations.SerializedName;
import org.bukkit.Material;

public class Mapping {

    /**
     * Mapping key (unique id of this mapping).
     */
    private String key;

    /**
     * Material part (Minecraft item id) of specific mapping.
     */
    @SerializedName("material_value")
    private Material material;

    /**
     * Data value part of specified mapping.
     */
    @SerializedName("data_value")
    private int dataValue;

    /**
     * Minecraft 1.9 should support different models for different durability
     * values of items.
     */
    @SerializedName("durability_value")
    private int durability;

    /**
     * Type of mapping (block, item...). This is here to support different
     * models for same type and data pair.
     */
    private Type type;

    public Mapping() {
    }

    public Mapping(String key, Material material, int dataValue, Type type, int durability) {
        this.key = key;
        this.material = material;
        this.dataValue = dataValue;
        this.type = type;
        this.durability = durability;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getDataValue() {
        return dataValue;
    }

    public void setDataValue(int dataValue) {
        this.dataValue = dataValue;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mapping mapping = (Mapping) o;

        if (dataValue != mapping.dataValue) return false;
        if (durability != mapping.durability) return false;
        if (key != null ? !key.equals(mapping.key) : mapping.key != null) return false;
        if (material != mapping.material) return false;
        return type == mapping.type;

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (material != null ? material.hashCode() : 0);
        result = 31 * result + dataValue;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + durability;
        return result;
    }

    @Override
    public String toString() {
        return "Mapping{" +
                "key='" + key + '\'' +
                ", material=" + material +
                ", dataValue=" + dataValue +
                ", type=" + type +
                ", durability=" + durability +
                '}';
    }
}
