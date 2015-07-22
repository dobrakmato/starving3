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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import eu.matejkormuth.starving.npc.behaviours.base.AbstractBehaviour;
import eu.matejkormuth.starving.npc.types.HumanNPC;
import org.bukkit.Location;

import com.mojang.authlib.GameProfile;

public class NPCRegistry {

    private final Map<Integer, NPC> entityByIds;
    private final String name;
    public final NPCProfiler profiler;

    public NPCRegistry(String name, NPCProfiler profiler) {
        this.entityByIds = new HashMap<>();
        this.name = name;
        this.profiler = profiler;
    }

    public String getName() {
        return name;
    }

    public NPC getNPC(int entityId) {
        return entityByIds.get(entityId);
    }

    public PlayerNPCBuilder createPlayer() {
        return new PlayerNPCBuilder();
    }

    protected void addNPC(NPC npc) {
        this.entityByIds.put(npc.getId(), npc);
    }

    protected void removeNPC(NPC npc) {
        this.entityByIds.remove(npc.getId());
    }

    public void tick() {
        for (NPC npc : entityByIds.values()) {
            npc.tickEntity();
        }
    }

    public class PlayerNPCBuilder {
        protected List<AbstractBehaviour> behaviours;
        protected Location spawn;
        private GameProfile profile;

        public PlayerNPCBuilder() {
            behaviours = new ArrayList<>();
        }

        public PlayerNPCBuilder withBehaviour(AbstractBehaviour behaviour) {
            behaviours.add(behaviour);
            return this;
        }

        public PlayerNPCBuilder withSpawnLocation(Location spawnLocation) {
            this.spawn = spawnLocation;
            return this;
        }

        public PlayerNPCBuilder withProfile(UUID uuid, String name) {
            this.profile = new GameProfile(uuid, name);
            return this;
        }

        public PlayerNPCBuilder withProfile(String name) {
            this.profile = new GameProfile(UUID.randomUUID(), name);
            return this;
        }

        public NPC spawn() {
            if (this.spawn == null) {
                throw new NullPointerException(
                        "Spawn location can't be null. You must set spawn location.");
            }
            return buildPlayerNpc();

        }

        private NPC buildPlayerNpc() {
            HumanNPC npc = new HumanNPC(NPCRegistry.this, this.profile, this.spawn);
            NPCRegistry.this.addNPC(npc);
            return npc;
        }
    }

}
