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
package eu.matejkormuth.starving.worldgen;

import eu.matejkormuth.bukkit.Worlds;
import eu.matejkormuth.starving.worldgen.accessors.BukkitWorldAccessor;
import eu.matejkormuth.starving.worldgen.filters.FieldFilter;
import eu.matejkormuth.starving.worldgen.filters.GrassFilter;
import eu.matejkormuth.starving.worldgen.filters.base.Filter;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class WorldGenManager {

    private Map<Player, PlayerSession> sessions;
    private Map<World, WorldAccessor> worlds;
    private Map<String, Filter> filters;

    private Constructor<? extends WorldAccessor> preferedAccessorCtr;

    public WorldGenManager() {
        sessions = new WeakHashMap<>();
        worlds = new WeakHashMap<>();
        filters = new HashMap<>();

        // Determinate ideal accessor for this server.
        determinateAccessor();

        // Register known filters.
        registerFilters();
    }

    private void registerFilters() {
        System.out.println("Registering filters...");
        registerFilter(new FieldFilter());
        registerFilter(new GrassFilter());
    }

    private void determinateAccessor() {
        Class<? extends WorldAccessor> clazz = BukkitWorldAccessor.class;
        try {
            preferedAccessorCtr = clazz.getConstructor(World.class);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private WorldAccessor createAccessor(World world) {
        try {
            return preferedAccessorCtr.newInstance(new Object[]{world});
        } catch (InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerSession getSession(Player player) {
        if (!sessions.containsKey(player)) {
            sessions.put(player, new PlayerSession(player));
        }

        return this.sessions.get(player);
    }

    public WorldAccessor getWorld(String name) {
        return getWorld(Worlds.by(name));
    }

    public WorldAccessor getWorld(World world) {
        if (!worlds.containsKey(world)) {
            worlds.put(world, createAccessor(world));
        }

        return worlds.get(world);
    }

    public Collection<Filter> getFilters() {
        return this.filters.values();
    }

    public void registerFilter(Filter filter) {
        System.out.println("Registerting filter: " + filter.getName() + "; "
                + filter.getClass().getName());
        this.filters.put(filter.getName(), filter);
    }

    public Filter getFilter(String name) {
        return this.filters.get(name);
    }

}
