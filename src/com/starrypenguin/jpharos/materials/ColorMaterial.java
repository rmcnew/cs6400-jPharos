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

import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.util.Shared;

import java.awt.*;
import java.util.Map;

/**
 * ColorMaterial
 * <p/>
 * A Material with a given color or colors that does not specify light interaction
 */
public class ColorMaterial extends Material {

    private Map<Point, Color> colorMap = null;
    private Color color = null;

    public ColorMaterial(Color color) {
        Shared.notNull(color, "Parameter color cannot be null!");
        this.color = color;
    }

    public ColorMaterial(Map<Point, Color> colorMap) {
        Shared.notNull(colorMap, "Parameter colorMap cannot be null!");
        this.colorMap = colorMap;
    }

    @Override
    public Color getColor(Point point) {
        Shared.notNull(point, "Parameter point cannot be null!");
        if (colorMap != null) {
            return colorMap.get(point);
        }
        return color;
    }

}
