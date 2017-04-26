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

/**
 * Lens
 * <p/>
 * Changes the directions of Rays entering / exiting the Camera before hitting the Film
 */

import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.util.Shared;

public abstract class Lens {

    final public double focalLength;
    final public Point location;

    public Lens(double focalLength, Point location) {
        Shared.notNaN(focalLength, "Parameter focalLength cannot be Not A Number!");
        Shared.notNull(location, "Parameter location cannot be null!");
        this.focalLength = focalLength;
        this.location = location;
    }

    public Point getSamplePoint() {
        return location;
    }
}
