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
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents set of frames usually called 'Clip'.
 */
@Slf4j
@Data
public class Clip implements DataSerializable {

    /**
     * Version of file format.
     */
    private static final int MAGIC_VERSION = 5;

    /**
     * Name of the clip (optional). Must be at least blank string! Can't
     * be null.
     */
    private String name = ""; // Must be at least blank! Can't be null!
    /**
     * Frames per second in this clip.
     */
    private short fps;

    /**
     * List of all frames in this clip.
     */
    @Delegate
    private List<Frame> frames = new ArrayList<>();

    @Override
    public void serialize(@Nonnull ObjectOutputStream stream) throws IOException {
        stream.write(MAGIC_VERSION);
        stream.writeUTF(name);
        stream.writeShort(fps);

        // frames
        stream.writeInt(frames.size());
        for (Frame frame : frames) {
            frame.serialize(stream);
        }
    }

    @Override
    public void deserialize(@Nonnull ObjectInputStream stream) throws IOException {
        int version;
        if ((version = stream.readInt()) != MAGIC_VERSION) {
            log.warn("Decoding from version {} to version {}!", version, MAGIC_VERSION);
        }

        name = stream.readUTF();
        fps = stream.readShort();

        // frames
        int frameCount = stream.readInt();
        frames = new ArrayList<>(frameCount);

        for (int i = 0; i < frameCount; i++) {
            Frame currentFrame = new Frame();
            currentFrame.deserialize(stream);
            frames.add(currentFrame);
        }
    }
}
