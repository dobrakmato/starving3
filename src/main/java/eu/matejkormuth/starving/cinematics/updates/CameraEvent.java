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
package eu.matejkormuth.starving.cinematics.updates;

import eu.matejkormuth.starving.cinematics.Scene;
import eu.matejkormuth.starving.cinematics.SceneEvent;
import lombok.Data;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Represents update of camera position.
 */
@Data
public class CameraEvent extends SceneEvent {

    private float x;
    private float y;
    private float z;
    private float yaw;
    private float pitch;

    public CameraEvent() {
    }

    public CameraEvent(float x, float y, float z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void setPosition(@Nonnull Vector position) {
        this.x = (float) position.getX();
        this.y = (float) position.getY();
        this.z = (float) position.getZ();
    }

    public void setRotation(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Override
    public void updateScene(@Nonnull Scene scene) {
        scene.getCamera().setPosition(new Vector(x, y, z));
        scene.getCamera().setYaw(yaw);
        scene.getCamera().setPitch(pitch);

        scene.getCamera().notifyObservers();
    }

    @Override
    public void serialize(@Nonnull ObjectOutputStream stream) throws IOException {
        stream.writeFloat(x);
        stream.writeFloat(y);
        stream.writeFloat(z);
        stream.writeFloat(yaw);
        stream.writeFloat(pitch);
    }

    @Override
    public void deserialize(@Nonnull ObjectInputStream stream) throws IOException {
        x = stream.readFloat();
        y = stream.readFloat();
        z = stream.readFloat();
        yaw = stream.readFloat();
        pitch = stream.readFloat();
    }
}
