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

import eu.matejkormuth.starving.cinematics.Clip;
import eu.matejkormuth.starving.cinematics.Frame;
import eu.matejkormuth.starving.cinematics.updates.CameraEvent;
import eu.matejkormuth.starving.main.Time;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ClipBuilder {

    private Clip clip;
    private InterpolationType interpolationType = InterpolationType.SLERP;
    private List<Node> nodes = new ArrayList<>();

    private ClipBuilder(@Nonnull String name) {
        clip = new Clip();
        clip.setName(name);
    }

    public static ClipBuilder create(@Nonnull String name) {
        return new ClipBuilder(name);
    }

    /**
     * Sets frames per second for this clip.
     *
     * @param fps frame per second
     * @return this clip builder
     */
    public ClipBuilder fps(int fps) {
        clip.setFps((short) fps);
        return this;
    }


    /**
     * Sets the look at point vector of last point.
     *
     * @param point new look at
     * @return this clip builder
     */
    public ClipBuilder lookAtPoint(@Nonnull Vector point) {
        lastNode().setLookAtPoint(point);
        return this;
    }

    /**
     * Sets the look at point vector of last point.
     *
     * @param x x
     * @param y y
     * @param z z
     * @return this clip builder
     */
    public ClipBuilder lookAtPoint(double x, double y, double z) {
        return lookAtPoint(new Vector(x, y, z));
    }

    /**
     * Adds new point to path of this clip
     *
     * @param x x
     * @param y y
     * @param z z
     * @return this clip builder
     */
    public ClipBuilder point(double x, double y, double z) {
        return point(new Vector(x, y, z));
    }

    /**
     * Adds new point to path of this clip
     *
     * @param point point
     * @return this clip builder
     */
    public ClipBuilder point(@Nonnull Vector point) {
        Node node = new Node();
        node.setPosition(point);
        nodes.add(node);
        return this;
    }

    /**
     * Sets the length of travel to next node.
     *
     * @param length length of travel to next node
     * @return this clip builder
     */
    public ClipBuilder duration(@Nonnull Time length) {
        lastNode().setTime(length);
        return this;
    }

    /**
     * Sets interpolation mode to slerp.
     *
     * @return this clip builder
     */
    public ClipBuilder slerp() {
        interpolationType = InterpolationType.SLERP;
        return this;
    }

    /**
     * Sets interpolation mode to slerp.
     *
     * @return this clip builder
     */
    public ClipBuilder lerp() {
        interpolationType = InterpolationType.LERP;
        return this;
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

    /**
     * Renders the frames from nodes to clip, and
     * returns rendered clip.
     */
    public Clip render() {
        long seconds = nodes.stream().mapToLong(value -> value.getTime().toSeconds()).sum();
        long frameCount = seconds * clip.getFps();

        Bukkit.broadcastMessage("Total nodes: " + nodes.size());
        Bukkit.broadcastMessage("Total seconds: " + seconds);
        Bukkit.broadcastMessage("Total frames to be generated: " + frameCount);
        Bukkit.broadcastMessage("Interpolation method: " + interpolationType);

        List<Frame> allFrames = new ArrayList<>((int) frameCount);

        for (int i = 0; i < nodes.size() - 1; i++) {
            Node current = nodes.get(i);
            Node next = nodes.get(i + 1);

            Bukkit.broadcastMessage("Rendering node path #" + i);
            renderFrame(current, next, allFrames, clip.getFps());
        }

        this.clip.setFrames(allFrames);

        return this.clip;
    }

    /**
     * Renders frames between specified nodes to allFrames.
     *
     * @param current   current node
     * @param next      next node
     * @param allFrames all frames list
     * @param fps       fps of clip
     */
    private void renderFrame(Node current, Node next, List<Frame> allFrames, int fps) {
        Bukkit.broadcastMessage(" Path time: " + current.getTime().toString());
        Bukkit.broadcastMessage(" Resulting frames: " + current.getTime().toSeconds() * fps);
        Bukkit.broadcastMessage(" From: " + current.getPosition());
        Bukkit.broadcastMessage(" To: " + next.getPosition());
        Bukkit.broadcastMessage(" Look at: " + current.getLookAtPoint());

        int frames = (int) (current.getTime().toSeconds() * fps);
        if (frames == 0) {
            frames = 1;
        }

        double addition = 1f / (float) frames;
        double progress = 0;

        // Generate 'frames' frames.
        for (int i = 0; i < frames; i++) {
            Frame frame = new Frame();
            CameraEvent event = new CameraEvent();

            Vector framePosition = interpolate(current.getPosition(), next.getPosition(), progress);
            Vector dir = current.getLookAtPoint().clone().subtract(framePosition);
            dir.normalize();
            float distance = (float) Math.sqrt(dir.getZ() * dir.getZ() + dir.getX() * dir.getX());
            float pitch = (float) Math.atan2(dir.getY(), distance);
            float yaw = (float) Math.atan2(dir.getX(), dir.getZ());

            event.setPosition(framePosition);
            event.setPitch((float) Math.toDegrees(pitch));
            event.setYaw((float) Math.toDegrees(yaw));

            // Add camera event.
            frame.add(event);

            // Add frame to list.
            allFrames.add(frame);

            // Proceed
            progress = clamp(0, 1, progress + addition);
        }
    }

    /**
     * Claps specified value between min and max values.
     *
     * @param min min value
     * @param max max value
     * @param val value to clamp
     * @return clamped value
     */
    private double clamp(double min, double max, double val) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Interpolates from vector to vector using selected interpolation type.
     *
     * @param from     from vector
     * @param to       to vector
     * @param progress progress in 0 to 1
     * @return interpolated vector
     */
    private Vector interpolate(Vector from, Vector to, double progress) {
        switch (interpolationType) {
            case LERP:
                return Interpolation.lerp(from, to, (float) progress);
            case SLERP:
                return Interpolation.slerp(from, to, (float) progress);
            default:
                return from;
        }
    }
}
