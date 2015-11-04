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
     * Type of mapping (block, item...). This is here to support different
     * models for same type and data pair.
     */
    private Type type;

    /**
     * Minecraft 1.9 should support different models for different durability
     * values of items.
     */
    private int durability;

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
