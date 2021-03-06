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
package eu.matejkormuth.starving.mappings.api.files;

import eu.matejkormuth.starving.mappings.api.JsonMappingFile;
import eu.matejkormuth.starving.mappings.api.Mapping;
import eu.matejkormuth.starving.mappings.api.Type;
import org.bukkit.Material;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

public class JsonMappingFileTest {

    public static final int AMOUNT = 5;
    public static Random random = new Random();

    @Test
    @Ignore
    public void testToJson() {
        JsonMappingFile jsonMappingFile = new JsonMappingFile();
        Mapping[] arr = new Mapping[AMOUNT];
        for (int i = 0; i < AMOUNT; i++) {
            arr[i] = genMapping();
        }
        jsonMappingFile.setMappings(arr);
        System.out.println(jsonMappingFile.toJson());
    }

    private Mapping genMapping() {
        Material material = Material.values()[random.nextInt(Material.values().length)];
        String key = Integer.toHexString(random.nextInt());
        int data = random.nextInt(4);
        Type type = random.nextBoolean() ? Type.BLOCK : Type.ITEM;
        int durability = 0;
        return new Mapping(key, material, data, type, durability);
    }
}