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
package eu.matejkormuth.starving.sounds.ambient;

import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.nms.NMS;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class AmbientSoundManager {
    private Map<Player, PlayerChannel> channels;

    // Hardcoded atmospheres.
    private RandomSound[] NO_RANDOM = new RandomSound[]{};

    // Reference to nms.
    private final NMS nms;

    // Atmospheres.
    private Atmosphere WOODS;
    private Atmosphere CRICKETS;
    private Atmosphere CAVE;
    private Atmosphere INTERIOR;

    public AmbientSoundManager(NMS nms) {
        this.nms = nms;
        this.channels = new WeakHashMap<>();

        // Init atmospheres.
        WOODS = new Atmosphere("base", 9000, new RepeatingSound[]{
                new RepeatingSound("ambient.birdsonly")}, NO_RANDOM);
        CRICKETS = new Atmosphere("crickets", 12750, new RepeatingSound[]{
                new RepeatingSound("ambient.crickets")}, NO_RANDOM);
        CAVE = new Atmosphere("cave", 9000, new RepeatingSound[]{
                new RepeatingSound("ambient.cave")}, NO_RANDOM);
        INTERIOR = new Atmosphere("interior", 5000, new RepeatingSound[]{
                new RepeatingSound("ambient.sum")}, NO_RANDOM);

        // Schedule tasks.
        RepeatingTask.of(this::updateChannels).schedule(1L);
        RepeatingTask.of(this::updateAtmospheres).schedule(20L);
    }

    public void updateChannels() {
        for (PlayerChannel channel : this.channels.values()) {
            try {
                channel.update();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addPlayer(Player player) {
        PlayerChannel ch = new PlayerChannel(player);
        this.channels.put(player, ch);
        ch.setAtmosphere(determinateAtmosphere(player));
    }

    public void removePlayer(Player player) {
        this.channels.remove(player);
    }

    public void updateAtmospheres() {
        for (Entry<Player, PlayerChannel> p : channels.entrySet()) {
            Atmosphere a = determinateAtmosphere(p.getKey());
            if (p.getValue().getAtmosphere() != a) {
                p.getValue().setAtmosphere(a);
            }
        }
    }

    private Atmosphere determinateAtmosphere(Player key) {
        Block b = key.getWorld().getHighestBlockAt(key.getLocation());
        if (b.getLocation().getY() > key.getEyeLocation().getY()) {
            if (key.getLocation().getY() < 50) {
                return CAVE;
            } else {
                return INTERIOR;
            }
        } else {
            if (key.getWorld().getTime() > 12000) {
                return CRICKETS;
            } else {
                return WOODS;
            }
        }
    }

    public void clear() {
        this.channels.clear();
    }
}
