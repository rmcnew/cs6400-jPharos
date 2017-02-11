package com.starrypenguin.jpharos.geometry;

/**
 * Vector
 * <p/>
 * description
 * <p/>
 * Author: Richard Scott McNew
 */
public class Vector {
    // value class; immutable and cannot be changed after being created
    final public double x, y, z;

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

    public double magnitude() {
        return Math.sqrt( (this.x * this.x) + (this.y * this.y) + (this.z * this.z)  );
    }

    public Vector scale(double scalar) {
        if (Double.isNaN(scalar)) {
            throw new IllegalArgumentException("scalar cannot be Not a Number!");
        }
        return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vector inverse() {
        return new Vector(-this.x, -this.y, -this.z);
    }
}
