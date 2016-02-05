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
package eu.matejkormuth.starving.cinematics;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import eu.matejkormuth.starving.cinematics.updates.CameraEvent;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

@Slf4j
@UtilityClass
public final class EventRegistry {

    private static BiMap<Short, Class<? extends SceneEvent>> mapping = HashBiMap.create();

    static {
        // Register all events.
        mapping.put((short) 0, CameraEvent.class);
    }

    public static short getId(@Nonnull Class<? extends SceneEvent> clazz) {
        if (mapping.containsValue(clazz)) {
            return mapping.inverse().get(clazz);
        }
        return -1;
    }

    public static SceneEvent getInstance(short id) {
        if (mapping.containsKey(id)) {
            try {
                return mapping.get(id).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                // Be silent?
                log.error("Can't create instance of " + mapping.get(id), e);
            }
        }
        return null;
    }
}
