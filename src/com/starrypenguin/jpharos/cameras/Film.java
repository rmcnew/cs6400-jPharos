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

package com.starrypenguin.jpharos.cameras;

/**
 * Film
 * <p/>
 * Represents the film in the camera
 */
public class Film {

    double pixelSize;  // pixels are square with side length of pixelSize
    int xPixels, yPixels; // film size in pixels

    // film center = camera location + lookAt * focalLength


    /**
     * Write the finished film out to disk
     */
    public void develop() {

    }
}
