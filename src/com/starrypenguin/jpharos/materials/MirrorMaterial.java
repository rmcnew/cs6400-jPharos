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

import com.starrypenguin.jpharos.core.CastRayForIntersection;
import com.starrypenguin.jpharos.core.Intersection;
import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;

import java.awt.*;

/**
 * MirrorMaterial
 * <p/>
 * Mirror-like reflective material
 */
public class MirrorMaterial extends Material {

    private final static double RAY_ADJUST_TIME = 0.01;

    protected static Vector calculateReflectedVector(Intersection intersection) {
        Vector v_reflect = intersection.surfaceNormal.scale(2.0).minus(intersection.ray.direction);
        return v_reflect;
    }

    protected static Color calculateReflection(Intersection intersection) {
        Vector v_reflect = calculateReflectedVector(intersection);
        // cast v_reflect to get color of whatever it hits, reduced by some factor
        //System.out.println("Intersection point is: " + intersection.intersectionPoint + ", v_reflect is: " + v_reflect);
        Ray reflectedRay = adjustRayOrigin(intersection.intersectionPoint, v_reflect);
        //System.out.println("Angle between inverted incidence and normal: " + intersection.ray.direction.inverse().angleBetween(intersection.surfaceNormal.toVector()) + ", angle between normal and reflection: " + reflectedRay.direction.angleBetween(intersection.surfaceNormal.toVector()));
        //System.out.println("Incident ray: " + intersection.ray + ", intersection Point: " + intersection.intersectionPoint + ", Intersection Point distance from origin: " + Point.distance(intersection.intersectionPoint, Point.ORIGIN)  + ", Reflected ray: " + reflectedRay);
        CastRayForIntersection castRayForIntersection = new CastRayForIntersection(reflectedRay);
        Intersection maybeIntersection = castRayForIntersection.call();

        if (maybeIntersection != null) { // use the color of the reflection
            //System.out.println("Reflected ray: " + maybeIntersection.ray + " hit " + maybeIntersection.body.name + " at intersection point: " + maybeIntersection.intersectionPoint + " and intersection time: " + maybeIntersection.intersectionTime);
            return maybeIntersection.body.material.getColor(maybeIntersection).darker();
        }
        // the reflected ray did not hit anything, show the color of the surrounding environment
        return Color.BLACK.brighter();
    }

    private static Ray adjustRayOrigin(Point intersectionPoint, Vector direction) {
        Ray tempRay = new Ray(intersectionPoint, direction.normalized());
        Point adjustedIntersectionPoint = tempRay.atTime(RAY_ADJUST_TIME);
        return new Ray(adjustedIntersectionPoint, direction);
    }

    @Override
    public Color getColor(Intersection intersection) {
        return calculateReflection(intersection);
    }

    @Override
    protected Color getColorInternal(Intersection intersection) {
        return null;
    }

}
