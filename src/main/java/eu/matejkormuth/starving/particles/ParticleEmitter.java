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
package eu.matejkormuth.starving.particles;

import com.darkblade12.particleeffect.ParticleEffect;
import com.darkblade12.particleeffect.ParticleEffect.ParticleColor;
import com.darkblade12.particleeffect.ParticleEffect.ParticleData;
import com.darkblade12.particleeffect.ParticleEffect.ParticleProperty;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleEmitter {

    private Location location;
    private float speed;
    private int amount;
    private ParticleEffect effect;

    private ParticleColor color;

    private ParticleData data;

    private Vector direction;

    private float offsetX;
    private float offsetY;
    private float offsetZ;

    private int interval = 1;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    private boolean showLines;

    public ParticleEmitter() {
    }

    public ParticleEmitter(Location location, float speed, int amount,
            ParticleEffect effect) {
        this.location = location;
        this.speed = speed;
        this.amount = amount;
        this.effect = effect;
    }

    public Location getLocation() {
        return location;
    }

    public float getSpeed() {
        return speed;
    }

    public int getAmount() {
        return amount;
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    public void setEffect(ParticleEffect effect) {
        this.effect = effect;
    }

    public void setLocation(Location location) {
        if(location.getWorld() == null) {
            throw new IllegalArgumentException("World is null!");
        }

        this.location = location;
    }

    public void emit() {
        if (this.location == null) {
            System.out.println("Can't emit particle. Location null.");
        }

        if (this.data != null) {
            if (this.direction != null) {
                this.effect.display(data, direction, speed, this.location,
                        Double.MAX_VALUE);
            } else {
                this.effect.display(data, offsetX, offsetY, offsetZ, speed,
                        amount, this.location, Double.MAX_VALUE);
            }
        } else if (this.color != null) {
            this.effect.display(color, this.location, Double.MAX_VALUE);
        } else {
            if (this.direction != null) {
                this.effect.display(direction, speed, this.location,
                        Double.MAX_VALUE);
            } else {
                this.effect.display(offsetX, offsetY, offsetZ, speed, amount,
                        this.location, Double.MAX_VALUE);
            }
        }
    }

    public ParticleColor getColor() {
        return color;   
    }

    public void setColor(ParticleColor color) {
        if (!this.isColorable()) {
            throw new UnsupportedOperationException("particle "
                    + this.effect.name() + " does not supports color.");
        }

        this.color = color;
    }

    public ParticleData getData() {
        return data;
    }

    public void setData(ParticleData data) {
        if (!this.requiresData()) {
            throw new UnsupportedOperationException("particle "
                    + this.effect.name() + " does not supports data.");
        }

        this.data = data;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        if (!this.isDirectional()) {
            throw new UnsupportedOperationException("particle "
                    + this.effect.name() + " does not supports direction.");
        }
        this.direction = direction;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(float offsetZ) {
        this.offsetZ = offsetZ;
    }

    public void setOffsets(float offsetX, float offsetY, float offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setShowLines(boolean showLines) {
        this.showLines = showLines;
    }

    public boolean isShowLines() {
        return this.showLines;
    }

    public boolean isDirectional() {
        return this.effect.hasProperty(ParticleProperty.DIRECTIONAL);
    }

    public boolean isColorable() {
        return this.effect.hasProperty(ParticleProperty.COLORABLE);
    }

    public boolean requiresData() {
        return this.effect.hasProperty(ParticleProperty.REQUIRES_DATA);
    }

    public boolean requiresWater() {
        return this.effect.hasProperty(ParticleProperty.REQUIRES_WATER);
    }
}
