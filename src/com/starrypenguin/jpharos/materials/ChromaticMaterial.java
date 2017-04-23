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
import java.util.Queue;

/**
 * ChromaticMaterial
 * <p/>
 * Multi-colored material that shows depth using different colors
 */
public class ChromaticMaterial extends Material {

    // we want the RGB range to go from 0.1 to 0.9 on a scale of 0.0 to 1.0

    final static private double SCALE = 0.5;
    final static private double OFFSET = 0.5;

    @Override
    protected Color getColorInternal(Intersection intersection) {
        double deltaX = intersection.body.getBoundingBox().max.x - intersection.body.getBoundingBox().min.x;
        double deltaY = intersection.body.getBoundingBox().max.y - intersection.body.getBoundingBox().min.y;
        double deltaZ = intersection.body.getBoundingBox().max.z - intersection.body.getBoundingBox().min.z;

        float red  = (float) Math.min(Math.max(((((intersection.intersectionPoint.x - intersection.body.getBoundingBox().max.x)/ deltaX) * SCALE) + OFFSET), 0.0), 1.0);
        float green  = (float) Math.min(Math.max(((((intersection.intersectionPoint.y - intersection.body.getBoundingBox().max.y)/ deltaY) * SCALE) + OFFSET), 0.0), 1.0);
        float blue  = (float) Math.min(Math.max(((((intersection.intersectionPoint.z - intersection.body.getBoundingBox().max.z)/ deltaZ) * SCALE) + OFFSET), 0.0), 1.0);

        return new Color(red, green, blue);
    }

    @Override
    public Queue<Color> getColor(Intersection intersection) {
        return Material.calculateLambertianAndShadow(intersection);
    }

}
