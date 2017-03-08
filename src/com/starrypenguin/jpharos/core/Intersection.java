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


/**
 * Intersection
 * <p/>
 * Represents the data from when a Ray hits a Body
 */
final public class Intersection implements Comparable<Intersection> {

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

    @Override
    public int compareTo(Intersection intersection) {
        return Double.compare(this.intersectionTime, intersection.intersectionTime);
    }
}
