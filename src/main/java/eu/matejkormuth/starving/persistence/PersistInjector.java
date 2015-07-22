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
package eu.matejkormuth.starving.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

import org.bukkit.Location;

import eu.matejkormuth.bukkit.Worlds;

/**
 * Class that injects values to Objects or stores (saves) values from HDD.
 * 
 * @see Persist
 * @see AbstractPersistable
 * @see AbstractPersistableInstance
 */
public class PersistInjector {
    private static String confPath;

    public static void setConfigurationsFolder(Path confPath) {
        PersistInjector.confPath = confPath.toAbsolutePath().toString();
    }

    public static void store(final Object object) {
        Objects.requireNonNull(object);

        store(object, object.getClass().getName());
    }

    public static void store(final Object object, final int instancePersistId) {
        Objects.requireNonNull(object);

        store(object, object.getClass().getName() + "-" + instancePersistId);
    }

    public static void store(final Object object, final String fileName) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(fileName);

        store(object, new File(confPath + "/" + fileName + ".properties"));
    }

    public static void store(final Object object, final File file) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(file);

        Properties properties = new Properties();
        store(object, properties);
        try {
            // Open file.
            FileOutputStream fos = new FileOutputStream(file);
            properties.store(fos,
                    "Persist file of " + object.getClass().getCanonicalName()
                            + " / " + object.hashCode());
            // Close file.
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void store(final Object object, final Properties properties) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(properties);

        for (Field field : object.getClass().getDeclaredFields()) {
            // If is field persist.
            if (field.isAnnotationPresent(Persist.class)) {
                // Make annotated fields accessible.
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Persist persist = field.getAnnotation(Persist.class);
                try {
                    // Allow storing org.bukkit.Location
                    if (field.getType().equals(Location.class)) {
                        Location loc = (Location) field
                                .get(object);
                        // Put value to properties object.
                        properties.put(persist.key() + "_x",
                                String.valueOf(loc.getX()));
                        properties.put(persist.key() + "_y",
                                String.valueOf(loc.getY()));
                        properties.put(persist.key() + "_z",
                                String.valueOf(loc.getZ()));
                        properties.put(persist.key() + "_world",
                                String.valueOf(loc.getWorld().getName()));
                    } else {
                        // Put value to properties object.
                        properties.put(persist.key(),
                                String.valueOf(field.get(object)));
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void inject(final Object object) {
        Objects.requireNonNull(object);

        inject(object, object.getClass().getName());
    }

    public static void inject(final Object object, final int instancePersistId) {
        Objects.requireNonNull(object);

        inject(object, object.getClass().getName() + "-" + instancePersistId);
    }

    public static void inject(final Object object, final String fileName) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(fileName);

        inject(object, new File(confPath + "/" + fileName + ".properties"));
    }

    public static void inject(final Object object, final File file) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(file);

        try {
            // Open file.
            FileInputStream fis = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();
            // Inject using loaded properties file.
            inject(object, properties);
        } catch (IOException e) {
            // File does not exists!
            inject(object, new Properties());
        }
    }

    public static void inject(final Object object, final Properties properties) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(properties);

        for (Field field : object.getClass().getDeclaredFields()) {
            // If is field persist.
            if (field.isAnnotationPresent(Persist.class)) {
                // Make annotated fields accessible.
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Persist persist = field.getAnnotation(Persist.class);
                if (properties.containsKey(persist.key())) {
                    // Inject value from properties.
                    try {
                        // Special case for org.bukkit.Location
                        if (field.getType().equals(Location.class)) {
                            double x = Double
                                    .valueOf(
                                            properties.getProperty(persist
                                                    .key() + "_x"))
                                    .doubleValue();
                            double y = Double
                                    .valueOf(
                                            properties.getProperty(persist
                                                    .key() + "_y"))
                                    .doubleValue();
                            double z = Double
                                    .valueOf(
                                            properties.getProperty(persist
                                                    .key() + "_z"))
                                    .doubleValue();
                            String name = properties.getProperty(persist.key()
                                    + "_world", "world");
                            Location loc = new Location(
                                    Worlds.by(name), x, y, z);
                            try {
                                field.set(object, loc);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } else {
                            setField(field, object,
                                    properties.getProperty(persist.key()));
                        }
                    } catch (RuntimeException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private static void setField(final Field field, final Object object,
            final String value) {
        // Get type of field.
        Class<?> type = field.getType();
        try {
            // Set value by type.
            if (type == int.class || type == Integer.class) {
                field.set(object, Integer.valueOf(value).intValue());
            } else if (type == long.class || type == Long.class) {
                field.set(object, Long.valueOf(value).longValue());
            } else if (type == short.class || type == Short.class) {
                field.set(object, Short.valueOf(value).shortValue());
            } else if (type == byte.class || type == Byte.class) {
                field.set(object, Byte.valueOf(value).byteValue());
            } else if (type == boolean.class || type == Boolean.class) {
                field.set(object, Boolean.valueOf(value).booleanValue());
            } else if (type == String.class) {
                field.set(object, String.valueOf(value));
            } else if (type == float.class || type == Float.class) {
                field.set(object, Float.valueOf(value).floatValue());
            } else if (type == double.class || type == Double.class) {
                field.set(object, Double.valueOf(value).doubleValue());
            } else if (Enum.class.isAssignableFrom(type)) {
                Object[] consts = type.getEnumConstants();
                // Try to find right enum constant.
                for (Object _const : consts) {
                    if (_const.toString().equals(value)) {
                        field.set(object, _const);
                        // End method execution.
                        return;
                    }
                }
                throw new RuntimeException(
                        "Can't inject enum property of enum type "
                                + type.getName() + " for value '" + value + "'");
            } else {
                throw new RuntimeException("Can't inject property of type "
                        + type.getName());
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
