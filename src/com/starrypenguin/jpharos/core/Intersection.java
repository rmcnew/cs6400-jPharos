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
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.lights.Light;
import com.starrypenguin.jpharos.main.jPharos;
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
    public final Body body;

    public Intersection(Ray ray, double intersectionTime, Normal surfaceNormal, Point intersectionPoint, Body body) {
        Shared.notNull(ray, "ray cannot be null!");
        Shared.notNaN(intersectionTime, "intersectionTime cannot be Not A Number!");
        Shared.notNull(surfaceNormal, "surfaceNormal cannot be null!");
        Shared.notNull(intersectionPoint, "intersectionPoint cannot be null!");
        Shared.notNull(body, "body cannot be null!");
        this.ray = ray;
        this.intersectionTime = intersectionTime;
        this.surfaceNormal = surfaceNormal;
        this.intersectionPoint = intersectionPoint;
        this.body = body;
    }

    /**
     * Calculate the Lambertian lighting and shadows
     * @return Color with darkness values adjusted
     */
    public Color calculateLambertianAndShadow() {
        // see if this intersection point is in the shadows:  can we cast rays to a light source?
        for (Light light : jPharos.scene.lights) {
            Vector directionToLight = new Vector(intersectionPoint, light.location);
            Ray towardLight = new Ray(intersectionPoint, directionToLight);
            Intersection maybeIntersection = jPharos.scene.castRay(towardLight);
            if (maybeIntersection != null && maybeIntersection.body != body) {  // we hit something, a shadow is here
                return Color.BLACK.brighter();
            }
        }
        double rawLambert = Math.max(ray.direction.dot(surfaceNormal), 0.0);
        double maxValue = ray.direction.magnitude() * surfaceNormal.magnitude();
        double scaledLambert = rawLambert / maxValue;
        float factor = (float) (1.0 - scaledLambert);
        float red = (float) (this.body.material.color.getRed() / 255.0) * factor;
        float green = (float) (this.body.material.color.getGreen() / 255.0) * factor;
        float blue = (float) (this.body.material.color.getBlue() / 255.0) * factor;
        return new Color(red, green, blue);
    }


}
