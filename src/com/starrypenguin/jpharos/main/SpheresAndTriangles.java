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

package com.starrypenguin.jpharos.main;

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
import com.starrypenguin.jpharos.materials.Material;
import com.starrypenguin.jpharos.shapes.Sphere;
import com.starrypenguin.jpharos.shapes.Triangle;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Spheres And Triangles
 * <p/>
 * Main class for Spheres and Triangles assignemnt
 */
final public class SpheresAndTriangles {

    // Per the assignment instructions, hard-code the Scene
    public static Scene scene;
    public static Camera camera;

static {
        // Bodies
        Set<Body> bodies = new HashSet<>();
        // Sphere
        Point sphereLocation = new Point(0, 0, 70);
        Sphere sphere = new Sphere(sphereLocation, 40);
        Material redStuff = new Material(Color.RED);
        Body sphereBody = new Body(sphere, redStuff);
        bodies.add(sphereBody);

        //Triangles to make a plane
        Point quadrant1 = new Point(150, 100, 0);
        Point quadrant2 = new Point(-150, 100, 0);
        Point quadrant3 = new Point(-150, -100, 0);
        Point quadrant4 = new Point(150, -100, 0);
        Triangle triangle1 = new Triangle(quadrant4, quadrant1, quadrant3);
        Triangle triangle2 = new Triangle(quadrant1, quadrant2, quadrant3);
        Material whiteStuff = new Material(Color.WHITE);
        Body triangle1Body = new Body(triangle1, whiteStuff);
        Body triangle2Body = new Body(triangle2, whiteStuff);
        bodies.add(triangle1Body);
        bodies.add(triangle2Body);

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
        camera = new Camera(film, lens, cameraLocation, lookAt, up);

        // Put it all in the scene
        scene = new Scene(camera, lights, bodies);
    }
/*
    public static void main(String[] args) {
        String outFilename = "sphere.ppm";
        if (args.length > 0 && !args[0].isEmpty()) {
            outFilename = args[0];
        }
        camera.generateRays(scene, outFilename);
    }
*/
}
