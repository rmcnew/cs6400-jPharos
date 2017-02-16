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

import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.util.Shared;

/**
 * Ray
 * <p/>
 * The ray class represents a ray of light
 */
public class Ray {

    public final Point origin;
    public final Vector direction;
    public final double timeMax;

    public Ray(Point origin, Vector direction, double timeMax) {
        Shared.notNull(origin, "origin cannot be null!");
        Shared.notNull(direction, "direction cannot be null!");
        Shared.notNaNAndInclusiveRangeCheck(timeMax, 0.0, Double.POSITIVE_INFINITY, "timeMax must be in range 0 to positive Infinity!");
        this.origin = origin;
        this.direction = direction;
        this.timeMax = timeMax;
    }

    public Ray(Point origin, Vector direction) {
        this(origin, direction, Double.MAX_VALUE);
    }

    public Point atTime(double time) {
        return this.origin.plus(direction.scale(time));
    }
}
