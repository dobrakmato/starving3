package eu.matejkormuth.starving.mappings.api.files;

import eu.matejkormuth.starving.mappings.api.JsonMappingFile;
import eu.matejkormuth.starving.mappings.api.Mapping;
import eu.matejkormuth.starving.mappings.api.Type;
import org.bukkit.Material;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

public class JsonMappingFileTest {

    public static final int AMOUNT = 5;
    public static Random random = new Random();

    @Test
    @Ignore
    public void testToJson() {
        JsonMappingFile jsonMappingFile = new JsonMappingFile();
        Mapping[] arr = new Mapping[AMOUNT];
        for (int i = 0; i < AMOUNT; i++) {
            arr[i] = genMapping();
        }
        jsonMappingFile.setMappings(arr);
        System.out.println(jsonMappingFile.toJson());
    }

    private Mapping genMapping() {
        Material material = Material.values()[random.nextInt(Material.values().length)];
        String key = Integer.toHexString(random.nextInt());
        int data = random.nextInt(4);
        Type type = random.nextBoolean() ? Type.BLOCK : Type.ITEM;
        int durability = 0;
        return new Mapping(key, material, data, type, durability);
    }
}