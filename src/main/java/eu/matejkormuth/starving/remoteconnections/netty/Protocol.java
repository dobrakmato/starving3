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
package eu.matejkormuth.starving.remoteconnections.netty;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class Protocol {

    private final Map<Class<? extends Packet>, Constructor<? extends Packet>> constructorsByClass;
    private final Map<Short, Constructor<? extends Packet>> constructorsByInt;
    private final Map<Class<? extends Packet>, Short> idsByClass;

    public Protocol() {
        this.constructorsByClass = new HashMap<>();
        this.constructorsByInt = new HashMap<>();
        this.idsByClass = new HashMap<>();

        registerPackets();
    }

    protected abstract void registerPackets();

    public void register(int id, Class<? extends Packet> type) {
        if (id > Short.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "id must be lower then Short.MAX_VALUE");
        }

        try {
            Constructor<? extends Packet> noArgCtr = type.getConstructor();
            if (noArgCtr.isAccessible()) {
                throw new RuntimeException(
                        "Argless contructor is not accessible!");
            }

            constructorsByClass.put(type, noArgCtr);
            constructorsByInt.put((short) id, noArgCtr);
            idsByClass.put(type, (short) id);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(
                    "All packets must declare public argless contructor!", e);
        }
    }

    public <T extends Packet> T create(Class<T> type) {
        if (this.constructorsByClass.containsKey(type)) {
            try {
                Packet packet = this.constructorsByClass.get(type)
                        .newInstance();
                return unsafeCast(packet, type);
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException("Can't create packet of type "
                        + type.getName(), e);
            }
        } else {
            throw new RuntimeException("No such packet of type "
                    + type.getName() + " is registered in this PacketFactory!");
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Packet> T create(short id) {
        if (this.constructorsByInt.containsKey(id)) {
            try {
                Packet packet = this.constructorsByInt.get(id)
                        .newInstance();
                return (T) packet;
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException("Can't create packet of id "
                        + id, e);
            }
        } else {
            throw new RuntimeException("No such packet of type "
                    + id + " is registered in this PacketFactory!");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T unsafeCast(Object o, Class<T> type) {
        return (T) o;
    }

    public int getId(Packet msg) {
        Class<?> clazz = msg.getClass();
        if (this.idsByClass.containsKey(clazz)) {
            return this.idsByClass.get(clazz);
        }
        else {
            throw new IllegalArgumentException("Packet '"
                    + msg.getClass().getName()
                    + "' is not recognized by protocol "
                    + this.getClass().getName() + "!");
        }
    }
}
