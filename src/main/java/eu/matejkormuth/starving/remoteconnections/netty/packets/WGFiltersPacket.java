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
package eu.matejkormuth.starving.remoteconnections.netty.packets;

import eu.matejkormuth.starving.worldgen.filters.base.Filter;
import eu.matejkormuth.starving.worldgen.filters.base.FilterProperty;
import io.netty.buffer.ByteBuf;

import java.util.Collection;

import eu.matejkormuth.starving.remoteconnections.netty.Packet;

public class WGFiltersPacket extends Packet {

    public FilterRepresentation[] filters;

    public WGFiltersPacket() {
    }

    public WGFiltersPacket(Collection<Filter> filters) {
        int count = filters.size();
        this.filters = new FilterRepresentation[count];
        int index = 0;
        for (Filter f : filters) {
            this.filters[index] = new FilterRepresentation();
            this.filters[index].name = f.getName();
            Collection<FilterProperty> props = f.getDefaultProperties().getProperties().values();
            this.filters[index].properties = new FilterPropertyRepresentaion[props.size()];
            int propIndex = 0;
            for (FilterProperty fp : props) {
                this.filters[index].properties[propIndex] = new FilterPropertyRepresentaion();
                this.filters[index].properties[propIndex].name = fp.getName();
                this.filters[index].properties[propIndex].type = (byte) (fp.getType() - 1000);
                propIndex++;
            }
            index++;
        }
    }

    @Override
    public void writeTo(ByteBuf toBuffer) {
        toBuffer.writeByte(filters.length);
        for (FilterRepresentation fr : filters) {
            toBuffer.writeByte(fr.name.length());
            toBuffer.writeBytes(fr.name.getBytes(PROTOCOL_ENCODING));
            toBuffer.writeByte(fr.properties.length);
            for (FilterPropertyRepresentaion fpr : fr.properties) {
                toBuffer.writeByte(fpr.name.length());
                toBuffer.writeBytes(fpr.name.getBytes(PROTOCOL_ENCODING));
                toBuffer.writeByte(fpr.type);
            }
        }
    }

    @Override
    public void readFrom(ByteBuf fromBuffer) {
        byte filterCount = fromBuffer.readByte();
        FilterRepresentation[] filters = new FilterRepresentation[filterCount];
        for (int i = 0; i < filterCount; i++) {
            filters[i] = new FilterRepresentation();
            byte nameLength = fromBuffer.readByte();
            byte[] nameBuffer = new byte[nameLength];
            fromBuffer.readBytes(nameBuffer);
            filters[i].name = new String(nameBuffer, PROTOCOL_ENCODING);
            byte propertiesCount = fromBuffer.readByte();
            filters[i].properties = new FilterPropertyRepresentaion[propertiesCount];
            for (int p = 0; p < propertiesCount; p++) {
                byte propertyNameLength = fromBuffer.readByte();
                byte[] propertyNameBuffer = new byte[propertyNameLength];
                fromBuffer.readBytes(propertyNameBuffer);
                filters[i].properties[p] = new FilterPropertyRepresentaion();
                filters[i].properties[p].name = new String(propertyNameBuffer,
                        PROTOCOL_ENCODING);
                filters[i].properties[p].type = fromBuffer.readByte();
            }
        }
    }

    public final static class FilterRepresentation {
        String name;
        FilterPropertyRepresentaion[] properties;
    }

    public final static class FilterPropertyRepresentaion {
        byte type;
        String name;
    }
}
