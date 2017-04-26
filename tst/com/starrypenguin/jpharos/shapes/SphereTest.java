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

import com.starrypenguin.jpharos.geometry.Point;
import org.junit.Test;

/**
 * SphereTest
 * <p/>
 * Tests for Sphere
 */
public class SphereTest {

    @Test
    public void randomPointsOnSphereTest() {
        Point center = new Point(1, 2, -3);
        Sphere sphere = new Sphere(center, 10);
        Point randPoint1 = sphere.getSamplePoint();
        Point randPoint2 = sphere.getSamplePoint();
        Point randPoint3 = sphere.getSamplePoint();
        System.out.println("Random Point 1: " + randPoint1 + " is " + Point.distance(randPoint1, center) + " from the center");
        System.out.println("Random Point 2: " + randPoint2 + " is " + Point.distance(randPoint2, center) + " from the center");
        System.out.println("Random Point 3: " + randPoint3 + " is " + Point.distance(randPoint3, center) + " from the center");
    }
}
