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

import com.starrypenguin.jpharos.core.Body;
import com.starrypenguin.jpharos.core.Intersection;
import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.shapes.Shape;
import com.starrypenguin.jpharos.shapes.Triangle;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.shapes.TriangleMeshBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * BoundingBox
 * <p/>
 * Axis-aligned bounding box used for accelerating ray impact detection
 */
public class BoundingBox extends Shape {

    public final Point max;  // the point with the highest x, y, and z values calculated from the supplied corner Points
    public final Point min;  // the point with the lowest x, y, and z values calculated from the supplied corner Points
    private Point center = null; // if we calculate the center Point, cache it for reuse
    private TriangleMesh triangleMesh = null;  // if we create the TriangleMesh for this BoundingBox, cache it for reuse

    public BoundingBox(Point pA, Point pB) {
        super(Point.linearInterpolate(0.5, pA, pB));
        // calculate the max point and min point using the supplied corner Points
        max = new Point(Math.max(pA.x, pB.x), Math.max(pA.y, pB.y), Math.max(pA.z, pB.z));
        min = new Point(Math.min(pA.x, pB.x), Math.min(pA.y, pB.y), Math.min(pA.z, pB.z));
    }

    public boolean containsPoint(Point point) {
        return (min.x <= point.x) && (point.x <= max.x) &&
               (min.y <= point.y) && (point.y <= max.y) &&
               (min.z <= point.z) && (point.z <= max.z);
    }

    public List<Rectangle> toRectangles() {
        List<Rectangle> rectangles = new ArrayList<>(6);
        Point pA = min;
        Point pB = max;
        Point pC = new Point(pB.x, pA.y, pA.z);
        Point pD = new Point(pB.x, pB.y, pA.z);
        Point pE = new Point(pA.x, pB.y, pB.z);
        Point pF = new Point(pA.x, pA.y, pB.z);
        Point pG = new Point(pB.x, pA.y, pB.z);
        Point pH = new Point(pA.x, pB.y, pA.z);
        rectangles.add(new Rectangle(pG, pC, pA, pF));
        rectangles.add(new Rectangle(pB, pD, pC, pG));
        rectangles.add(new Rectangle(pE, pH, pD, pB));
        rectangles.add(new Rectangle(pF, pA, pH, pE));
        rectangles.add(new Rectangle(pB, pG, pF, pE));
        rectangles.add(new Rectangle(pC, pD, pH, pA));
        return rectangles;
    }

    public synchronized TriangleMesh getTriangleMesh() {
        if (triangleMesh == null) {
            TriangleMeshBuilder builder = new TriangleMeshBuilder();
            for (Rectangle rectangle : toRectangles()) {
                for (Triangle triangle : rectangle.toTriangles()) {
                    builder.addTriangle(triangle.v1, triangle.v2, triangle.v3);
                }
            }
            triangleMesh =  builder.build();
        }
        return triangleMesh;
    }

    public BoundingBox union(BoundingBox boundingBox) {
        Point max = new Point(Math.max(this.max.x, boundingBox.max.x), Math.max(this.max.y, boundingBox.max.y), Math.max(this.max.z, boundingBox.max.z));
        Point min = new Point(Math.min(this.min.x, boundingBox.min.x), Math.min(this.min.y, boundingBox.min.y), Math.min(this.min.z, boundingBox.min.z));
        return new BoundingBox(max, min);
    }

    public synchronized Point getCenterPoint() {
        if (center == null) {
            center = new Point((this.min.x + this.max.x) / 2.0, (this.min.y + this.max.y) / 2.0, (this.min.z + this.max.z) / 2.0);
        }
        return center;
    }

    @Override
    public boolean IntersectsP(Ray ray) {
        return getTriangleMesh().IntersectsP(ray);
    }

    @Override
    public Intersection Intersects(Ray ray, Body body) {
        return getTriangleMesh().Intersects(ray, body);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this;
    }

    @Override
    public double surfaceArea() {
        return getTriangleMesh().surfaceArea();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoundingBox that = (BoundingBox) o;

        if (!max.equals(that.max)) return false;
        return min.equals(that.min);
    }

    @Override
    public int hashCode() {
        int result = max.hashCode();
        result = 31 * result + min.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "max=" + max +
                ", min=" + min +
                '}';
    }


}
