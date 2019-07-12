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
package eu.matejkormuth.starving.sounds.new3;

import eu.matejkormuth.bmboot.internal.Module;
import eu.matejkormuth.starving.sounds.new3.bukkit.SoundscapeControlListener;
import eu.matejkormuth.starving.sounds.new3.listeners.PlayerListener;
import eu.matejkormuth.starving.sounds.new3.listeners.WorldListener;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.WeakHashMap;

@Slf4j
public class SoundsModule extends Module {

    private final Map<Player, PlayerListener> playerListenerCache = new WeakHashMap<>();
    private final Map<World, WorldListener> worldListenerCache = new WeakHashMap<>();
    private final Map<Player, Soundscape> soundscapeCache = new WeakHashMap<>();

    @Override
    public void onEnable() {
        listener(new SoundscapeControlListener(this));
    }

    @Override
    public void onDisable() {

    }

    /**
     * Returns new or existing player listener object for specified player.
     *
     * @param player player
     * @return player listener object bound to specified player
     */
    public PlayerListener getPlayerListener(@Nonnull Player player) {
        return playerListenerCache.putIfAbsent(player, new PlayerListener(player));
    }

    /**
     * Returns new or existing world listener object for specified world.
     *
     * @param world world
     * @return world listener bound to specified world
     */
    public WorldListener getWorldListener(@Nonnull World world) {
        return worldListenerCache.putIfAbsent(world, new WorldListener(world));
    }

    /**
     * @param player
     */
    public void constructSoundscape(@Nonnull Player player) {
        if (soundscapeCache.containsKey(player)) {
            log.warn("We still have constructed sound scape for player {}?!", player.getName());
        } else {
            Soundscape soundscape = new Soundscape(player);
            soundscape.addListener(getPlayerListener(player));
            soundscapeCache.putIfAbsent(player, soundscape);
        }
    }

    /**
     * @param player
     */
    public void destructSoundscape(@Nonnull Player player) {
        if (soundscapeCache.containsKey(player)) {
            soundscapeCache.remove(player).removeListener(getPlayerListener(player));
        } else {
            log.warn("Soundscape for player {} does not exists! Nothing to destruct!");
        }
    }
}
