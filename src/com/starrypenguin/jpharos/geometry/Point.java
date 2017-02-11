package com.starrypenguin.jpharos.geometry;

/**
 * Point
 * <p/>
 * Points are value classes that represent a location in 3D space
 * <p/>
 * Author: Richard Scott McNew
 */
public class Point {
    // value class; immutable and cannot be changed after being created
    final public double x, y, z;

    public Point(double x, double y, double z) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            throw new IllegalArgumentException("Point coordinates cannot be Not a Number!");
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point plus(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("point cannot be null!");
        }
        return new Point(this.x + point.x, this.y + point.y, this.z + point.z);
    }

    public Point plus(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("vector cannot be null!");
        }
        return new Point(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    public Point minus(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("vector cannot be null!");
        }
        return new Point(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    public Point times(double scalar) {
        if (Double.isNaN(scalar)) {
            throw new IllegalArgumentException("scalar cannot be Not a Number!");
        }
        return new Point(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public static double distanceSquared(Point pA, Point pB) {
        if (pA == null) {
            throw new IllegalArgumentException("Point pA cannot be null!");
        } else if (pB == null) {
            throw new IllegalArgumentException("Point pB cannot be null!");
        }
        return  (Math.pow((pB.x - pA.x), 2.0) + Math.pow((pB.y - pA.y), 2.0) + Math.pow((pB.z - pA.z), 2.0) );
    }

    public static double distance(Point pA, Point pB) {
        if (pA == null) {
            throw new IllegalArgumentException("Point pA cannot be null!");
        } else if (pB == null) {
            throw new IllegalArgumentException("Point pB cannot be null!");
        }
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
        if (pA == null) {
            throw new IllegalArgumentException("Point pA cannot be null!");
        } else if (pB == null) {
            throw new IllegalArgumentException("Point pB cannot be null!");
        }
        return new Point(Math.max(pA.x, pB.x), Math.max(pA.y, pB.y), Math.max(pA.z, pB.z) );
    }

    public static Point min(Point pA, Point pB) {
        if (pA == null) {
            throw new IllegalArgumentException("Point pA cannot be null!");
        } else if (pB == null) {
            throw new IllegalArgumentException("Point pB cannot be null!");
        }
        return new Point(Math.min(pA.x, pB.x), Math.min(pA.y, pB.y), Math.min(pA.z, pB.z) );
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

    private double index(int i) {
        if ( (i < 0) || ( i > 2) ) {
            throw new IllegalArgumentException("index must be 0, 1, or 2");
        }
        if (i == 0) {
            return this.x;
        } else if (i == 1) {
            return this.y;
        } else {
            return this.z;
        }
    }

    public static Point permute(Point point, int x, int y, int z) {
        if (point == null) {
            throw new IllegalArgumentException("point cannot be null!");
        }
        return new Point(point.index(x), point.index(y), point.index(z) );
    }

// *** Implement  .equals, .hashCode, and .toString methods ***
// *** Add Jackson JSON binding annotations ***
// *** Add .toJSON that uses JSON binding to generate serialized JSON form ***
// *** Add static Point.fromJSON method to create Point from serialized JSON form ***
// *** Add unit tests for JSON serialization / deserialization ***
}
