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
import com.starrypenguin.jpharos.geometry.Normal;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.util.Shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Sphere
 * <p/>
 * Shape that represents a sphere
 */
final public class Sphere extends Shape {

    public final double radius;

    public Sphere(Point location, double radius) {
        super(location);
        Shared.notNaN(radius, "radius cannot be Not A Number!");
        this.radius = radius;
    }

    @Override
    public boolean IntersectsP(Ray ray) {
        List<Double> intersectionTimes = getIntersectionTimes(ray);
        return !intersectionTimes.isEmpty();
    }

    /**
     * The implicit representation of a sphere is:  x^2 + y^2 + z^2 = r^2  where r is the radius of the sphere
     *
     * The parametric form for the ray is:  o + dt  where o is the origin Point, d is the direction Vector, and t is the time (scalar)
     * The ray can be deconstructed into x, y, and z components to give:
     *         o_x + (d_x)t,  o_y + (d_y)t,  o_z + (d_z)t         Note that the underscore _ indicates a subscript so o_x is "o sub x"
     *
     * Substituting the ray components into the implicit sphere equation gives:  (o_x + (d_x)t)^2 + (o_y + (d_y)t)^2 + (o_z + (d_z)t)^2 = r^2
     *
     * The equation can be expanded and then we can gather coefficients to fit the quadratic equation for t:  at^2 + bt + c = 0
     * where:  a = (d_x)^2 + (d_y)^2 + (d_z)^2
     *         b = 2( (d_x)(o_x) + (d_y)(o_y) + (d_z)(o_z) )
     *         c = (o_x)^2 + (o_y)^2 + (o_z)^2 - r^2
     *
     * This will allow the quadratic formula to be used to find the minimum root in t, which corresponds to the intersection of the ray with the sphere
     * If there are no real roots, then no intersection occurs and we return null
     *
     * Reference:  "Physically Based Rendering", Third Edition, Section 3.2 "Spheres"
     *
     * Updated to use vector forms for simpler code
     * Reference: https://en.wikipedia.org/wiki/Line%E2%80%93sphere_intersection
     * @param ray
     * @return
     */
     private List<Double> getIntersectionTimes(Ray ray) {
         double a = ray.direction.dot(ray.direction);
         Vector temp = new Vector(location, ray.origin);
         double b = 2 * ray.direction.dot(temp);
         double c = temp.dot(temp) - (radius * radius);
         //System.out.println("Finding quadratic roots for a=" + a + ", b=" + b + ",c=" + c);
         List<Double> roots = Shared.findQuadraticRoots(a, b, c);
         List<Double> results = new ArrayList<>(2);
         for (Double root : roots) {
             if (!Double.isNaN(root) && (root >= 0.0)) {
                 results.add(root);
             }
         }
         return results;
     }

    public List<Point> getIntersectionPoints(Ray ray) {
        List<Point> intersectionPoints = new ArrayList<>(2);
        List<Double> intersectionTimes = getIntersectionTimes(ray);
        for (double intersectionTime : intersectionTimes) {
            Point intersectionPoint = ray.atTime(intersectionTime);
            intersectionPoints.add(intersectionPoint);
        }
        return intersectionPoints;
    }

    @Override
    public Intersection Intersects(Ray ray, Body body) {
        List<Double> intersectionTimes = getIntersectionTimes(ray);
        Intersection intersection = null;
        if (intersectionTimes.size() > 0) {
            // calculate surface normal for intersection
            double intersectionTime = intersectionTimes.get(0);
            if (Double.isNaN(intersectionTime) && intersectionTimes.get(1) != null) {
                intersectionTime = intersectionTimes.get(1);
            }
            if (!Double.isNaN(intersectionTime)) {
                Point intersectionPoint = ray.atTime(intersectionTime);
                Normal surfaceNormal = new Normal(location, intersectionPoint);
                intersection = new Intersection(ray, intersectionTime, surfaceNormal, intersectionPoint, body);
            }
        }
        return intersection;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sphere sphere = (Sphere) o;

        return Double.compare(sphere.radius, radius) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(radius);
        return (int) (temp ^ (temp >>> 32));
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "radius=" + radius +
                "} " + super.toString();
    }
}
