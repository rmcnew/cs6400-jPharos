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
 * BoundingBox
 * <p/>
 * Axis-aligned bounding box used for accelerating ray impact detection
 */
public class BoundingBox {

    public final Point max;  // the point with the highest x, y, and z values calculated from the supplied corner Points
    public final Point min;  // the point with the lowest x, y, and z values calculated from the supplied corner Points

    public BoundingBox() {
        this.min = new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        this.max = new Point(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    public BoundingBox(Point pA, Point pB) {
        Shared.notNull(pA, "pA cannot be null!");
        Shared.notNull(pB, "pB cannot be null!");
        // calculate the max point and min point using the supplied corner Points
        max = new Point(Math.max(pA.x, pB.x), Math.max(pA.y, pB.y), Math.max(pA.z, pB.z));
        min = new Point(Math.min(pA.x, pB.x), Math.min(pA.y, pB.y), Math.min(pA.z, pB.z));
    }

    public BoundingBox union(BoundingBox boundingBox) {
        Point max = new Point(Math.max(this.max.x, boundingBox.max.x), Math.max(this.max.y, boundingBox.max.y), Math.max(this.max.z, boundingBox.max.z));
        Point min = new Point(Math.min(this.min.x, boundingBox.min.x), Math.min(this.min.y, boundingBox.min.y), Math.min(this.min.z, boundingBox.min.z));
        return new BoundingBox(max, min);
    }

    public Point getCenterPoint() {
        return new Point((this.min.x + this.max.x)/2.0, (this.min.y + this.max.y)/2.0, (this.min.z + this.max.z)/2.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoundingBox that = (BoundingBox) o;

        if (!max.equals(that.max)) return false;
        return min.equals(that.min);
    }

    @Override
    public int hashCode() {
        int result = max.hashCode();
        result = 31 * result + min.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "max=" + max +
                ", min=" + min +
                '}';
    }
}
