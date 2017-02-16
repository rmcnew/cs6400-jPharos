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

import java.util.Collection;

/**
 * Shared
 * <p/>
 * Shared constants and general utility functions
 */
public class Shared {

    public static boolean nullOrEmpty(String str) {
        return (str == null) || str.isEmpty();
    }

    public static boolean notNullAndNotEmpty(String str) {
        return (str != null) && (!str.isEmpty());
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
        if (Double.isNaN(value) || value <= 0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // ensure value is not NaN;  ensure  min <= value <= max
    public static void notNaNAndInclusiveRangeCheck(double value, double min, double max, String errorMessage) {
        if (Double.isNaN(value) || (value < min) || (value > max) ) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // ensure value is not NaN;  ensure  min < value < max
    public static void notNaNAndExclusiveRangeCheck(double value, double min, double max, String errorMessage) {
        if (Double.isNaN(value) || (value <= min) || (value >= max) ) {
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
}
