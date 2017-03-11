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
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.lights.Light;
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
            retVal = jPharos.instance.camera.film.newDevelopedPixel(ray.filmCoordinate, calculateLambertianAndShadow(maybeIntersection));
        } else {
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
        return maybeIntersection;
    }

    /**
     * Calculate the Lambertian lighting and shadows
     *
     * @return Color with darkness values adjusted
     */
    private Color calculateLambertianAndShadow(Intersection intersection) {
        // see if this intersection point is in the shadows:  can we cast rays to a light source?
        for (Light light : jPharos.instance.scene.lights) {
            Vector directionToLight = new Vector(intersection.intersectionPoint, light.location);
            Ray towardLight = new Ray(intersection.intersectionPoint, directionToLight);
            Intersection maybeIntersection = castRay(towardLight);
            if (maybeIntersection != null && maybeIntersection.body != intersection.body) {  // we hit something, a shadow is here
                return Color.BLACK.brighter();
            }
        }
        double rawLambert = Math.max(ray.direction.dot(intersection.surfaceNormal), 0.0);
        double maxValue = ray.direction.magnitude() * intersection.surfaceNormal.magnitude();
        double scaledLambert = rawLambert / maxValue;
        float factor = (float) (1.0 - scaledLambert);
        float red = (float) (intersection.body.material.color.getRed() / 255.0) * factor;
        float green = (float) (intersection.body.material.color.getGreen() / 255.0) * factor;
        float blue = (float) (intersection.body.material.color.getBlue() / 255.0) * factor;
        return new Color(red, green, blue);
    }
}
