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
 * MirrorMaterial
 * <p/>
 * Mirror-like reflective material
 */
public class MirrorMaterial extends Material {

    @Override
    public Color getColor(Intersection intersection) {
        return Material.calculateReflection(intersection);
    }

    @Override
    protected Color getColorInternal(Intersection intersection) {
        return null;
    }
}
