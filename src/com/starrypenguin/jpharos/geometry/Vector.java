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

/**
 * Vector
 * <p/>
 * Represents a mathematical vector entity that has a direction and magnitude
 */
final public class Vector {
    // value class; immutable and cannot be changed after being created
    final public double x;
    final public double y;
    final public double z;

    public Vector(double x, double y, double z) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            throw new IllegalArgumentException("Vector coordinates cannot be Not a Number!");
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector (Point from, Point to) {
        this(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    public Vector plus(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("vector cannot be null!");
        }
        return new Vector(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    public Vector plus(Normal normal) {
        if (normal == null) {
            throw new IllegalArgumentException("normal cannot be null!");
        }
        return new Vector(this.x + normal.x, this.y + normal.y, this.z + normal.z);
    }


    public Vector minus(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("vector cannot be null!");
        }
        return new Vector(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    public double magnitude() {
        return Math.sqrt( (this.x * this.x) + (this.y * this.y) + (this.z * this.z)  );
    }

    public Vector scale(double scalar) {
        if (Double.isNaN(scalar)) {
            throw new IllegalArgumentException("scalar cannot be Not a Number!");
        }
        return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vector normalized() {
        double mag = this.magnitude();
        return new Vector(this.x / mag, this.y / mag, this.z/mag);
    }

    public double dot(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("vector cannot be null!");
        }
        return (this.x * vector.x) + (this.y * vector.y) + (this.z * vector.z);
    }

    public double dot(Normal normal) {
        if (normal == null) {
            throw new IllegalArgumentException("normal cannot be null!");
        }
        return (this.x * normal.x) + (this.y * normal.y) + (this.z * normal.z);
    }

    public boolean isOrthogonalTo(Vector vector) {
        return (this.dot(vector) == 0);
    }

    public Vector cross(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("vector cannot be null!");
        }
        return new Vector( (this.y * vector.z - this.z * vector.y),
                           (this.z * vector.x - this.x * vector.z),
                           (this.x * vector.y - this.y * vector.x) );
    }

    public Vector inverse() {
        return new Vector(-this.x, -this.y, -this.z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (Double.compare(vector.x, x) != 0) return false;
        if (Double.compare(vector.y, y) != 0) return false;
        return Double.compare(vector.z, z) == 0;
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
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
    
}
