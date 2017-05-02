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

package com.starrypenguin.jpharos.core;

import com.starrypenguin.jpharos.cameras.Film;
import com.starrypenguin.jpharos.main.jPharos;
import com.starrypenguin.jpharos.util.Shared;

import java.awt.*;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;


/**
 * CastRayForDevelopedPixel
 * <p/>
 * Cast a ray, determine what happens and get a color for a given pixel
 */
public class CastRayForDevelopedPixel implements Callable<Film.DevelopedPixel> {

    private Ray ray;

    public CastRayForDevelopedPixel(Ray ray) {
        Shared.notNull(ray, "Parameter ray cannot be null!");
        this.ray = ray;
    }

    @Override
    public Film.DevelopedPixel call() throws Exception {
        Film.DevelopedPixel retVal;
        // see what the ray hits
        //System.out.println("Thread " + Thread.currentThread().getId() + " running CastRay.call() for Ray " + ray);
        CastRayForIntersection castRayForIntersection = new CastRayForIntersection(this.ray);
        Future<Intersection> futureIntersection = jPharos.instance.executor.castRayForFutureIntersection(castRayForIntersection);
        Intersection maybeIntersection = futureIntersection.get();
        if (maybeIntersection != null) {
            jPharos.instance.raysHit.incrementAndGet();
            retVal = jPharos.instance.camera.film.newDevelopedPixel(ray.filmCoordinate, maybeIntersection.body.material.getColor(maybeIntersection));
        } else {
            // ray did not intersect, use background color
            Queue<Color> colors = new ConcurrentLinkedQueue<>();
            colors.add(Color.BLACK);
            retVal = jPharos.instance.camera.film.newDevelopedPixel(ray.filmCoordinate, colors);
        }
        //System.out.println("Thread " + Thread.currentThread().getId() + "ran CastRay.call() and generated DevelopedPixel=" + retVal);
        return retVal;
    }



}
