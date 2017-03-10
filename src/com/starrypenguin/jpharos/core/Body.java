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

import com.starrypenguin.jpharos.geometry.BoundingBox;
import com.starrypenguin.jpharos.materials.Material;
import com.starrypenguin.jpharos.shapes.Shape;
import com.starrypenguin.jpharos.util.Shared;

/**
 * Body
 * <p/>
 * A Shape of a given ColorMaterial that is somewhere in a Scene
 */
public class Body {
    // value class; immutable and cannot be changed after being created
    public final Shape shape;
    public final Material material;

    public Body(Shape shape,  Material material) {
        Shared.notNull(shape, "shape cannot be null!");
        Shared.notNull(material, "material cannot be null!");
        this.shape = shape;
        this.material = material;
    }

    public boolean IntersectsP(Ray ray) {
        return shape.IntersectsP(ray);
    }

    public Intersection Intersects(Ray ray) {
        return shape.Intersects(ray, this);
    }

    public BoundingBox getBoundingBox() {
        return shape.getBoundingBox();
    }

    public double surfaceArea() {
        return shape.surfaceArea();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Body body = (Body) o;

        if (!shape.equals(body.shape)) return false;
        return material.equals(body.material);
    }

    @Override
    public int hashCode() {
        int result = shape.hashCode();
        result = 31 * result + material.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Body{" +
                "shape=" + shape +
                ", material=" + material +
                '}';
    }
}
