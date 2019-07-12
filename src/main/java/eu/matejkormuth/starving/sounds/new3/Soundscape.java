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


import eu.matejkormuth.starving.sounds.new3.emitters.RandomEmitter;
import eu.matejkormuth.starving.sounds.new3.emitters.RepeatingEmitter;
import eu.matejkormuth.starving.sounds.new3.filters.HeightFilter;
import eu.matejkormuth.starving.sounds.new3.filters.InWaterFilter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents mix of ambient sounds.
 */
public class Soundscape extends Emitter {

    /**
     * All repeating sound sources.
     */
    private final List<RepeatingEmitter> repeating = new ArrayList<>();

    /**
     * All random sound sources.
     */
    private final List<RandomEmitter> random = new ArrayList<>();

    /**
     * Player this soundscape is attached to.
     */
    private final Player player;

    public Soundscape(Player player) {
        this.player = player;
        this.build();
    }

    /**
     * Creates new repeating sound source and returns new FilterPipeline
     * to set up filters.
     *
     * @param sound sound of emitter
     * @return new filter pipeline
     */
    private FilterPipeline repeating(@Nonnull Sound sound) {
        RepeatingEmitter emitter = new RepeatingEmitter(sound);
        repeating.add(emitter);
        return setupPipeline(emitter);
    }

    /**
     * Creates new random sound source and returns new FilterPipeline
     * to set up filters.
     *
     * @param sound sound of emitter
     * @return new filter pipeline
     */
    private FilterPipeline random(@Nonnull Sound sound) {
        RandomEmitter emitter = new RandomEmitter(sound);
        random.add(emitter);
        return setupPipeline(emitter);
    }

    // Internal method for better code reuse.
    private FilterPipeline setupPipeline(Emitter emitter) {
        FilterPipeline pipeline = new FilterPipeline();
        pipeline.output(this::loopback);
        emitter.addListener(pipeline);
        return pipeline;
    }

    /**
     * Builds the soundscape. This is called only once and in object
     * initialization.
     */
    public void build() {
        repeating(new Sound("wind")).filter(new HeightFilter(player));
        repeating(new Sound("ambient.inwater")).filter(new InWaterFilter(player));
    }

    /**
     * Provides fake listener to check and fix sound events before
     * allowing them to leave the soundscape emitter.
     *
     * @param event sound event
     */
    private void loopback(@Nonnull SoundEvent event) {
        // Reset location
        event.setLocation(player.getLocation());

        // TODO: Decide on sound level.

        // Actually emit the sound event.
        emit(event);
    }
}
