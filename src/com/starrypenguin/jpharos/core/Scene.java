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

import com.starrypenguin.jpharos.cameras.Camera;
import com.starrypenguin.jpharos.lights.Light;
import com.starrypenguin.jpharos.util.Shared;

import java.util.Set;

/**
 * Scene
 * <p/>
 * The overall container of with objects needed to generateRays a scene
 */
final public class Scene {

    // Scene is a Singleton object
    final public Camera camera;
    final public Set<Light> lights;
    final public Set<Body> bodies;

    public Scene(Camera camera, Set<Light> lights,  Set<Body> bodies) {
        Shared.notNull(camera, "camera cannot be null!");
        Shared.notNullAndNotEmpty(lights, "lights cannot be null or empty!");
        Shared.notNullAndNotEmpty(bodies, "bodies cannot be null or empty!");
        this.camera = camera;
        this.lights = lights;
        this.bodies = bodies;
    }
}
