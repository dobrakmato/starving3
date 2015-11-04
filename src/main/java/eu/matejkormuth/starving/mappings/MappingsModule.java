package eu.matejkormuth.starving.mappings;

import eu.matejkormuth.bmboot.Dependency;
import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.filestorage.FileStorageModule;
import eu.matejkormuth.starving.mappings.api.JsonMappingFile;
import eu.matejkormuth.starving.mappings.api.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MappingsModule extends Module {

    private static final Logger log = LoggerFactory.getLogger(MappingsModule.class);

    @Dependency
    private FileStorageModule fileStorageModule;

    private Map<String, Mapping> mappings = new HashMap<>();

    @Override
    public void onEnable() {
        log.info("Loading mappings from mappings.json...");

        Path mappingsJson = fileStorageModule.getPath("mappings.json");
        String json;
        try {
            json = new String(Files.readAllBytes(mappingsJson), Charset.forName("UTF-8"));
        } catch (IOException e) {
            log.info("Can't load mappings.json!", e);
            return;
        }
        JsonMappingFile file = JsonMappingFile.fromJson(json);

        Arrays.stream(file.getMappings())
                .forEach(mapping -> mappings.put(mapping.getKey(), mapping));

        log.info("Loaded {} different mappings!", mappings.size());
    }

    @Override
    public void onDisable() {

    }

    public Mapping getMapping(String key) {
        return mappings.get(key);
    }
}
