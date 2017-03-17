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
import java.util.concurrent.Callable;


/**
 * CastRay
 * <p/>
 * Cast a ray, determine what happens and return a color
 */
public class CastRay implements Callable<Film.DevelopedPixel> {

    private Ray ray;

    public CastRay(Ray ray) {
        Shared.notNull(ray, "Parameter ray cannot be null!");
        this.ray = ray;
        jPharos.instance.executor.taskCount.incrementAndGet();
    }

    @Override
    public Film.DevelopedPixel call() throws Exception {
        Film.DevelopedPixel retVal;
        // see what the ray hits
        Intersection maybeIntersection = this.castRay(this.ray);
        if (maybeIntersection != null) {
            retVal = jPharos.instance.camera.film.newDevelopedPixel(ray.filmCoordinate, maybeIntersection.body.material.getColor(maybeIntersection));
        } else {
            // ray did not intersect, use background color
            retVal = jPharos.instance.camera.film.newDevelopedPixel(ray.filmCoordinate, Color.BLACK);
        }
        //System.out.println("CastRay generated DevelopedPixel=" + retVal);
        jPharos.instance.executor.taskCount.decrementAndGet();
        return retVal;
    }

    public Intersection castRay(Ray ray) {
        jPharos.instance.raysCast.incrementAndGet();
        Intersection maybeIntersection = jPharos.instance.scene.boundingVolumeHierarchy.castRay(ray);
        if (maybeIntersection != null) {
            jPharos.instance.raysHit.incrementAndGet();
        }
        //System.out.println("CastRay:  returned Intersection is: " + maybeIntersection);
        return maybeIntersection;
    }


}
