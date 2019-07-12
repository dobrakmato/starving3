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
package eu.matejkormuth.starving.sounds.new3.filters;

import eu.matejkormuth.starving.main.NMSHooks;
import eu.matejkormuth.starving.sounds.new3.Filter;
import eu.matejkormuth.starving.sounds.new3.SoundEvent;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class HeightFilter implements Filter {

    /**
     * Maximum height in Minecraft.
     */
    public static final float MINECRAFT_MAX_HEIGHT = 256f;

    /**
     * Optimized method to get player's height without allocating
     * new objects.
     */
    @NMSHooks(version = "v1_8_R3")
    private class NMSHeightAccessor {
        private double getY() {
            return ((CraftPlayer) player).getHandle().locY;
        }
    }

    private final Player player;
    private final NMSHeightAccessor accessor;

    public HeightFilter(@Nonnull Player player) {
        this.player = player;
        this.accessor = new NMSHeightAccessor();
    }

    @Override
    public void apply(@Nonnull SoundEvent event) {
        // For now just linear attenuation.
        event.setVolume((float) (accessor.getY() / MINECRAFT_MAX_HEIGHT));
    }
}
