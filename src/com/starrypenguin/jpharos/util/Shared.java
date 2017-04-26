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

package com.starrypenguin.jpharos.util;

import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;

import java.io.File;
import java.util.*;

/**
 * Shared
 * <p/>
 * Shared constants and general utility functions
 */
final public class Shared {

    final public static Random random = new Random();
    final public static int DIFFERENTIALS_PER_RAY = 15;
    final public static double LENGTH_PERCENTAGE = 3;
    // methods

    public static void notNullAndNotEmpty(String str, String errorMessage) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void notNullExistsAndReadable(File file, String errorMessage) {
        if (file == null || !file.exists() || !file.canRead()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void notNull(Object obj, String errorMessage) {
        if (obj == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void notNaN(double value, String errorMessage) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void notNaNAndPositive(double value, String errorMessage) {
        if (Double.isNaN(value) || Double.compare(value, 0.0) < 0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // ensure value is not NaN;  ensure  min <= value <= max
    public static void notNaNAndInclusiveRangeCheck(double value, double min, double max, String errorMessage) {
        if (Double.isNaN(value) || (value < min) || (value > max)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // ensure value is not NaN;  ensure  min < value < max
    public static void notNaNAndExclusiveRangeCheck(double value, double min, double max, String errorMessage) {
        if (Double.isNaN(value) || (value <= min) || (value >= max)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void notNaN3D(double x, double y, double z, String errorMessage) {
        if (Double.isNaN(x)) {
            throw new IllegalArgumentException("Parameter x cannot be Not a Number!  " + errorMessage);
        }
        if (Double.isNaN(y)) {
            throw new IllegalArgumentException("Parameter y cannot be Not a Number!  " + errorMessage);
        }
        if (Double.isNaN(z)) {
            throw new IllegalArgumentException("Parameter z cannot be Not a Number!  " + errorMessage);
        }
    }

    // ensure  min <= value <= max
    public static void inclusiveRangeCheck(int value, int min, int max, String errorMessage) {
        if ((min > value) || (value > max)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // ensure  min < value < max
    public static void exclusiveRangeCheck(int value, int min, int max, String errorMessage) {
        if ((min >= value) || (value >= max)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void notNullAndNotEmpty(Collection collection, String errorMessage) {
        if ((collection == null) || (collection.isEmpty())) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void positive(int value, String errorMessage) {
        if (value <= 0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Use the quadratic equation to solve a second-degree polynomial:  ax^2 + bx + c = 0
     *
     * @param a the coefficient for the squared term: ax^2
     * @param b the coefficient for the linear term: bx
     * @param c the coefficient for the constant term: c
     * @return a sorted list of roots in order least to greatest, if any; the list may be empty (no real roots), or have one or two roots
     */
    public static List<Double> findQuadraticRoots(double a, double b, double c) {
        notNaN(a, "Parameter a cannot be Not a Number!");
        notNaN(b, "Parameter b cannot be Not a Number!");
        notNaN(c, "Parameter c cannot be Not a Number!");

        List<Double> results = new ArrayList<>(2);
        double discriminantSquared = Math.pow(b, 2) - (4 * a * c);
        if (discriminantSquared > 0) {
            double rootA = (-b + Math.sqrt(discriminantSquared)) / (2 * a);
            double rootB = (-b - Math.sqrt(discriminantSquared)) / (2 * a);
            if (!Double.isNaN(rootA) && !Double.isNaN(rootB)) {
                results.add(Math.min(rootA, rootB));
                results.add(Math.max(rootA, rootB));
            } else if (!Double.isNaN(rootA)) {
                results.add(rootA);
            } else {
                results.add(rootB);
            }
        } else if (discriminantSquared == 0) {
            double root = -b / (2 * a);
            if (!Double.isNaN(root)) {
                results.add(root);
            }
        }
        return results;
    }

    /**
     * Get a set of differentials for a given ray that share the same
     * starting point, but have slightly different directions centered around
     * the given ray
     *
     * @param ray the ray to perturb
     * @return a set of differential Rays, including the original ray
     */
    final public static Set<Ray> perturbRay(Ray ray, int differentialsPerRay, double lengthPercentage) {
        Shared.notNull(ray, "Parameter ray cannot be null!");
        Set<Ray> differentials = new HashSet<>();
        // add the original vector
        differentials.add(ray);
        // perturb the original vector to get differentials
        Point rayOrigin = ray.origin;
        Point rayEndPoint = rayOrigin.plus(ray.direction);
        //System.out.println("rayEndPoint is: " + rayEndPoint);
        double perturbRadius = ray.direction.magnitude() * (lengthPercentage / 100.0);
        //System.out.println("perturbRadius is: " + perturbRadius);
        for (int i = 0; i < differentialsPerRay; i++) {
            Point perturbedEndPoint = perturbPoint(rayEndPoint, perturbRadius);
            //System.out.println("perturbedEndPoint is: " + perturbedEndPoint);
            Vector differentialVector = new Vector(rayOrigin, perturbedEndPoint);
            differentials.add(new Ray(rayOrigin, differentialVector, ray.filmCoordinate));
        }
        return differentials;
    }

    final public static double randomSign() {
        return Math.random() > 0.5 ? 1.0 : -1.0;
    }

    final public static Point perturbPoint(Point point, double maxPerturbDistance) {
        double x = randomSign() * Math.random() * maxPerturbDistance + point.x;
        double y = randomSign() * Math.random() * maxPerturbDistance + point.y;
        double z = randomSign() * Math.random() * maxPerturbDistance + point.z;
        return new Point(x, y, z);
    }

    // constants
    final public class GraphicsFileFormat {
        public static final String ASCII_BW_PBM_MAGIC_NUMBER = "P1";
        public static final String ASCII_GRAYSCALE_PGM_MAGIC_NUMBER = "P2";
        public static final String ASCII_COLOR_PPM_MAGIC_NUMBER = "P3";
        // Reference:  https://en.wikipedia.org/wiki/Netpbm_format
    }
}
