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

import com.starrypenguin.jpharos.core.Intersection;
import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.core.Scene;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.lenses.Lens;
import com.starrypenguin.jpharos.util.Shared;

import java.awt.*;

/**
 * Camera
 * <p/>
 * A Camera represents the place and direction of observation for the scene;
 * Cameras have a Lens which can change how light rays enter / exit the camera;
 * Cameras have Film which determine how image data is captured
 */
final public class Camera {

    public final Film film;
    public final Lens lens;
    public final Point cameraLocation;
    public final Vector lookAt;
    public final Vector up;

    public Camera(Film film, Lens lens, Point location, Vector lookAt, Vector up) {
        Shared.notNull(film, "film cannot be null!");
        Shared.notNull(lens, "lens cannot be null!");
        Shared.notNull(location, "location cannot be null!");
        Shared.notNull(lookAt, "lookAt cannot be null!");
        Shared.notNull(up, "up cannot be null!");

        this.film = film;
        this.lens = lens;
        this.cameraLocation = location;
        this.lookAt = lookAt;
        this.up = up;
    }

    public void render(Scene scene, String outFilename) {
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
     *          create a Intersection to record what the Ray hit (or did not hit)
     *
     */

        Point filmCenter = cameraLocation.plus(lookAt);
        Vector right = lookAt.cross(up).normalized();
        Vector left = right.inverse();
        Vector normalizedUp = up.normalized();
        double xOffset = film.pixelSize * film.filmWidthInPixels / 2;
        double yOffset = film.pixelSize * film.filmHeightInPixels / 2;
        Point topLeft = new Point(filmCenter.x - xOffset, filmCenter.y + yOffset, filmCenter.z);
        for (int xIndex = 0; xIndex < film.filmWidthInPixels; xIndex++) {
            for (int yIndex = 0; yIndex < film.filmHeightInPixels; yIndex++) {
                Point pixelLocation = new Point(topLeft.x + xIndex * film.pixelSize, topLeft.y - yIndex * film.pixelSize, filmCenter.z);
                Vector rayDirection = new Vector(cameraLocation, pixelLocation);
                Ray currentRay = new Ray(cameraLocation, rayDirection);
                // see what the ray hits
                Intersection maybeIntersection = scene.castRay(currentRay);
                if (maybeIntersection != null) {
                    film.capture(xIndex, yIndex, maybeIntersection.calculateLambertian());
                } else {
                    film.capture(xIndex, yIndex, Color.BLACK);
                }
            }
        }
        film.develop(outFilename);
    }

}
