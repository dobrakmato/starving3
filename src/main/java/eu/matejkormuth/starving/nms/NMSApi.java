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

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;

@Slf4j
@UtilityClass
public final class NMSApi {

    private static final String VERSION;
    private static final String NMS_PACKAGE;
    private static final String CB_PACKAGE;

    static {
        log.info("Initializing NMSApi...");

        String server = Bukkit.getServer().getClass().getName(); // org.bukkit.craftbukkit.v1_8_R3.CraftServer
        int versionStart = server.indexOf("craftbukkit.") + 12;
        int versionEnd = server.indexOf("CraftServer") - 1;
        VERSION = server.substring(versionStart, versionEnd);

        // Derive other packages.
        CB_PACKAGE = "org.bukkit.craftbukkit." + VERSION;
        NMS_PACKAGE = "net.minecraft.server." + VERSION;

        log.info("Detected version {}", VERSION);
        log.info("CB Package: {}", CB_PACKAGE);
        log.info("NMS Package: {}", NMS_PACKAGE);
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName(NMS_PACKAGE + "." + name);
        } catch (ClassNotFoundException e) {
            log.error("No class for name {}", NMS_PACKAGE + "." + name);
            return null;
        }
    }

    public static Class<?> getCBClass(String name) {
        try {
            return Class.forName(CB_PACKAGE + "." + name);
        } catch (ClassNotFoundException e) {
            log.error("No class for name {}", CB_PACKAGE + "." + name);
            return null;
        }
    }

    @UtilityClass
    public static class Packets {
        public static Object createResourcePackPacket(String url, String sha1lower40chars) {
            return ReflectionClass
                    .of(getPacketClass("PacketPlayOutResourcePackSend"));
                    //.getConstructor(String.class, String.class)

        }

        private static Class<?> getPacketClass(String className) {
            return NMSApi.getNMSClass(className);
        }
    }

    public static void sendPacket(@NonNull Object packet) {

    }
}
