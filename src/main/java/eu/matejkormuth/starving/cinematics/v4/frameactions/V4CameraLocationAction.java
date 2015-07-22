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
package eu.matejkormuth.starving.cinematics.v4.frameactions;

import java.io.IOException;

import eu.matejkormuth.starving.cinematics.frameactions.CameraLocationAction;
import eu.matejkormuth.starving.cinematics.v4.streams.V4InputStream;
import eu.matejkormuth.starving.cinematics.v4.streams.V4OutputStream;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class V4CameraLocationAction extends AbstractAction implements
        CameraLocationAction {

    private Location location;

    @Override
    public void execute(Player player) {
        this.getClipPlayer().getCamera().setLocation(this.location);
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

    @Override
    public void writeTo(V4OutputStream os) throws IOException {
        os.writeLocation(this.location);
    }

    @Override
    public void readFrom(V4InputStream os) throws IOException {
        this.location = os.readLocation();
    }

}
