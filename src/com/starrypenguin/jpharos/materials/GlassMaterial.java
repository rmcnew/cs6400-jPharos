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
import com.starrypenguin.jpharos.util.Shared;

import java.awt.*;

/**
 * GlassMaterial
 * <p/>
 * Glass-like refractive material
 */
public class GlassMaterial extends Material {

    final private double indexOfRefraction;

    public GlassMaterial(double indexOfRefraction) {
        Shared.notNaN(indexOfRefraction, "Parameter indexOfRefraction cannot be Not A Number!");
        this.indexOfRefraction = indexOfRefraction;
    }

    protected static Color calculateRefraction(Intersection intersection) {
        Shared.notNull(intersection, "Parameter intersection cannot be null!");
        // make sure the intersected material is refractive
        if (!(intersection.body.material instanceof GlassMaterial)) {
            throw new IllegalArgumentException("material is not a refractive material!");
        }

        // r = n_1 / n_2 where n_1 is the index of refraction for the current medium
        // and n_2 is the index of refraction for the new medium the light ray is entering

        // c = dot(-normal, ray.direction

        // v_refract = r * ray.direction + (r * c - sqrt( (1 - r^2) * (1 - c^2))) * normal
        return null;
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
