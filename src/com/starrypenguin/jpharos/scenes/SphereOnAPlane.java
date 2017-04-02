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

package com.starrypenguin.jpharos.scenes;

import com.starrypenguin.jpharos.cameras.Camera;
import com.starrypenguin.jpharos.cameras.Film;
import com.starrypenguin.jpharos.core.Body;
import com.starrypenguin.jpharos.core.Scene;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.lenses.Lens;
import com.starrypenguin.jpharos.lenses.PinholeLens;
import com.starrypenguin.jpharos.lights.Light;
import com.starrypenguin.jpharos.lights.PointLight;
import com.starrypenguin.jpharos.materials.ColorMaterial;
import com.starrypenguin.jpharos.shapes.Sphere;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.shapes.TriangleMeshBuilder;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * SphereOnAPlane
 * <p/>
 * Scene for Assignment 1:
 * Create a simple ray tracer.  This will be the basis for future assignments.
 * <p>
 * You need only support spheres and triangles as shapes.
 * <p>
 * One ray per pixel - This will be relaxed in later assignments
 * <p>
 * No acceleration structures yet - test each shape for intersections
 * <p>
 * No scene file - you will be able to hard code a scene every assignment.
 * <p>
 * Only point light sources - this will be relaxed in later assignments
 * <p>
 * Only use Lambertian shading - this will change in future assignments
 * <p>
 * Turn in an image that shows a sphere above and casting a shadow on a plane
 * (made of two or more triangles) that was produced by your ray tracer.
 */
public class SphereOnAPlane implements SceneBuilder {

    public Scene build() {
        // Bodies
        Set<Body> bodies = new HashSet<>();
        // Sphere
        Point sphereLocation = new Point(0, 0, 1);
        Sphere sphere = new Sphere(sphereLocation, 40);
        ColorMaterial redStuff = new ColorMaterial(Color.RED);
        Body sphereBody = new Body(sphere, redStuff);
        bodies.add(sphereBody);

        //Triangles to make a plane
        Point quadrant1 = new Point(150, 100, 0);
        Point quadrant2 = new Point(-150, 100, 0);
        Point quadrant3 = new Point(-150, -100, 0);
        Point quadrant4 = new Point(150, -100, 0);
        TriangleMeshBuilder triangleMeshBuilder = new TriangleMeshBuilder();
        triangleMeshBuilder.addTriangle(quadrant4, quadrant1, quadrant3);
        triangleMeshBuilder.addTriangle(quadrant1, quadrant2, quadrant3);
        ColorMaterial whiteStuff = new ColorMaterial(Color.WHITE);
        TriangleMesh triangleMesh = triangleMeshBuilder.build();
        Body plane = new Body(triangleMesh, whiteStuff);
        bodies.add(plane);

        // Lights
        Light pointLight = new PointLight(new Point(-20, 0, 120));
        Set<Light> lights = new HashSet<>();
        lights.add(pointLight);

        // Camera
        Point cameraLocation = new Point(60, 0, 25);
        Vector up = new Vector(0, 0, 1);
        Vector lookAt = new Vector(-1, 0, 0);
        Lens lens = new PinholeLens(60);
        Film film = new Film(1, 300, 300, 1);
        Camera camera = new Camera(film, lens, cameraLocation, lookAt, up);

        // Put it all in the scene
        return new Scene(camera, lights, bodies);
    }
}
