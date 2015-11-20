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
package eu.matejkormuth.starving.cinematics.v4;

import eu.matejkormuth.starving.cinematics.FrameAction;
import eu.matejkormuth.starving.cinematics.v4.frameactions.AbstractAction;
import eu.matejkormuth.starving.cinematics.v4.frameactions.V4CameraLocationAction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


public class V4ActionRegistry {

    private static Map<Byte, Constructor<? extends AbstractAction>> actions;
    private static Map<Class<? extends AbstractAction>, Byte> classes;

    public V4ActionRegistry() {
        actions = new HashMap<>();
        classes = new HashMap<>();
        register();
    }

    private static void register() {
        // Register all V4 frame actions.
        add(0, V4CameraLocationAction.class);
    }

    public static void add(int type, Class<? extends AbstractAction> action) {
        try {
            Constructor<? extends AbstractAction> ctr = action.getConstructor();
            actions.put((byte) type, ctr);
            classes.put(action, (byte) type);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static AbstractAction createAction(byte type) {
        Constructor<? extends AbstractAction> ctr = actions.get(type);
        if (ctr != null) {
            try {
                AbstractAction aa = ctr.newInstance();
                return aa;
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Unsupported action type.");
        }
    }

    public static byte getType(FrameAction fa) {
        return classes.get(fa.getClass());
    }
}
