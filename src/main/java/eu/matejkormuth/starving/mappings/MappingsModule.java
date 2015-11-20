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
