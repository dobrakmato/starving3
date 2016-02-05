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
package eu.matejkormuth.starving.cinematics;

import eu.matejkormuth.callbacks.CallbackList;
import eu.matejkormuth.starving.main.RepeatingTask;
import eu.matejkormuth.starving.main.Time;
import lombok.Data;
import lombok.experimental.Delegate;
import org.bukkit.Bukkit;
import org.bukkit.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for update mechanism of scene and clip.
 */
@Data
public class ClipPlayer {

    // Represents tick source.
    private static SyncGenerator sync = new SyncGenerator();

    private static class SyncGenerator extends RepeatingTask {
        private static final int FREQUENCY = 20;
        @Delegate
        private final List<ClipPlayer> targets = new ArrayList<>();

        // Schedule to execute each tick.
        {
            this.schedule(Time.EACH_TICK);
        }

        @Override
        public void run() {
            List<ClipPlayer> view = targets.subList(0, targets.size() == 0 ? 0 : targets.size() - 1);
            for(ClipPlayer player : view) {
                player.tick();
            }
        }
    }

    /**
     * Currently played clip.
     */
    private final Clip clip;

    /**
     * Scene for currently played clip.
     */
    private final Scene scene;

    /**
     * Callbacks called on clip start.
     */
    private final CallbackList<Void> callbacksOnStart = new CallbackList<>();
    /**
     * Callbacks called on clip end.
     */
    private final CallbackList<Void> callbacksOnEnd = new CallbackList<>();

    private int ticksPerFrame = 0; //clip.getFps() / SyncGenerator.FREQUENCY;
    private int ticksSinceFrame = 0;
    private int currentFrame = 0;
    private boolean playing = false;

    public ClipPlayer(@Nonnull Clip clip, @Nonnull World world) {
        this.clip = clip;
        this.scene = new Scene(world);
        this.ticksPerFrame = clip.getFps() / SyncGenerator.FREQUENCY;

        Bukkit.broadcastMessage("Created ClipPlayer!");
        Bukkit.broadcastMessage("Clip: " + clip.getName());
        Bukkit.broadcastMessage("Fps: " + clip.getFps());
        Bukkit.broadcastMessage("Frames: " + clip.getFrames().size());
    }

    private void tick() {
        ticksSinceFrame++;
        if (ticksSinceFrame >= ticksPerFrame) {
            if (currentFrame != clip.size()) {
                nextFrame();
            } else {
                // End of clip.
                stop();
            }
        }
    }

    private void nextFrame() {
        Frame frame = clip.get(currentFrame);

        // Perform all updates.
        for (SceneEvent event : frame.getEvents()) {
            event.updateScene(this.scene);
        }

        currentFrame++;
    }

    /**
     * Starts playing the clip if not playing already.
     */
    public void play() {
        if (playing) {
            return;
        }
        playing = true;

        // Register to sync.
        sync.add(this);

        // Fire all callbacks.
        callbacksOnStart.call(null);
        Bukkit.broadcastMessage("Player playing.");
    }

    /**
     * Stops clips if not stopped already.
     */
    public void stop() {
        if (!playing) {
            return;
        }
        playing = false;

        // First all callbacks.
        callbacksOnEnd.call(null);

        // Unregister from sync.
        sync.remove(this);

        Bukkit.broadcastMessage("Player stopping.");
    }

    /**
     * Resets clip play position.
     */
    public void reset() {
        stop();

        currentFrame = 0;
    }

}
