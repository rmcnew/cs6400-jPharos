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

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * VectorTest
 * <p/>
 * Tests for the Vector class
 */
public class VectorTest {

    @Test
    public void vectorFromPoints() {
        Point from = new Point(1, 2, 3);
        Point to = new Point(4, 7, 10);
        Vector vector = new Vector(from, to);
        assertTrue(vector.equals(new Vector(3, 5, 7)));
    }

    @Test
    public void plusTest() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 7, 10);
        Vector v3 = v1.plus(v2);
        assertTrue(v3.equals(new Vector(5, 9, 13)));
    }

    @Test
    public void minusTest() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 7, 10);
        Vector v3 = v2.minus(v1);
        assertTrue(v3.equals(new Vector(3, 5, 7)));
    }

    @Test
    public void magnitudeTest() {
        Vector v1 = new Vector(3, 4, 0);
        double mag = v1.magnitude();
        assertTrue(Double.compare(mag, 5.0) == 0);
    }

    @Test
    public void scaleTest() {
        Vector v1 = new Vector(3, 4, 0);
        Vector v2 = v1.scale(2.0);
        assertTrue(v2.equals(new Vector(6, 8, 0)));
    }

    @Test
    public void orthogonalTest() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 7, 10);
        boolean ortho1 = v1.isOrthogonalTo(v2);
        assertFalse(ortho1);

        Vector v3 = new Vector(1, 0, 0);
        Vector v4 = new Vector(0, 1, 0);
        boolean ortho2 = v3.isOrthogonalTo(v4);
        assertTrue(ortho2);
    }

    @Test
    public void crossTest() {
        Vector v3 = new Vector(1, 0, 0);
        Vector v4 = new Vector(0, 1, 0);
        Vector v5 = v3.cross(v4);
        assertTrue(v5.equals(new Vector(0, 0, 1)));
    }

    @Test
    public void inverseTest() {
        Vector v1 = new Vector(4, 7, 10);
        Vector v2 = v1.inverse();
        assertTrue(v2.equals(new Vector(-4, -7, -10)));
    }

    @Test
    public void normalizedTest() {
        Vector v1 = new Vector(2, 2, 2);
        Vector v2 = v1.normalized();
        Vector v3 = new Vector(0.5, 0, 0);
        Vector v4 = v2.normalized();
        double v1Length = v1.magnitude();
        double v2Length = v2.magnitude();
        double v3Length = v3.magnitude();
        double v4Length = v4.magnitude();
        System.out.println("v1 is " + v1 + ", v1 magnitude is " + v1Length);
        System.out.println("v2 is " + v2 + ", v2 magnitude is " + v2Length);
        System.out.println("v3 is " + v3 + ", v3 magnitude is " + v3Length);
        System.out.println("v4 is " + v4 + ", v4 magnitude is " + v4Length);
    }
}
