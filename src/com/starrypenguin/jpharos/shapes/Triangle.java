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
import com.starrypenguin.jpharos.geometry.*;
import com.starrypenguin.jpharos.util.Shared;

/**
 * Triangle
 * <p/>
 * Shape that represents a triangle
 */
public class Triangle extends Shape {

    private final static double EPSILON = 0.000001;
    public final Point v1;
    public final Point v2;
    public final Point v3;

    public Triangle(Point v1, Point v2, Point v3) {
        super(calculateTriangleCentroid(v1, v2, v3));
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    /**
     * Ensure that the provided Points represent valid vertices for a triangle
     * Specifically:
     * all Points are valid Points (not null, no NaNs)
     * each vertex is unique (not equal to one of the other two vertices)
     *
     * @param v1 Point for the first triangle vertex
     * @param v2 Point for the second triangle vertex
     * @param v3 Point for the third triangle vertex
     */
    private static void trianglePointsValid(Point v1, Point v2, Point v3) {
        Shared.notNull(v1, "Parameter v1 cannot be null!");
        Shared.notNull(v2, "Parameter v2 cannot be null!");
        Shared.notNull(v3, "Parameter v3 cannot be null!");
        if (v1.equals(v2)) {
            throw new IllegalArgumentException("Invalid triangle!  v1 equals v2");
        }
        if (v1.equals(v3)) {
            throw new IllegalArgumentException("Invalid triangle!  v1 equals v3");
        }
        if (v2.equals(v3)) {
            throw new IllegalArgumentException("Invalid triangle!  v2 equals v3");
        }
    }

    /**
     * Calculate the triangle's centroid given the vertices
     * Given the coordinates of the three vertices of a triangle ABC, the centroid O coordinates are given by:
     * O_x = (A_x + B_x + C_x) / 3
     * O_y = (A_y + B_y + C_y) / 3
     * O_z = (A_z + B_z + C_z) / 3
     * <p>
     * Reference:  http://www.mathopenref.com/coordcentroid.html
     *
     * @param v1 Point for the first triangle vertex
     * @param v2 Point for the second triangle vertex
     * @param v3 Point for the third triangle vertex
     * @return Point for the triangle's centroid
     */
    private static Point calculateTriangleCentroid(Point v1, Point v2, Point v3) {
        // first make sure triangle is sane
        trianglePointsValid(v1, v2, v3);
        // now determine the centroid coordinates
        double x = (v1.x + v2.x + v3.x) / 3.0;
        double y = (v1.y + v2.y + v3.y) / 3.0;
        double z = (v1.z + v2.z + v3.z) / 3.0;
        return new Point(x, y, z);
    }

    /**
     * Use the Möller–Trumbore intersection algorithm to quickly determine
     * if the ray intersects the triangle
     *
     * Reference:  https://en.wikipedia.org/wiki/M%C3%B6ller%E2%80%93Trumbore_intersection_algorithm
     * @param ray the Ray to test for intersection
     * @return Double with the time that the intersection occurs or null if there is no intersection
     */
    private Double determineIntersection(Ray ray) {
        Double intersectionTime = null;
        Vector edge1, edge2, P, Q, T;
        double det, invDet, u, v, t;

        // Find vectors for two edges sharing V1
        edge1 = new Vector(v1, v2);
        edge2 = new Vector(v1, v3);
        // Begin calculating determinant - also used to calculate u parameter
        P = ray.direction.cross(edge2);
        // if determinant is near zero, ray lies in plane of triangle or ray is parallel to plane of triangle
        det = edge1.dot(P);
        if (det > -EPSILON && det < EPSILON) {
            return intersectionTime;
        }

        invDet = 1.0 / det;
        // Calculate distance from V1 to ray origin
        T = new Vector(v1, ray.origin);
        // Calculate u parameter and test bound
        u = T.dot(P) * invDet;
        //The intersection lies outside of the triangle
        if (u < 0.0 || u > 1.0) {
            return intersectionTime;
        }

        //Prepare to test v parameter
        Q = T.cross(edge1);
        //Calculate V parameter and test bound
        v = ray.direction.dot(Q) * invDet;
        //The intersection lies outside of the triangle
        if (v < 0.0 || (u+v) > 1.0) {
            return intersectionTime;
        }

        t = edge2.dot(Q)*invDet;

        if (t > EPSILON) {
            intersectionTime = t;
            return intersectionTime;
        }
        return intersectionTime;
    }

    @Override
    public boolean IntersectsP(Ray ray) {
        return determineIntersection(ray) != null;
    }

    @Override
    public Intersection Intersects(Ray ray, Body body) {
        Intersection intersection = null;
        Double intersectionTime = determineIntersection(ray);
        if (intersectionTime != null) {
            Point intersectionPoint = ray.atTime(intersectionTime);
            intersection = new Intersection(ray, intersectionTime, getSurfaceNormal(), intersectionPoint, body);
            //System.out.println("Intersected with triangle: " + intersection);
        }
        return intersection;
    }

    @Override
    public BoundingBox getBoundingBox() {
        Point max = Point.max(Point.max(v1, v2), v3);
        Point min = Point.min(Point.min(v1, v2), v3);
        return new BoundingBox(max, min);
    }

    private Normal getSurfaceNormal() {
        Vector vec1 = new Vector(v1, v2);
        Vector vec2 = new Vector(v1, v3);
        return Normal.fromVector(vec1.cross(vec2));
    }

    /**
     * Use Heron's Formula to calculate the triangle's area:
     *   A = sqrt( s(s-a)(s-b)(s-c) )
     * where s is the semiperimeter given by:
     *   s = (a + b + c) / 2
     * and a, b, and c are the lengths of the triangle's sides
     *
     * Reference:  https://en.wikipedia.org/wiki/Heron%27s_formula
     *
     * @return double for the triangle's area
     */
    @Override
    public double surfaceArea() {
        double a = Point.distance(v1, v2);
        double b = Point.distance(v2, v3);
        double c = Point.distance(v3, v1);

        double s = (a + b + c) / 2.0;
        return Math.sqrt( s * (s-a) * (s-b) * (s-c) );
    }

    public BarycentricCoordinate calculateBarycentricCoordinateForPoint(Point point) {
        Shared.notNull(point, "Paramter point cannot be null!");
        Vector vector0 = new Vector(this.v1, this.v2);
        Vector vector1 = new Vector(this.v1, this.v3);
        Vector vector2 = new Vector(this.v1, point);
        double d00 = vector0.dot(vector0);
        double d01 = vector0.dot(vector1);
        double d11 = vector1.dot(vector1);
        double d20 = vector2.dot(vector0);
        double d21 = vector2.dot(vector1);
        double denominator = d00 * d11 - d01 * d01;
        double v = (d11 * d20 - d01 * d21) / denominator;
        double w = (d00 * d21 - d01 * d20) / denominator;
        double u = 1.0 - v - w;
        return new BarycentricCoordinate(u, v, w);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triangle triangle = (Triangle) o;

        if (!v1.equals(triangle.v1)) return false;
        if (!v2.equals(triangle.v2)) return false;
        return v3.equals(triangle.v3);
    }

    @Override
    public int hashCode() {
        int result = v1.hashCode();
        result = 31 * result + v2.hashCode();
        result = 31 * result + v3.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "v1=" + v1 +
                ", v2=" + v2 +
                ", v3=" + v3 +
                "} " + super.toString();
    }
}
