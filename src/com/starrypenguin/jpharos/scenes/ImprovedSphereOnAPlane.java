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
import com.starrypenguin.jpharos.lights.AreaLight;
import com.starrypenguin.jpharos.lights.Light;
import com.starrypenguin.jpharos.materials.ColorMaterial;
import com.starrypenguin.jpharos.shapes.Sphere;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.shapes.TriangleMeshBuilder;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * ImprovedSphereOnAPlane
 * <p/>
 * Scene for Distributed Ray Tracing assignment
 */
public class ImprovedSphereOnAPlane implements SceneBuilder {

    public Scene build() {
        // Bodies
        Set<Body> bodies = new HashSet<>();
        // Sphere
        Point sphereLocation = new Point(0, 0, 1);
        Sphere sphere = new Sphere(sphereLocation, 30);
        ColorMaterial redStuff = new ColorMaterial(Color.RED);
        Body sphereBody = new Body(sphere, redStuff);
        bodies.add(sphereBody);

        //Triangles to make a plane
        Point point1 = new Point(205, 205, -205);
        Point point2 = new Point(-205, 205, -205);
        Point point3 = new Point(-205, -205, -205);
        Point point4 = new Point(205, -205, -205);
        TriangleMeshBuilder belowBuilder = new TriangleMeshBuilder();
        belowBuilder.addTriangle(point1, point2, point3);
        belowBuilder.addTriangle(point1, point3, point4);
        TriangleMesh belowTriangleMesh = belowBuilder.build();
        ColorMaterial white = new ColorMaterial(Color.WHITE);
        Body belowPlane = new Body(belowTriangleMesh, white, "White Lower Plane");
        bodies.add(belowPlane);

        // Lights
        Point lightCenter = new Point(0, 0, 100);
        double lightRadius = 10;
        Sphere lightSphere = new Sphere(lightCenter, lightRadius);
        AreaLight areaLight = new AreaLight(lightSphere);
        Set<Light> lights = new HashSet<>();
        lights.add(areaLight);
        ColorMaterial lightMaterial = new ColorMaterial(Color.YELLOW);
        Body areaLightBody = new Body(lightSphere, lightMaterial, "Light", true);
        bodies.add(areaLightBody);

        // Camera
        Point cameraLocation = new Point(0, -100, 10);
        Vector up = new Vector(0, 0, 1);
        Vector lookAt = new Vector(0, 1, 0);
        Lens lens = new PinholeLens(20);
        Film film = new Film(0.2, 300, 300, 1);
        Camera camera = new Camera(film, lens, cameraLocation, lookAt, up);

        // Put it all in the scene
        return new Scene(camera, lights, bodies);
    }
}
