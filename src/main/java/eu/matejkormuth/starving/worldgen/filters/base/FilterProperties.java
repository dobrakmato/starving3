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
package eu.matejkormuth.starving.worldgen.filters.base;

import java.util.HashMap;
import java.util.Map;

public class FilterProperties implements Cloneable {

    private final Map<String, FilterProperty> properties;
    private boolean locked = false;

    public FilterProperties() {
        this.properties = new HashMap<>();
    }

    public void set(FilterProperty property) {
        if (locked) {
            throw new RuntimeException(
                    "Can't modify property of locked (immutable) FilterProperties! Use FilterProperties::clone().");
        }
        this.properties.put(property.getName(), property);
    }

    @Override
    public FilterProperties clone() {
        try {
            FilterProperties fp = (FilterProperties) super.clone();
            fp.locked = false;
            return fp;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    };

    public Map<String, FilterProperty> getProperties() {
        return properties;
    }

    /**
     * Returns FilterProperty or null if property of specified name does not
     * exists.
     * 
     * @param name
     *            Name of property to look up.
     * @return property for specified name if found, null otherwise
     */
    public FilterProperty getProperty(String name) {
        return properties.get(name);
    }

    public void lock() {
        this.locked = true;
    }
}
