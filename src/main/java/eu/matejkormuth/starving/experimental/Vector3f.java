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
package eu.matejkormuth.starving.experimental;

import lombok.ToString;

@ToString
public class Vector3f implements Cloneable {

    public static final Vector3f ZERO = new Vector3f(0, 0, 0);

    public final float x;
    public final float y;
    public final float z;

    public Vector3f() {
        this(0, 0, 0);
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    protected Vector3f clone() throws CloneNotSupportedException {
        return new Vector3f(x, y, z);
    }

    public Vector3f add(Vector3f vec) {
        return new Vector3f(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vector3f subtract(Vector3f vec) {
        return new Vector3f(x - vec.x, y - vec.y, z - vec.z);
    }

    public Vector3f multiply(float n) {
        return new Vector3f(x * n, y * n, z * n);
    }

    public Vector3f add(float x, float y, float z) {
        return new Vector3f(this.x + x, this.y + y, this.z + z);
    }

    public Vector3f subtract(float x, float y, float z) {
        return new Vector3f(this.x - x, this.y - y, this.z - z);
    }

    public float dot(Vector3f vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    public Vector3f cross(Vector3f vec) {
        return new Vector3f(y * vec.z - vec.y * z,
                z * vec.x - vec.z * x,
                x * vec.y - vec.x * y);
    }

    public Vector3f normalize() {
        float length = length();
        return new Vector3f(x / length, y / length, z / length);
    }

    public Vector3f invert() {
        return new Vector3f(-x, -y, -z);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public float distance(Vector3f vec) {
        return (float) Math.sqrt(Math.pow(vec.x - x, 2) + Math.pow(vec.y - y, 2) + Math.pow(vec.z - z, 2));
    }

    public float distanceSquared(Vector3f vec) {
        return (float) (Math.pow(vec.x - x, 2) + Math.pow(vec.y - y, 2) + Math.pow(vec.z - z, 2));
    }


    public Vector3f rotate(float degrees, Vector3f axis) {
        double sinHalfAngle = Math.sin(degrees / 2);
        double cosHalfAngle = Math.cos(degrees / 2);

        float rX = (float) (axis.x * sinHalfAngle);
        float rY = (float) (axis.y * sinHalfAngle);
        float rZ = (float) (axis.z * sinHalfAngle);
        float rW = (float) cosHalfAngle;

        Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
        Quaternion conjugate = rotation.conjugate();
        Quaternion w = rotation.multiply(this).multiply(conjugate);

        return new Vector3f(w.x, w.y, w.z);
    }
}
