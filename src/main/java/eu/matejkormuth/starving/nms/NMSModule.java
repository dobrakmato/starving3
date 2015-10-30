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

import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.main.NMSHooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NMSModule extends Module {

    private static final Logger log = LoggerFactory.getLogger(NMSModule.class);
    private NMS nms;

    @Override
    public void onEnable() {
        // Check for compatibility.
        checkCompatible();

        // Create NMS.
        nms = new NMS();
    }

    private void checkCompatible() {
        // Get version of NMS.
        String nmsVersion = NMS.class.getAnnotation(NMSHooks.class).version();
        try {
            // Try to find MinecraftServer class.
            Class.forName("net.minecraft.server." + nmsVersion + ".MinecraftServer");
        } catch (Exception ex) {
            log.error("======================================");
            log.error("MinecraftServer class of version {} couldn't be found!", nmsVersion);
            log.error("This version of plugin is probably incompatible with this Minecraft version.");
            log.error("Please downgrade to {} or upgrade Starving plugin to match version of your server.", nmsVersion);
            log.error("======================================");
            // Throw new exception.
            throw new UnsupportedOperationException("Can't work with NMS " + nmsVersion);
        }
    }

    @Override
    public void onDisable() {

    }

    public NMS getNms() {
        return nms;
    }
}
