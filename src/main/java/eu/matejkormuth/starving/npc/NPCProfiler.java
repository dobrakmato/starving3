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

import java.util.HashMap;
import java.util.Map;

public final class NPCProfiler {

    private Map<String, ProfilerEntry> entries;

    public NPCProfiler() {
        this.entries = new HashMap<>(256);
    }

    public final void start(final String key) {
        if(this.entries.containsKey(key)) {
            
        } else {
            
        }
    }

    public final void end(final String key) {
        
    }

    public static final class ProfilerEntry {
        private final String key;
        private long count;
        private double totalTime;

        public ProfilerEntry(final String key) {
            this.key = key;
        }

        public final void add(double totalTime) {
            this.totalTime += totalTime;
            this.count++;
        }

        public final long getCount() {
            return count;
        }

        public final String getKey() {
            return key;
        }

        public final double getTotalTime() {
            return totalTime;
        }

        public final double getAvarageTime() {
            return totalTime / (double) count;
        }
    }
}
