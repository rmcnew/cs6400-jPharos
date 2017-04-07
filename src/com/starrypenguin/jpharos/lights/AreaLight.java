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

package com.starrypenguin.jpharos.lights;

import com.starrypenguin.jpharos.core.Body;
import com.starrypenguin.jpharos.core.Intersection;
import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.geometry.BoundingBox;
import com.starrypenguin.jpharos.shapes.Shape;

/**
 * AreaLight
 * <p/>
 * Light source that has a shape
 */
public class AreaLight extends Light {

    private final Shape shape;

    public AreaLight(Shape shape) {
        super(shape.location);
        this.shape = shape;
    }

    public boolean IntersectsP(Ray ray) {
        return shape.IntersectsP(ray);
    }

    public Intersection Intersects(Ray ray, Body body) {
        return shape.Intersects(ray, body);
    }

    public BoundingBox getBoundingBox() {
        return shape.getBoundingBox();
    }

    public double surfaceArea() {
        return shape.surfaceArea();
    }
}
