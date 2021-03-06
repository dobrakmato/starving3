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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents set of scene events that occurs at discrete step of time, usually
 * called a Frame.
 */
@Data
public class Frame implements DataSerializable {

    /**
     * Maximum amount of scene events per one frame.
     */
    private final int MAX_EVENTS = Short.MAX_VALUE;

    /**
     * List of all scene events that occur in this frame.
     */
    @Delegate
    private List<SceneEvent> events = new ArrayList<>();

    @Override
    public void serialize(@Nonnull ObjectOutputStream stream) throws IOException {
        if (events.size() > MAX_EVENTS) {
            throw new IOException("This frame has more events (" + events.size() + ") than maximum (32767)!");
        }

        // Write amount of scene events.
        stream.writeShort(events.size());

        // Write each scene event.
        for (SceneEvent event : events) {
            short type = EventRegistry.getId(event.getClass());

            if (type == -1) {
                throw new RuntimeException("Can't find ID for class " + event.getClass());
            }

            stream.writeShort(type);
            event.serialize(stream);
        }
    }

    @Override
    public void deserialize(@Nonnull ObjectInputStream stream) throws IOException {
        // Read amount of scene events.
        short eventCount = stream.readShort();
        events = new ArrayList<>(eventCount);

        // Read all scene events.
        for (int i = 0; i < eventCount; i++) {
            short type = stream.readShort();
            SceneEvent event = EventRegistry.getInstance(type);

            if (event == null) {
                throw new RuntimeException("Can't find class for ID " + type);
            }

            event.deserialize(stream);
        }
    }
}
