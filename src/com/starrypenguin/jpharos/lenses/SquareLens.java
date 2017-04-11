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
import com.starrypenguin.jpharos.main.jPharos;
import com.starrypenguin.jpharos.util.Shared;

/**
 * SquareLens
 * <p/>
 * A lens in the shape of a square
 */
public class SquareLens extends Lens {

    private final double focusKnob; // range 0.0 to 2.0 inclusive;
    private final double sideLength;
    //public final Rectangle square;

    public SquareLens(double focalLength, double focusKnob, double sideLength) {
        super(focalLength);
        Shared.notNaNAndInclusiveRangeCheck(focusKnob, 0.0, 2.0, "Parameter focusKnob must be between 0.0 and 2.0 inclusive!");
        Shared.notNaNAndPositive(sideLength, "Parameter sideLength cannot be Not A Number!");
        this.focusKnob = focusKnob;
        this.sideLength = sideLength;
        // calculate z'
        double zPrime = solveForZPrime();
        // determine lens location
        Point cameraLocation = jPharos.instance.camera.cameraLocation;
        Vector lookAt = jPharos.instance.camera.lookAt;
        Vector up = jPharos.instance.camera.up;
        Point lensCenter = cameraLocation.plus(lookAt.normalized().scale(zPrime));
        Vector right = lookAt.cross(up).normalized();
        Vector left = right.inverse();
        Vector normalizedUp = up.normalized();
        Vector down = normalizedUp.inverse();

        // place lens shape
    }

    /**
     * Use the Gaussian lens equation to determine focus:
     * Z = focusKnob * focalLength
     * Z' = (focalLength * Z) / (focalLength + Z)
     *
     * @return zPrime, the distance from the lens to the film
     */
    private double solveForZPrime() {
        double Z = this.focusKnob * this.focalLength;
        return (focalLength * Z) / (focalLength + Z);
    }
}
