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

package com.starrypenguin.jpharos.core;

import com.starrypenguin.jpharos.util.Shared;

import java.awt.*;

/**
 * RayImpact
 * <p/>
 * Represents the data from when a Ray hits a Body
 */
public class RayImpact {

    // at what time along the Ray did the Impact occur?
    public final double impactTime;
    public final Ray ray;
    public final Color color;

    public RayImpact(Ray ray,  double impactTime, Color color) {
        Shared.notNull(ray, "ray cannot be null!");
        Shared.notNaN(impactTime, "impactTime cannot be Not A Number!");
        Shared.notNull(color, "color cannot be null!");
        this.ray = ray;
        this.impactTime = impactTime;
        this.color = color;
    }
}
