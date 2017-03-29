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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Shared
 * <p/>
 * Shared constants and general utility functions
 */
final public class Shared {

    public static void notNullAndNotEmpty(String str, String errorMessage) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    // methods

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
        // if a is zero and b is not zero, then just solve for bx + c = 0  ==>  x = -c / b
        if ((Double.compare(a, 0.0) == 0) && (Double.compare(b, 0.0) != 0)) {
            double result = -c / b;
            if (!Double.isNaN(result)) {
                results.add(result);
            }
            // if a is not zero and b is zero, then just solve for ax^2 + c = 0  ==>  x = +/- sqrt(-c / a) iff -c / a  is positive
        } else if ((Double.compare(a, 0.0) != 0) && (Double.compare(b, 0.0) == 0)) {
            double minusCdivA = -c / a;
            if (Double.compare(minusCdivA, 0.0) < 0) {
                // -c / a is less than zero, so no real roots; return empty results list
            } else {
                double sqrtMinusCdivA = Math.sqrt(minusCdivA);
                double negSqrtMinusCdivA = -sqrtMinusCdivA;
                double min = Math.min(sqrtMinusCdivA, negSqrtMinusCdivA);
                double max = Math.max(sqrtMinusCdivA, negSqrtMinusCdivA);
                if (!Double.isNaN(min)) {
                    results.add(min);
                }
                if (!Double.isNaN(max)) {
                    results.add(max);
                }
            }
            // a and b are both not zero; use the full quadratic formula:  x = (-b +/- sqrt(b^2 - 4ac)) / 2a
        } else {
            // first check sign of discriminant:
            //    a negative discriminant means no real roots;
            //    a discriminant with value 0 means one real root;
            //    a positive discriminant means two real roots
            double discriminant = (b * b) - (4.0 * a * c);
            if (Double.compare(discriminant, 0.0) < 0) {
                // the discriminant is negative; return empty results list
            } else if (Double.compare(discriminant, 0.0) == 0) {
                // there is only one real root
                double root = -b / (2.0 * a);
                results.add(root);
            } else {
                double root1 = (-b + Math.sqrt(discriminant)) / (2.0 * a);
                double root2 = (-b - Math.sqrt(discriminant)) / (2.0 * a);
                double min = Math.min(root1, root2);
                double max = Math.max(root1, root2);
                if (!Double.isNaN(min)) {
                    results.add(min);
                }
                if (!Double.isNaN(max)) {
                    results.add(max);
                }
            }
        }
        return results;
    }

    // constants
    final public class GraphicsFileFormat {
        public static final String ASCII_BW_PBM_MAGIC_NUMBER = "P1";
        public static final String ASCII_GRAYSCALE_PGM_MAGIC_NUMBER = "P2";
        public static final String ASCII_COLOR_PPM_MAGIC_NUMBER = "P3";
        // Reference:  https://en.wikipedia.org/wiki/Netpbm_format
    }
}
