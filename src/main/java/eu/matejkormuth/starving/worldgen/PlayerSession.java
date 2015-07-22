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
package eu.matejkormuth.starving.worldgen;

import org.bukkit.entity.Player;

import eu.matejkormuth.starving.worldgen.brushes.Brush;
import eu.matejkormuth.starving.worldgen.brushes.BrushType;
import eu.matejkormuth.starving.worldgen.brushes.CircleBrush;
import eu.matejkormuth.starving.worldgen.brushes.SquareBrush;
import eu.matejkormuth.starving.worldgen.filters.GrassFilter;
import eu.matejkormuth.starving.worldgen.filters.base.Filter;
import eu.matejkormuth.starving.worldgen.filters.base.FilterProperties;

public class PlayerSession {

    private Player player;
    private Brush brush;
    private Filter filter;
    private FilterProperties filterProperties;
    private int maxDistance;

    public PlayerSession(Player player) {
        this.player = player;
        this.maxDistance = 256;
        this.brush = new SquareBrush(5);
        this.setFilter(new GrassFilter());
    }

    public Player getPlayer() {
        return player;
    }

    public void setBrushSize(int newSize) {
        if (this.brush.getType() == BrushType.CIRCLE) {
            this.brush = new CircleBrush(newSize);
        } else if (this.brush.getType() == BrushType.SQUARE) {
            this.brush = new SquareBrush(newSize);
        } else {
            throw new RuntimeException("Invalid brush type!");
        }
    }

    public void setBrushType(BrushType type) {
        int size = this.brush.getSize();
        switch (type) {
            case CIRCLE:
                this.brush = new CircleBrush(size);
                break;
            case SQUARE:
                this.brush = new SquareBrush(size);
                break;
        }
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
        // Clone properties from default properties.
        this.filterProperties = filter.getDefaultProperties().clone();
        // TODO: Notify remote client of filter properties change.
    }

    public Filter getFilter() {
        return filter;
    }

    public FilterProperties getFilterProperties() {
        return filterProperties;
    }

    public void setFilterProperties(FilterProperties filterProperties) {
        this.filterProperties = filterProperties;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getMaxDistance() {
        return this.maxDistance;
    }

    public Brush getBrush() {
        return brush;
    }
}
