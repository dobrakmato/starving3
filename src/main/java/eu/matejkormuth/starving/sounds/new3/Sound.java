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
package eu.matejkormuth.starving.sounds.new3;

import eu.matejkormuth.starving.sounds.SoundCategory;
import eu.matejkormuth.starving.sounds.SoundType;
import lombok.Value;

/**
 * Represents sound file in resource pack.
 */
@Value
public final class Sound {

    /**
     * Constant that specifies the length of sound is undefined.
     */
    public static final long LENGTH_UNDEFINED = -1024L;

    /**
     * Internal name of this sound effect.
     */
    private String name;

    /**
     * Length of the sound in milliseconds.
     */
    private long length = LENGTH_UNDEFINED;

    /**
     * Category of played sound.
     */
    private SoundCategory category = SoundCategory.UNCATEGORIZED;

    /**
     * Type of sound.
     */
    private SoundType type = SoundType.SINGLE;

    /**
     * Maximum hearing distance of this sound.
     */
    private int maxHearingDistance = 16;

}
