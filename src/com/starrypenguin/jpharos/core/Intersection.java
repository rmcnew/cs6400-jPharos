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
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.util.Shared;

import java.awt.*;


/**
 * Intersection
 * <p/>
 * Represents the data from when a Ray hits a Body
 */
final public class Intersection {

    public final double intersectionTime;
    public final Ray ray;
    public final Normal surfaceNormal;
    public final Point intersectionPoint;
    public final Color color;

    public Intersection(Ray ray, double intersectionTime, Normal surfaceNormal, Point intersectionPoint, Color color) {
        Shared.notNull(ray, "ray cannot be null!");
        Shared.notNaN(intersectionTime, "intersectionTime cannot be Not A Number!");
        Shared.notNull(surfaceNormal, "surfaceNormal cannot be null!");
        Shared.notNull(intersectionPoint, "intersectionPoint cannot be null!");
        Shared.notNull(color, "color cannot be null!");
        this.ray = ray;
        this.intersectionTime = intersectionTime;
        this.surfaceNormal = surfaceNormal;
        this.intersectionPoint = intersectionPoint;
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

    @Override
    public String toString() {
        return "Intersection{" +
                "intersectionTime=" + intersectionTime +
                ", ray=" + ray +
                ", surfaceNormal=" + surfaceNormal +
                ", intersectionPoint=" + intersectionPoint +
                ", color=" + color +
                '}';
    }
}
