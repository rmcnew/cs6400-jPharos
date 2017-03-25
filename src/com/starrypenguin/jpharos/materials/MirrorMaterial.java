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
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.main.jPharos;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * MirrorMaterial
 * <p/>
 * Mirror-like reflective material
 */
public class MirrorMaterial extends Material {

    protected static Vector calculateReflectedVector1(Intersection intersection) {
        // cos(theta) = dot(-normal, ray.direction)
        // cos(theta) must be positive, otherwise invert normal
        double cosTheta = intersection.surfaceNormal.inverse().dot(intersection.ray.direction);
        if (cosTheta < 0.0) {
            cosTheta = intersection.surfaceNormal.dot(intersection.ray.direction);
        }

        // v_reflect = ray.direction + 2 * cos(theta) * normal
        Vector v_reflect = intersection.ray.direction.plus(intersection.surfaceNormal.scale(2 * cosTheta));
        return v_reflect;
    }

    protected static Vector calculateReflectedVector2(Intersection intersection) {
        Vector tempVector = intersection.ray.direction.inverse().minus(intersection.surfaceNormal);
        Vector v_reflect = intersection.surfaceNormal.plus(tempVector).normalized();
        return v_reflect;
    }

    protected static Color calculateReflection(Intersection intersection) {
        Vector v_reflect = calculateReflectedVector1(intersection);
        // cast v_reflect to get color of whatever it hits, reduced by some factor
        //System.out.println("Intersection point is: " + intersection.intersectionPoint + ", v_reflect is: " + v_reflect);
        Ray reflectedRay = new Ray(intersection.intersectionPoint, v_reflect);
        CastRayForIntersection castRayForIntersection = new CastRayForIntersection(reflectedRay);
        jPharos.instance.executor.castRay(castRayForIntersection);
        Intersection maybeIntersection = null;
        try {
            Future<Intersection> futureIntersection = jPharos.instance.executor.pollIntersections();
            maybeIntersection = futureIntersection.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (maybeIntersection != null) { // use the color of the reflection
            System.out.println("Reflected ray hit " + maybeIntersection.body.name);
            return maybeIntersection.body.material.getColor(maybeIntersection).darker();
        }
        // the reflected ray did not hit anything, show the color of the surrounding environment
        return Color.BLACK.brighter();
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
