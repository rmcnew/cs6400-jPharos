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

import com.starrypenguin.jpharos.core.CastRay;
import com.starrypenguin.jpharos.core.Intersection;
import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.lights.Light;
import com.starrypenguin.jpharos.main.jPharos;

import java.awt.*;

/**
 * Material
 * <p/>
 * The material composition of a Body that determines what happens when light interacts with a Body;
 * This is the abstract base class for all materials
 */
public abstract class Material {

    // common Material properties go here

    /**
     * Calculate the Lambertian lighting and shadows
     *
     * @return Color with darkness values adjusted
     */
    protected static Color calculateLambertianAndShadow(Intersection intersection) {
        // see if this intersection point is in the shadows:  can we cast rays to a light source?
        for (Light light : jPharos.instance.scene.lights) {
            Vector directionToLight = new Vector(intersection.intersectionPoint, light.location);
            Ray towardLight = new Ray(intersection.intersectionPoint, directionToLight);
            CastRay castRay = new CastRay(towardLight);
            Intersection maybeIntersection = castRay.castRay(towardLight);
            if (maybeIntersection != null && maybeIntersection.body != intersection.body) {  // we hit something, a shadow is here
                return Color.BLACK.brighter();
            }
        }
        double rawLambert = Math.max(intersection.ray.direction.dot(intersection.surfaceNormal), 0.0);
        double maxValue = intersection.ray.direction.magnitude() * intersection.surfaceNormal.magnitude();
        double scaledLambert = rawLambert / maxValue;
        float factor = (float) (1.0 - scaledLambert);
        Color intersectionPointColor = intersection.body.material.getColorInternal(intersection);
        float red = (float) (intersectionPointColor.getRed() / 255.0) * factor;
        float green = (float) (intersectionPointColor.getGreen() / 255.0) * factor;
        float blue = (float) (intersectionPointColor.getBlue() / 255.0) * factor;
        return new Color(red, green, blue);
    }

    protected static Color calculateReflection(Intersection intersection) {
        // cos(theta) = dot(-normal, ray.direction)
        // cos(theta) must be positive, otherwise invert normal
        double cosTheta = intersection.surfaceNormal.inverse().dot(intersection.ray.direction);
        if (cosTheta < 0.0) {
            cosTheta = intersection.surfaceNormal.dot(intersection.ray.direction);
        }

        // v_reflect = ray.direction + 2 * cos(theta) * normal
        Vector v_reflect = intersection.ray.direction.plus(intersection.surfaceNormal.scale(2 * cosTheta));

        // cast v_reflect to get color of whatever it hits, reduced by some factor
        Ray reflectedRay = new Ray(intersection.intersectionPoint, v_reflect);
        CastRay castRay = new CastRay(reflectedRay);
        Intersection maybeIntersection = castRay.castRay(reflectedRay);
        if (maybeIntersection != null) { // use the color of the reflection
            return maybeIntersection.body.material.getColor(maybeIntersection).darker();
        }
        // the reflected ray did not hit anything, show the color of the surrounding environment
        return Color.BLACK.brighter();
    }

    public abstract Color getColor(Intersection intersection);

    protected abstract Color getColorInternal(Intersection intersection);
}
