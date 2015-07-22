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
package eu.matejkormuth.starving.npc;

import eu.matejkormuth.starving.main.RepeatingTask;

import java.util.HashMap;
import java.util.Map;

public class NPCManager {

    private Map<String, NPCRegistry> registers;
    private NPCRegistry mainRegistry;
    private NPCProfiler profiler;
    private RepeatingTask task;

    public NPCManager() {
        this.registers = new HashMap<>();
        this.profiler = new NPCProfiler();
        this.mainRegistry = new NPCRegistry("main", this.profiler);

        this.task = new RepeatingTask() {
            @Override
            public void run() {
                NPCManager.this.updateAllRegisters();
            }
        };
        this.task.schedule(1);
    }

    private void updateAllRegisters() {
        this.mainRegistry.tick();
    }

    public NPCProfiler getProfiler() {
        return profiler;
    }

    public NPCRegistry getMainRegistry() {
        return mainRegistry;
    }

    public NPCRegistry getCustomRegistry(String name) {
        if (registers.containsKey(name)) {
            return registers.get(name);
        } else {
            return registers.put(name, new NPCRegistry(name, this.profiler));
        }
    }

    public void shutdown() {
        this.task.cancel();
    }
}
