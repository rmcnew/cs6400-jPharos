/*
 * jPharos is a simple Java-based Ray Tracer.
 * Copyright (c) 2017.   Richard Scott McNew
 *
 * jPharos is free software: you can redistribute it and / or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.starrypenguin.jpharos.materials;

import com.starrypenguin.jpharos.core.Intersection;

import java.awt.*;

/**
 * NullMaterial
 * <p/>
 * Dummy material to simplify class hierarchy; used when raw Shapes are
 * needed to fill Bodies that do not have corresponding Materials.
 */
final public class NullMaterial extends Material {
    // use a Singleton NullMaterial object to save resources
    private static final NullMaterial nullMaterial = new NullMaterial();

    private NullMaterial() {
    }

    public static NullMaterial instance() {
        return nullMaterial;
    }

    @Override
    public Color getColor(Intersection intersection) {
        return null;
    }
}
