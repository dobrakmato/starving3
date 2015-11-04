package eu.matejkormuth.starving.mappings.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JsonMappingFile {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Mapping[] mappings;

    public JsonMappingFile() {
    }

    public Mapping[] getMappings() {
        return mappings;
    }

    public void setMappings(Mapping[] mappings) {
        this.mappings = mappings;
    }

    public static JsonMappingFile fromJson(String json) {
        return gson.fromJson(json, JsonMappingFile.class);
    }

    public String toJson() {
        return gson.toJson(this);
    }
}
