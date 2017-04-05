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
import com.starrypenguin.jpharos.main.jPharos;
import com.starrypenguin.jpharos.util.Shared;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * RefractiveMaterial
 * <p/>
 * Refractive material that bends light similar to glass, quartz, diamond, etc.
 * See RefractionIndices below for refractive materials.
 */
public class RefractiveMaterial extends Material {

    private final static double RAY_ADJUST_TIME = 0.01;
    final private double indexOfRefraction;

    public RefractiveMaterial(double indexOfRefraction) {
        Shared.notNaN(indexOfRefraction, "Parameter indexOfRefraction cannot be Not A Number!");
        this.indexOfRefraction = indexOfRefraction;
    }

    protected static Color calculateRefraction(Intersection intersection) {
        Shared.notNull(intersection, "Parameter intersection cannot be null!");
        // make sure the intersected material is refractive
        if (!(intersection.body.material instanceof RefractiveMaterial)) {
            throw new IllegalArgumentException("material is not a refractive material!");
        }
        RefractiveMaterial intersectionMaterial = (RefractiveMaterial) intersection.body.material;
        // r = n_1 / n_2 where n_1 is the index of refraction for the current medium
        // and n_2 is the index of refraction for the new medium the light ray is entering

        // assume we are casting rays through the air
        double r = RefractionIndices.AIR_SEA_LEVEL / intersectionMaterial.indexOfRefraction;

        // c = dot(normal, ray.direction)
        double c = -(intersection.surfaceNormal.dot(intersection.ray.direction));

        // v_refract = r * ray.direction + (r * c - sqrt( (1 - r^2) * (1 - c^2))) * normal
        Vector rl = intersection.ray.direction.scale(r);
        double temp = (r * c) - Math.sqrt(1 - Math.pow(r, 2) * (1 - Math.pow(c, 2)));
        if (!Double.isNaN(temp)) {
            Vector tn = intersection.surfaceNormal.scale(temp).toVector();
            Vector v_refract = rl.plus(tn);
            Ray refractedRay = adjustRayOrigin(intersection.intersectionPoint, v_refract);
            CastRayForIntersection castRayForIntersection = new CastRayForIntersection(refractedRay);
            Future<Intersection> futureIntersection = jPharos.instance.executor.castRayForFutureIntersection(castRayForIntersection);
            Intersection maybeIntersection = null;
            try {
                maybeIntersection = futureIntersection.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (maybeIntersection != null && maybeIntersection.body != intersection.body) {  // we hit something, get a darker version of the intersection color
                return maybeIntersection.body.material.getColor(maybeIntersection).darker().darker();
            }
        }
        return Color.DARK_GRAY;
    }

    private static Ray adjustRayOrigin(Point intersectionPoint, Vector direction) {
        Ray tempRay = new Ray(intersectionPoint, direction.normalized());
        Point adjustedIntersectionPoint = tempRay.atTime(RAY_ADJUST_TIME);
        return new Ray(adjustedIntersectionPoint, direction);
    }

    @Override
    public Color getColor(Intersection intersection) {
        return calculateRefraction(intersection);
    }

    @Override
    protected Color getColorInternal(Intersection intersection) {
        return null;
    }

    public static final class RefractionIndices {
        // refraction indices from "Physically Based Rendering, Third Edition", Table 8.1
        public static final double VACUUM = 1.0;
        public static final double AIR_SEA_LEVEL = 1.00029;
        public static final double ICE = 1.31;
        public static final double WATER = 1.333; // at 20 degrees Celsius
        public static final double FUSED_QUARTZ = 1.46;
        public static final double GLASS = 1.55; // table gives 1.5 to 1.6
        public static final double SAPPHIRE = 1.77;
        public static final double DIAMOND = 2.42;
    }
}
