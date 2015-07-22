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
package eu.matejkormuth.starving.localities;

import java.util.HashMap;
import java.util.Map;

import eu.matejkormuth.starving.main.Region;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Locality")
public class Locality implements ConfigurationSerializable {
    static {
        ConfigurationSerialization.registerClass(Locality.class);
    }

    /**
     * Constant for locality, that is everywhere, where any other locality isn't
     * specified.
     */
    public static final Locality WILDERNESS = new Locality("Wilderness", null);

    private final String name;
    private final Region region;

    public Locality(Map<String, Object> serialized) {
        this.name = String.valueOf(serialized.get("name"));
        this.region = (Region) serialized.get("region");
    }

    public Locality(String name, Region region) {
        this.name = name;
        this.region = region;
    }

    public String getName() {
        return this.name;
    }

    public Region getRegion() {
        return this.region;
    }

    public boolean isWilderness() {
        return this == WILDERNESS;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<String, Object>();
        serialized.put("name", this.name);
        serialized.put("region", this.region);
        return serialized;
    }

    public boolean isPvpFree() {
        return false;
    }
}
