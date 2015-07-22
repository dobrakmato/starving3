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

import eu.matejkormuth.starving.cinematics.*;

import java.nio.file.Path;

public class V4Cinematics extends Cinematics {

    private static final String IMPLEMENTATION_NAME = "V4";
    private PlayerServer server;

    public V4Cinematics() {
        this.server = new V4ClipPlayerServer();
    }

    @Override
    public String getImplementationName() {
        return IMPLEMENTATION_NAME;
    }

    @Override
    public Clip createClip() {
        return new V4Clip();
    }

    @Override
    public ClipPlayer createPlayer(Clip clip) {
        return new V4ClipPlayer((V4Clip) clip);
    }

    @Override
    public Frame createFrame() {
        return new V4Frame();
    }

    @Override
    public PlayerServer getServer() {
        return this.server;
    }

    @Override
    public Clip loadClip(Path file) {
        return V4Writer.load(file.toFile());
    }

    @Override
    public void saveClip(Clip clip, Path file) {
        if (clip instanceof V4Clip) {
            V4Writer.save((V4Clip) clip, file.toFile());
        }
    }

}
