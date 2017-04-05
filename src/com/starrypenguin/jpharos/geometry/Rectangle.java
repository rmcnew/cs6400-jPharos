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

package com.starrypenguin.jpharos.geometry;

import com.starrypenguin.jpharos.shapes.Triangle;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.shapes.TriangleMeshBuilder;
import com.starrypenguin.jpharos.util.Shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Rectangle
 * <p/>
 * Represents a rectangle
 */
public class Rectangle {
    // We assume Points are ordered as shown:
    //
    //  p4-----------p1
    //  |             |
    //  |             |
    //  |             |
    //  |             |
    //  |             |
    //  p3-----------p2
    //
    final Point p1, p2, p3, p4;

    Rectangle(Point p1, Point p2, Point p3, Point p4) {
        // make sure Point are not null
        Shared.notNull(p1, "Parameter p1 cannot be null!");
        Shared.notNull(p2, "Parameter p2 cannot be null!");
        Shared.notNull(p3, "Parameter p3 cannot be null!");
        Shared.notNull(p4, "Parameter p4 cannot be null!");
        //System.out.println("Attempting to create Rectangle with points:  p1=" + p1 + ", p2=" + p2 + ", p3=" + p3 + ", p4=" + p4);
        // make sure Points are unique
        if (p1.equals(p2) || p1.equals(p3) || p1.equals(p4) ||
                p2.equals(p3) || p2.equals(p4) || p3.equals(p4)) {
            throw new IllegalArgumentException("All Rectangle points must be unique!");
        }
        // make sure points are pairwise equidistant
        double distP1P2 = Point.distance(p1, p2);
        double distP2P3 = Point.distance(p2, p3);
        double distP3P4 = Point.distance(p3, p4);
        double distP4P1 = Point.distance(p4, p1);
        if ((Double.compare(distP1P2, distP3P4) != 0) ||
                (Double.compare(distP2P3, distP4P1) != 0)) {
            throw new IllegalArgumentException("Rectangular legs must be pairwise equidistant!");
        }
        // skip right angle tests
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public List<Triangle> toTriangles() {
        List<Triangle> triangles = new ArrayList<>(2);
        triangles.add(new Triangle(p1, p2, p3));
        triangles.add(new Triangle(p1, p3, p4));
        return triangles;
    }

    public TriangleMesh toTriangleMesh() {
        TriangleMeshBuilder triangleMeshBuilder = new TriangleMeshBuilder();
        List<Triangle> triangles = toTriangles();
        for (Triangle triangle : triangles) {
            triangleMeshBuilder.addTriangle(triangle.v1, triangle.v2, triangle.v3);
        }
        return triangleMeshBuilder.build();
    }
}