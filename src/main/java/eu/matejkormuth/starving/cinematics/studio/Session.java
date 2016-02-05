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
package eu.matejkormuth.starving.cinematics.studio;

import eu.matejkormuth.starving.main.Time;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Session {

    private List<Node> nodes = new ArrayList<>();

    /**
     * Adds specified node to list.
     *
     * @param node node to add
     */
    public void addNode(@Nonnull Node node) {
        nodes.add(node);
    }

    /**
     * Sets the position of last node.
     *
     * @param position new position
     */
    public void setPosition(@Nonnull Vector position) {
        lastNode().setPosition(position);
    }

    /**
     * Sets the position of specified node.
     *
     * @param position new position
     */
    public void setPosition(int node, @Nonnull Vector position) {
        nodes.get(node).setPosition(position);
    }

    /**
     * Sets look at of previous node.
     *
     * @param point new look at
     */
    public void setLookAtPoint(@Nonnull Vector point) {
        lastNode().setLookAtPoint(point);
    }

    /**
     * Sets look at of previous node.
     *
     * @param point new look at
     */
    public void setLookAtPoint(int node, @Nonnull Vector point) {
        nodes.get(node).setLookAtPoint(point);
    }

    /**
     * Sets the length of travel from last node to next node.
     *
     * @param length length of last node
     */
    public void setTime(Time length) {
        lastNode().setTime(length);
    }

    /**
     * Returns the last node in the list.
     *
     * @return last node
     */
    public Node lastNode() {
        if (nodes.size() == 0) {
            throw new RuntimeException("Node list is empty!");
        }

        return nodes.get(nodes.size() - 1);
    }

    public void removeNode(@Nonnull Node node) {
        nodes.remove(node);
    }
}
