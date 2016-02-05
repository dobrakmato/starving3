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

import eu.matejkormuth.callbacks.Callback;
import org.bukkit.World;

public class ClipPlayerBuilder {

    private Clip clip;
    private ClipPlayer player;

    private ClipPlayerBuilder(Clip clip) {
        this.clip = clip;
    }

    public static ClipPlayerBuilder use(Clip clip) {
        return new ClipPlayerBuilder(clip);
    }

    public ClipPlayerBuilder world(World world) {
        player = new ClipPlayer(clip, world);
        return this;
    }

    public ClipPlayerBuilder observer(CameraObserver cameraObserver) {
        player.getScene().getCamera().addObserver(cameraObserver);
        return this;
    }

    public ClipPlayerBuilder observers(CameraObserver... cameraObservers) {
        for (CameraObserver co : cameraObservers) {
            player.getScene().getCamera().addObserver(co);
        }
        return this;
    }

    public ClipPlayerBuilder onStart(Callback<Void> callback) {
        player.getCallbacksOnStart().add(callback);
        return this;
    }

    public ClipPlayerBuilder onEnd(Callback<Void> callback) {
        player.getCallbacksOnEnd().add(callback);
        return this;
    }

    public ClipPlayerBuilder play() {
        player.play();
        return this;
    }

    public ClipPlayerBuilder stop() {
        player.stop();
        return this;
    }

    public ClipPlayerBuilder reset() {
        player.reset();
        return this;
    }

    public ClipPlayer player() {
        return player;
    }
}
