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

package com.starrypenguin.jpharos.util;

import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;
import org.junit.Test;

import java.util.Set;

/**
 * SharedTest
 * <p/>
 * Tests for the Shared utility methods
 */
public class SharedTest {

    @Test
    public void perturbRayTest() {
        // Create a ray
        Point origin = new Point(0, 0, 0);
        Vector vector = new Vector(1, 0, 0);
        Ray ray = new Ray(origin, vector);

        // print it out
        System.out.println("Original Ray: " + ray);

        // perturb the ray
        Set<Ray> rays = Shared.perturbRay(ray);

        // print out the perturbed rays
        System.out.println("Perturbed Rays:");
        for (Ray pRay : rays) {
            System.out.println(pRay);
        }

    }
}
