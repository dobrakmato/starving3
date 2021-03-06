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

import lombok.Data;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents view to scene, usually called Camera.
 */
@Data
public class Camera {

    /**
     * Scene where this camera is placed at.
     */
    private final Scene scene;

    /**
     * List of all camera position observers.
     */
    private List<CameraObserver> observers = new ArrayList<>();

    /**
     * World tha camera is in. Does not change during the scene.
     */
    private final World world;

    /**
     * Current position of camera.
     */
    private Vector position;

    /**
     * Current yaw of camera.
     */
    private float yaw;

    /**
     * Current pitch of camera.
     */
    private float pitch;

    /**
     * Notifies all observers about camera update.
     */
    public void notifyObservers() {
        for (CameraObserver observer : observers) {
            observer.notify(this);
        }
    }

    public boolean addObserver(@Nonnull CameraObserver cameraObserver) {
        return observers.add(cameraObserver);
    }

    public int observerCount() {
        return observers.size();
    }

    public boolean isEmpty() {
        return observers.isEmpty();
    }

    public CameraObserver getObserver(int index) {
        return observers.get(index);
    }

    public void clearObservers() {
        observers.clear();
    }

    public boolean removeObserver(@Nonnull CameraObserver o) {
        return observers.remove(o);
    }

    /**
     * Disallowed.
     *
     * @deprecated do not use. thanks!
     * @param world world to set
     * @throws UnsupportedOperationException always
     */
    @Deprecated
    public void setWorld(World world) {
        throw new UnsupportedOperationException();
    }
}
