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
package eu.matejkormuth.starving.cinematics.v4;

import eu.matejkormuth.starving.cinematics.Clip;
import eu.matejkormuth.starving.cinematics.Frame;
import eu.matejkormuth.starving.cinematics.v4.streams.V4InputStream;
import eu.matejkormuth.starving.cinematics.v4.streams.V4OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class V4Clip implements Clip, V4Serializable {
    private int frameRate = 20;
    private List<V4Frame> frames;

    public V4Clip() {
        this.frames = new ArrayList<>();
    }

    @Override
    public void addFrame(Frame frame) {
        if (frame instanceof V4Frame) {
            this.frames.add((V4Frame) frame);
        }

        throw new RuntimeException(
                "Specified frame is not compatibile with V4Clip.");
    }

    @Override
    public Frame getFrame(int index) {
        return this.frames.get(index);
    }

    @Override
    public int getFrameRate() {
        return this.frameRate;
    }

    @Override
    public int getLength() {
        return this.frames.size();
    }

    @Override
    public void writeTo(V4OutputStream os) throws IOException {
        // Write frame rate.
        os.writeInt(this.frameRate);
        // Write frame count.
        os.writeShort(this.frames.size());
        // Write frames.
        for (V4Frame frame : this.frames) {
            frame.writeTo(os);
        }
    }

    @Override
    public void readFrom(V4InputStream os) throws IOException {
        // Read frame rate.
        this.frameRate = os.readInt();
        // Read frame count.
        short frameCount = os.readShort();
        this.frames = new ArrayList<V4Frame>(frameCount);
        // Read frames.
        for (int i = 0; i < frameCount; i++) {
            V4Frame frame = new V4Frame();
            frame.readFrom(os);
            this.frames.add(frame);
        }
    }

}
