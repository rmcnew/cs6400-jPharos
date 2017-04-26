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

package com.starrypenguin.jpharos.lenses;

import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;
import org.junit.Test;

/**
 * ThinLensTest
 * <p/>
 * Tests for ThinLens
 */
public class ThinLensTest {

    @Test
    public void TestSampling() {
        Point loc = new Point(0, -5, 5);
        Vector lookAt = new Vector(0, 1, 0);
        Vector up = new Vector(0, 0, 1);
        ThinLens thinLens = new ThinLens(40, loc, 5, lookAt, up);
        Point samp1 = thinLens.getSamplePoint();
        Point samp2 = thinLens.getSamplePoint();
        Point samp3 = thinLens.getSamplePoint();
        System.out.println("Sample point 1 is: " + samp1);
        System.out.println("Sample point 2 is: " + samp2);
        System.out.println("Sample point 3 is: " + samp3);
    }
}
