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

package com.starrypenguin.jpharos.geometry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starrypenguin.jpharos.util.Shared;

import java.io.IOException;

/**
 * Point
 * <p/>
 * Points are value classes that represent a location in 3D space
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="class")
final public class Point {
    final private static ObjectMapper objectMapper = new ObjectMapper();
    // value class; immutable and cannot be changed after being created
    final public double x;
    final public double y;
    final public double z;

    @JsonCreator
    public Point(@JsonProperty("x") double x, @JsonProperty("y") double y, @JsonProperty("z") double z) {
        Shared.notNaN3D(x, y, z, "Point coordinates must be valid numbers");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static double distanceSquared(Point pA, Point pB) {
        Shared.notNull(pA, "Point pA cannot be null!");
        Shared.notNull(pB, "Point pB cannot be null!");
        return  (Math.pow((pB.x - pA.x), 2.0) + Math.pow((pB.y - pA.y), 2.0) + Math.pow((pB.z - pA.z), 2.0) );
    }

    public static double distance(Point pA, Point pB) {
        Shared.notNull(pA, "Point pA cannot be null!");
        Shared.notNull(pB, "Point pB cannot be null!");
        return Math.sqrt( Point.distanceSquared(pA, pB) );
    }

    //  0.0 <= time <= 1.0
    public static Point linearInterpolate(double time, Point pA, Point pB) {
        if (Double.isNaN(time) || (time < 0.0) || (time > 1.0) ) {
            throw new IllegalArgumentException("time must be in the range [0.0, 1.0]");
        } else if (pA == null) {
            throw new IllegalArgumentException("Point pA cannot be null!");
        } else if (pB == null) {
            throw new IllegalArgumentException("Point pB cannot be null!");
        }
        return pA.times(1-time).plus(pB.times(time));
    }

    public static Point max(Point pA, Point pB) {
        Shared.notNull(pA, "Point pA cannot be null!");
        Shared.notNull(pB, "Point pB cannot be null!");
        return new Point(Math.max(pA.x, pB.x), Math.max(pA.y, pB.y), Math.max(pA.z, pB.z) );
    }

    public static Point min(Point pA, Point pB) {
        Shared.notNull(pA, "Point pA cannot be null!");
        Shared.notNull(pB, "Point pB cannot be null!");
        return new Point(Math.min(pA.x, pB.x), Math.min(pA.y, pB.y), Math.min(pA.z, pB.z) );
    }

    public static int coordHashCode(double x, double y, double z) {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public static String toJSON(Point point) {
        String retVal = "";
        try {
            retVal = objectMapper.writeValueAsString(point);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static Point fromJSON(String strPoint) {
        Point retVal = null;
        try {
            retVal = objectMapper.readValue(strPoint, Point.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public Point plus(Point point) {
        Shared.notNull(point, "point cannot be null!");
        return new Point(this.x + point.x, this.y + point.y, this.z + point.z);
    }

    public Point plus(Vector vector) {
        Shared.notNull(vector, "vector cannot be null!");
        return new Point(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    public Point minus(Vector vector) {
        Shared.notNull(vector, "vector cannot be null!");
        return new Point(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    public Point times(double scalar) {
        Shared.notNaN(scalar, "scalar cannot be Not a Number!");
        return new Point(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Point floor() {
        return new Point(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z) );
    }

    public Point ceiling() {
        return new Point(Math.ceil(this.x), Math.ceil(this.y), Math.ceil(this.z) );
    }

    public Point absoluteValue() {
        return new Point(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z) );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.x, x) != 0) return false;
        if (Double.compare(point.y, y) != 0) return false;
        return Double.compare(point.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
