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
package eu.matejkormuth.starving.items.itemmeta;

import java.util.List;

public class StdLoreHandler implements KeyValueHandler {

    private static final char SEPARATOR = ':';
    private LoreAccessor accessor;

    public StdLoreHandler(LoreAccessor loreAccessor) {
        this.accessor = loreAccessor;
    }

    @Override
    public void set(String key, String value) {
        List<String> lore = accessor.getLore();
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            if (line.startsWith(key + SEPARATOR)) {
                lore.set(i, key + SEPARATOR + " " + value);
                accessor.setLore(lore);
                return;
            }
        }
        lore.add(key + SEPARATOR + " " + value);
        accessor.setLore(lore);
    }

    @Override
    public String get(String key) {
        for (String line : accessor.getLore()) {
            if (line.startsWith(key + SEPARATOR)) {
                return line.substring(line.indexOf(SEPARATOR) + 1).trim();
            }
        }
        return null;
    }
}
