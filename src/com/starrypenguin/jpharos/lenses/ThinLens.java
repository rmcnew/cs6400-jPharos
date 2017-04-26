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

package com.starrypenguin.jpharos.lenses;

import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.util.Shared;

/**
 * ThinLens
 * <p/>
 * A lens in the shape of a square
 */
public class ThinLens extends Lens {

    private final double radius;
    private final Vector lookAt;
    private final Vector up;
    private final Vector right;
    private final Vector normalizedUp;


    public ThinLens(double focalLength, Point location, double radius, Vector lookAt, Vector up) {
        super(focalLength, location);
        Shared.notNaNAndPositive(radius, "Parameter radius cannot be Not A Number!");
        Shared.notNull(lookAt, "Parameter lookAt cannot be null!");
        Shared.notNull(up, "Parameter up cannot be null!");
        this.radius = radius;
        this.lookAt = lookAt;
        this.up = up;
        this.right = lookAt.cross(up).normalized();
        this.normalizedUp = up.normalized();
    }

    public Point getSamplePoint() {
        // theta's range is [0, 2*Pi]
        double theta = Shared.random.nextDouble() * 2.0 * Math.PI;

        // r's range is [0, radius]
        double r = Shared.random.nextDouble() * radius;

        // x = r * cos(theta)
        double leftRightOffset = r * Math.cos(theta);

        // y = r * sin(theta)
        double upDownOffset = r * Math.sin(theta);

        return location.plus(right.scale(leftRightOffset)).plus(normalizedUp.scale(upDownOffset));
    }

}
