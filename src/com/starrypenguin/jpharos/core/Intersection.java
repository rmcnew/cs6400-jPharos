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

package com.starrypenguin.jpharos.core;

import com.starrypenguin.jpharos.geometry.Normal;
import com.starrypenguin.jpharos.util.Shared;

import java.awt.*;

/**
 * Intersection
 * <p/>
 * Represents the data from when a Ray hits a Body
 */
final public class Intersection {

    // at what time along the Ray did the Impact occur?
    public final double impactTime;
    public final Ray ray;
    public final Normal surfaceNormal;
    public final Color color;

    public Intersection(Ray ray, double impactTime, Normal surfaceNormal, Color color) {
        Shared.notNull(ray, "ray cannot be null!");
        Shared.notNaN(impactTime, "impactTime cannot be Not A Number!");
        Shared.notNull(surfaceNormal, "surfaceNormal cannot be null!");
        Shared.notNull(color, "color cannot be null!");
        this.ray = ray;
        this.impactTime = impactTime;
        this.surfaceNormal = surfaceNormal;
        this.color = color;
    }

    /**
     * Calculate the Lambertian lighting
     * @return Color with darkness values adjusted
     */
    public Color calculateLambertian() {
        double rawLambert = Math.max(ray.direction.dot(surfaceNormal), 0.0);
        double maxValue = ray.direction.magnitude() * surfaceNormal.magnitude();
        double scaledLambert = rawLambert / maxValue;
        float factor = (float) (1.0 - scaledLambert);
        float red = (float) (this.color.getRed() / 255.0) * factor;
        float green = (float) (this.color.getGreen() / 255.0) * factor;
        float blue = (float) (this.color.getBlue() / 255.0) * factor;
        return new Color(red, green, blue);
    }
}
