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

/**
 * <p>
 * Represents property of a Filter. This class holds value of specified
 * property.
 * </p>
 * <p>
 * The value is internally represented as a <i>float</i>. Casting to integer and
 * from integer is trivial. Every value that is bigger then <b>BOOLEAN_FALSE</b>
 * constant is considered as <i>true</i>. Everything smaller or equal as
 * constant <b>BOOLEAN_FALSE</b> is considered as <i>false</i>. There is also
 * constant <b>BOOLEAN_TRUE</b> which holds default representation of
 * <i>true</i> as <i>float</i>.
 * </p>
 * <p>
 * Static instances of this class are used to represents default value of
 * properties in each Filter class. There are also instances of FilterProperty
 * in PlayerSession which holds current values of filter properties.
 * </p>
 * <p>
 * All instances are wrapped in FilterProperties object which behaves like a
 * collection of FilterProperty.
 * </p>
 */
public class FilterProperty {

    /**
     * Constant that represents float value type.
     */
    public static final int TYPE_FLOAT = 1002;
    /**
     * Constant that represents boolean value type.
     */
    public static final int TYPE_BOOLEAN = 1001;
    /**
     * Constant that represents integer value type.
     */
    public static final int TYPE_INTEGER = 1000;

    /**
     * Constant that holds float value of representation of boolean true.
     */
    public static final float BOOLEAN_TRUE = 1;
    /**
     * Constant that holds float value of representation of boolean false.
     */
    public static final float BOOLEAN_FALSE = 0;

    private float value;
    private final String name;
    private final int type;

    public FilterProperty(String name, float defaultValue) {
        this.name = name;
        this.value = defaultValue;
        this.type = TYPE_FLOAT;
    }

    public FilterProperty(String name, int defaultValue) {
        this.name = name;
        this.value = defaultValue;
        this.type = TYPE_INTEGER;
    }

    public FilterProperty(String name, boolean defaultValue) {
        this.name = name;
        this.type = TYPE_BOOLEAN;
        if (defaultValue) {
            this.value = BOOLEAN_TRUE;
        } else {
            this.value = BOOLEAN_FALSE;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue(boolean value) {
        if (value) {
            this.value = BOOLEAN_TRUE;
        } else {
            this.value = BOOLEAN_FALSE;
        }
    }

    public int asInt() {
        return (int) value;
    }

    public float asFloat() {
        return value;
    }

    public boolean asBoolean() {
        if (value > BOOLEAN_FALSE) {
            return true;
        } else {
            return false;
        }
    }
}
