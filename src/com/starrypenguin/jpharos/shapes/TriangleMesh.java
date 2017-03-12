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

import com.starrypenguin.jpharos.core.Body;
import com.starrypenguin.jpharos.core.Intersection;
import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.geometry.BoundingBox;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.util.Shared;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TriangleMesh
 * <p/>
 * Represents a group of interconnected triangles (mesh) that is used
 * to represent complex geometries
 */
public class TriangleMesh extends Shape {

    TriangleMeshVertices vertices;

    public TriangleMesh(Point centroid, TriangleMeshVertices vertices) {
        super(centroid);
        Shared.notNull(vertices, "vertices parameter cannot be null!");
        this.vertices = vertices;
    }

    @Override
    public boolean IntersectsP(Ray ray) {
        return vertices.parallelStream().anyMatch(triangle -> triangle.IntersectsP(ray));
    }

    private List<Intersection> getIntersections(Ray ray, Body body) {
        return vertices.parallelStream().map(triangle -> triangle.Intersects(ray, body)).filter(Objects::nonNull).sorted().collect(Collectors.toList());
    }

    @Override
    public Intersection Intersects(Ray ray, Body body) {
        Intersection retVal = null;
        List<Intersection> intersections = getIntersections(ray, body);
        if (!intersections.isEmpty()) {
            retVal = intersections.get(0);
        }
        return retVal;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return vertices.getBoundingBox();
    }

    @Override
    public double surfaceArea() {
        return vertices.getSurfaceArea();
    }

    public Map<Point, Color> getColorMap() {
        return vertices.getColorMap();
    }
}
