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

package com.starrypenguin.jpharos.shapes;

import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.core.RayImpact;
import com.starrypenguin.jpharos.geometry.BoundingBox;
import com.starrypenguin.jpharos.geometry.Normal;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.util.Shared;

/**
 * Plane
 * <p/>
 * A flat, geometric plane
 */
public class Plane extends Shape {

    public final Normal normal;

    public Plane(Point location, Normal normal) {
        super(location);
        Shared.notNull(normal, "normal cannot be null!");
        this.normal = normal;
    }

    @Override
    public boolean ImpactsP(Ray ray) {
        return false;
    }

    @Override
    public RayImpact Impacts(Ray ray) {
        return null;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return null;
    }

    @Override
    public double surfaceArea() {
        return 0;
    }
}
