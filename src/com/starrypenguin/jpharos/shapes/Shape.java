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

import com.starrypenguin.jpharos.core.Intersection;
import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.geometry.BoundingBox;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.materials.Material;
import com.starrypenguin.jpharos.util.Shared;

/**
 * Shape
 * <p/>
 * A 3D geometric entity with a given position and volume that is used to determine
 * if, where, and when light interacts with a Body
 */
public abstract class Shape {
    // value class; immutable and cannot be changed after being created
    public final Point location;

    public Shape(Point location) {
        Shared.notNull(location, "location cannot be null!");
        this.location = location;
    }

    public abstract boolean IntersectsP(Ray ray);

    public abstract Intersection Intersects(Ray ray, Material material);

    public abstract BoundingBox getBoundingBox();

    public abstract double surfaceArea();
}
