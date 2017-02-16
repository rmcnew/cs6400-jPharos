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

package com.starrypenguin.jpharos.shapes;

import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.core.RayImpact;
import com.starrypenguin.jpharos.geometry.BoundingBox;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.util.Shared;

/**
 * Sphere
 * <p/>
 * Shape that represents a sphere
 */
public class Sphere extends Shape {

    public final double radius;

    public Sphere(Point location, double radius) {
        super(location);
        Shared.notNaN(radius, "radius cannot be Not A Number!");
        this.radius = radius;
    }

    @Override
    public boolean ImpactsP(Ray ray) {
        return false;
    }

    @Override
    public RayImpact Impacts(Ray ray) {
        return null;
    }

    @Override
    public BoundingBox getBoundingBox() {
        Point max = new Point(location.x + radius, location.y + radius, location.z + radius);
        Point min = new Point(location.x - radius, location.y - radius, location.z - radius);
        return new BoundingBox(max, min);
    }

    @Override
    public double surfaceArea() {
        return 4.0 * Math.PI * radius * radius;
    }
}
