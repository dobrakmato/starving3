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
package eu.matejkormuth.starving.items.itemmeta;

public interface KeyValueHandler {
    void set(String key, String value);

    default void set(String key, boolean value) {
        set(key, Boolean.toString(value));
    }

    default void set(String key, byte value) {
        set(key, Byte.toString(value));
    }

    default void set(String key, short value) {
        set(key, Short.toString(value));
    }

    default void set(String key, int value) {
        set(key, Integer.toString(value));
    }

    default void set(String key, long value) {
        set(key, Long.toString(value));
    }

    default void set(String key, float value) {
        set(key, Float.toString(value));
    }

    default void set(String key, double value) {
        set(key, Double.toString(value));
    }

    String get(String key);

    default byte getByte(String key) {
        return Byte.valueOf(get(key));
    }

    default short getShort(String key) {
        return Short.valueOf(get(key));
    }

    default int getInteger(String key) {
        return Integer.valueOf(get(key));
    }

    default long getLong(String key) {
        return Long.valueOf(get(key));
    }

    default float getFloat(String key) {
        return Float.valueOf(get(key));
    }

    default boolean getBoolean(String key) {
        return Boolean.valueOf(get(key));
    }

    default double getDouble(String key) {
        return Double.valueOf(get(key));
    }
}
