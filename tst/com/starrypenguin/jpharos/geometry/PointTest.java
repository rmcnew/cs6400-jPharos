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

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * PointTest
 * <p/>
 * Tests for Point class
 */
public class PointTest {

    @Test
    public void toJSONTest() {
        Point p1 = new Point(1, 2, 3);
        System.out.println(Point.toJSON(p1));
    }

    @Test
    public void fromJSONTest() {
        Point p2 = new Point(2, 4, 6);
        String p2String = Point.toJSON(p2);
        System.out.println(p2String);
        Point p2b = Point.fromJSON(p2String);
        assertTrue(p2.equals(p2b));
    }

    @Test
    public void plusPointTest() {
        Point p3 = new Point(1, 1, 1);
        Point p4 = new Point(4, 1, 1);
        Point p5 = p3.plus(p4);
        System.out.println("p5 is: " + p5);
        assertTrue(p5.equals(new Point(5, 2, 2)));
    }

    @Test
    public void plusVectorTest() {
        Vector v1 = new Vector(1, 1, 1);
        Point p4 = new Point(4, 1, 1);
        Point p5 = p4.plus(v1);
        System.out.println("p5 is: " + p5);
        assertTrue(p5.equals(new Point(5, 2, 2)));
    }

    @Test
    public void minusVectorTest() {
        Vector v1 = new Vector(1, 1, 1);
        Point p4 = new Point(4, 1, 1);
        Point p5 = p4.minus(v1);
        System.out.println("p5 is: " + p5);
        assertTrue(p5.equals(new Point(3, 0, 0)));
    }

    @Test
    public void distanceTest() {
        Point p3 = new Point(1, 1, 1);
        Point p4 = new Point(4, 1, 1);
        double dist = Point.distance(p3, p4);
        System.out.println("distance is: " + dist);
        assertTrue(Double.compare(dist, 3.0) == 0);
    }
}
