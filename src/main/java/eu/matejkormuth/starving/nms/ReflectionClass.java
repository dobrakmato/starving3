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
package eu.matejkormuth.starving.nms;

import eu.matejkormuth.starving.npc.K;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ReflectionClass<T> {

    private static final Map<Class<?>, ReflectionClass<?>> classCache = new HashMap<>();

    private static final ReflectionField NULL_FIELD = new ReflectionField(null, null) {
        @Override
        public Object get(K object) {
            log.error("Can't get value from {}, this is null field!", object);
            return null;
        }

        @Override
        public void set(K object, Object value) {
            log.error("Can't set {} on {}, this is null field!", value, object);
        }
    };

    private final Map<String, ReflectionField> fieldCache = new HashMap<>();
    //private final Map<Class<?>[], ReflectionConstructor> constructorCache = new HashMap<>();
    private final Class<T> clazz;

    private ReflectionClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> ReflectionClass<T> of(Class<T> clazz) {
        if (classCache.containsKey(clazz)) {
            return (ReflectionClass<T>) classCache.get(clazz);
        } else {
            ReflectionClass<T> refl = new ReflectionClass(clazz);
            classCache.put(clazz, refl);
            return refl;
        }
    }

    public ReflectionField getField(@Nonnull String name) {
        return fieldCache.putIfAbsent(name, getField0(name));
    }

    private ReflectionField getField0(String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            return new ReflectionField(this, field);
        } catch (NoSuchFieldException e) {
            return NULL_FIELD;
        }
    }

    //public ReflectionConstructor getConstructor(Class<?>... params) {
    //    if (constructorCache.containsKey(params)) {
    //        return constructorCache.get(params);
    //    } else {
    //
    //    }
    //}
}
