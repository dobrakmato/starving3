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

import eu.matejkormuth.starving.cinematics.Frame;
import eu.matejkormuth.starving.cinematics.FrameAction;
import eu.matejkormuth.starving.cinematics.v4.frameactions.AbstractAction;
import eu.matejkormuth.starving.cinematics.v4.streams.V4InputStream;
import eu.matejkormuth.starving.cinematics.v4.streams.V4OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class V4Frame implements Frame, V4Serializable {
    private List<FrameAction> frameActions;

    public V4Frame() {
        this.frameActions = new ArrayList<>();
    }

    @Override
    public Collection<FrameAction> getActions() {
        return this.frameActions;
    }

    @Override
    public void addAction(FrameAction action) {
        if (action instanceof AbstractAction) {
            this.frameActions.add(action);
        }

        throw new RuntimeException(
                "Specified action is not compatible with V4Frame.");
    }

    @Override
    public void writeTo(V4OutputStream os) throws IOException {
        // Write action count.
        os.writeShort(this.frameActions.size());
        // Write actions.
        for (FrameAction fa : this.frameActions) {
            // Write action type.
            os.writeByte(V4ActionRegistry.getType(fa));
            // Write action content.
            ((AbstractAction) fa).writeTo(os);
        }
    }

    @Override
    public void readFrom(V4InputStream os) throws IOException {
        // Read action count.
        short actionCount = os.readShort();
        this.frameActions = new ArrayList<>(actionCount);
        for (int i = 0; i < actionCount; i++) {
            // Read action type.
            byte type = os.readByte();
            // Read action content.
            AbstractAction action = V4ActionRegistry.createAction(type);
            action.readFrom(os);

            this.frameActions.add(action);
        }
    }

}
