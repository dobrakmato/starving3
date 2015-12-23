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
package eu.matejkormuth.starving.missions;

import eu.matejkormuth.starving.filestorage.FileStorageModule;
import eu.matejkormuth.starving.items.itemmeta.KeyValueHandler;
import eu.matejkormuth.starving.missions.storage.PropertiesStorageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Bad static class with some hidden private fields.
 */
public final class StorageFactory {

    private static final Logger log = LoggerFactory.getLogger(StorageFactory.class);

    private StorageFactory() {
    }

    static FileStorageModule fileStorageModule;

    private static Path path(String key) {
        return fileStorageModule.getPath("mstorage", key);
    }

    /**
     * Loads from file or creates new storage for specified key.
     *
     * @param key key of storage
     * @return storage
     */
    public static KeyValueHandler createOrLoad(String key) {
        Path file = path(key);
        if (Files.exists(file)) {
            return load(file);
        } else {
            return create(file);
        }
    }

    private static KeyValueHandler create(Path file) {
        return new PropertiesStorageImpl(file, true);
    }

    private static KeyValueHandler load(Path file) {
        return new PropertiesStorageImpl(file, false);
    }

    /**
     * Creates new KeyValueHandler from original KeyValueHandler as sub
     * storage with specified key prefix.
     *
     * @param key      key prefix
     * @param original original storage
     * @return new storage
     */
    public static KeyValueHandler wrap(String key, KeyValueHandler original) {
        return new KeyValueHandler() {
            @Override
            public void set(String key, String value) {
                original.set(key + "." + key, value);
            }

            @Override
            public String get(String key) {
                return original.get(key + "." + key);
            }
        };
    }

    /**
     * Removes storage for specified key.
     *
     * @param key key of storage to remove
     */
    public static void clearStorage(String key) {
        try {
            Files.delete(path(key));
        } catch (IOException e) {
            log.error("Can't clear storage " + key, e);
        }
    }
}
