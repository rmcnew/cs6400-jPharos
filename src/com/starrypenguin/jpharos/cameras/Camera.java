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

import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;

/**
 * Camera
 * <p/>
 * A Camera represents the place and direction of observation for the scene;
 * Cameras have a Lens which can change how light rays enter / exit the camera;
 * Cameras have Film which determine how image data is captured
 */
public abstract class Camera {

    private Lens lens;
    private Film film;
    private Point location;
    private Vector lookAt;
    private Vector up;

    /*
     * Rendering:
     * filmCenter = location + lookAt;
     * v = cross(lookAt, up);
     *
     *
     * for each xPixel in Film.xPixels {
     *     for each yPixel in Film.yPixels {
     *          get Point for Film pixel location based on current grid location and pixel size
     *          cast a ray from Film pixel location to lens
     *          create a RayImpact to record what the Ray hit (or did not hit)
     *
     */
}
