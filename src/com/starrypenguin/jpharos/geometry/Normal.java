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

import com.starrypenguin.jpharos.util.Shared;

/**
 * Normal
 * <p/>
 * Represents a surface normal vector
 */
final public class Normal {
    // value class; immutable and cannot be changed after being created
    public final double x;
    public final double y;
    public final double z;

    public Normal(double x, double y, double z) {
        Shared.notNaN3D(x, y, z, "All parameters x, y, and z cannot be Not A Number!");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Normal (Point from, Point to) {
        this(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    public static Normal fromVector(Vector vector) {
        Shared.notNull(vector, "Parameter vector cannot be null!");
        return new Normal(vector.x, vector.y, vector.z);
    }

    public Normal plus(Normal normal) {
        Shared.notNull(normal, "Parameter normal cannot be null!");
        return new Normal(this.x + normal.x, this.y + normal.y, this.z + normal.z);
    }

    public Vector plus(Vector vector) {
        Shared.notNull(vector, "Parameter vector cannot be null!");
        return new Vector(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    public Normal minus(Normal normal) {
        Shared.notNull(normal, "Parameter normal cannot be null!");
        return new Normal(this.x - normal.x, this.y - normal.y, this.z - normal.z);
    }

    public Vector minus(Vector vector) {
        Shared.notNull(vector, "Parameter vector cannot be null!");
        return new Vector(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    public double magnitude() {
        return Math.sqrt( (this.x * this.x) + (this.y * this.y) + (this.z * this.z)  );
    }

    public Normal scale(double scalar) {
        Shared.notNaN(scalar, "Parameter scalar cannot be Not A Number!");
        return new Normal(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Normal normalized() {
        double mag = this.magnitude();
        return new Normal(this.x / mag, this.y / mag, this.z/mag);
    }

    public double dot(Vector vector) {
        Shared.notNull(vector, "Parameter vector cannot be null!");
        return (this.x * vector.x) + (this.y * vector.y) + (this.z * vector.z);
    }

    public boolean isOrthogonalTo(Vector vector) {
        return (this.dot(vector) == 0);
    }

    public Vector cross(Vector vector) {
        Shared.notNull(vector, "Parameter vector cannot be null!");
        return new Vector( (this.y * vector.z - this.z * vector.y),
                (this.z * vector.x - this.x * vector.z),
                (this.x * vector.y - this.y * vector.x) );
    }

    public Normal inverse() {
        return new Normal(-this.x, -this.y, -this.z);
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "Normal{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
