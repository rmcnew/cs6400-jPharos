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
import com.starrypenguin.jpharos.util.Shared;

/**
 * SquareLens
 * <p/>
 * A lens in the shape of a square
 */
public class SquareLens extends Lens {

    private final Point cameraLocation;
    private final double sideLength;
    //public final Rectangle square;

    public SquareLens(double focalLength, Point cameraLocation, double sideLength) {
        super(focalLength);
        Shared.notNull(cameraLocation, "Parameter cameraLocation cannot be null!");
        Shared.notNaNAndPositive(sideLength, "Parameter sideLength cannot be Not A Number!");
        this.cameraLocation = cameraLocation;
        this.sideLength = sideLength;

    }
}
