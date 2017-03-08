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

import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.lenses.Lens;
import com.starrypenguin.jpharos.util.Shared;

import java.util.LinkedList;
import java.util.List;

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

    public List<Ray> generateRays() {
        LinkedList<Ray> rays = new LinkedList<>();
        Point filmCenter = cameraLocation.plus(lookAt.normalized().scale(lens.focalLength));
        Vector right = lookAt.cross(up).normalized();
        Vector left = right.inverse();
        Vector normalizedUp = up.normalized();
        Vector down = normalizedUp.inverse();
        double leftRightOffset = film.pixelSize * film.filmWidthInPixels / 2;
        double upDownOffset = film.pixelSize * film.filmHeightInPixels / 2;
        Point topLeft = filmCenter.plus(normalizedUp.scale(upDownOffset)).plus(left.scale(leftRightOffset));

        for (int upDownIndex = 0; upDownIndex < film.filmHeightInPixels; upDownIndex++) {
            for (int leftRightIndex = 0; leftRightIndex < film.filmWidthInPixels; leftRightIndex++) {
                Point pixelLocation = topLeft.plus(right.scale(film.pixelSize * leftRightIndex)).plus(down.scale(film.pixelSize * upDownIndex));
                Vector rayDirection = new Vector(cameraLocation, pixelLocation);
                Ray currentRay = new Ray(cameraLocation, rayDirection, film.newFilmCoordinate(upDownIndex, leftRightIndex));
                rays.add(currentRay);
            }
        }
        return rays;
    }

    public void develop(String outFilename) {
        film.develop(outFilename);
    }

}
