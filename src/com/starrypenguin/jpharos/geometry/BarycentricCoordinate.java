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
 * BarycentricCoordinate
 * <p/>
 * A class for Barycentric coordinates
 */
final public class BarycentricCoordinate {

    private final static double EPSILON = 0.000001;
    final public double u;
    final public double v;
    final public double w;

    public BarycentricCoordinate(double u, double v, double w) {
        Shared.notNaN(u, "Parameter u cannot be Not A Number!");
        Shared.notNaN(v, "Parameter v cannot be Not A Number!");
        Shared.notNaN(w, "Parameter w cannot be Not A Number!");
        double sum = u + v + w;
        if (Math.abs(1.0 - sum) > EPSILON) {
            throw new IllegalArgumentException("Barycentric coordinates must sum to 1.0!");
        }
        this.u = u;
        this.v = v;
        this.w = w;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BarycentricCoordinate that = (BarycentricCoordinate) o;

        if (Double.compare(that.u, u) != 0) return false;
        if (Double.compare(that.v, v) != 0) return false;
        return Double.compare(that.w, w) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(u);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(v);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(w);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "BarycentricCoordinate{" +
                "u=" + u +
                ", v=" + v +
                ", w=" + w +
                '}';
    }
}
