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
import com.starrypenguin.jpharos.materials.GlassMaterial;
import com.starrypenguin.jpharos.materials.MirrorMaterial;
import com.starrypenguin.jpharos.shapes.Sphere;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.shapes.TriangleMeshBuilder;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * MirrorAndGlassSpheres
 * <p/>
 * Scene for Assignment 3:
 * <p>
 * Add reflective and refractive materials to your ray tracer.
 * <p>
 * Generate an image showing off your new capabilities.
 * <p>
 * You do not need to have caustics working correctly.
 */
public class MirrorAndGlassSpheres implements SceneBuilder {

    public Scene build() {
        // Bodies
        Set<Body> bodies = new HashSet<>();
        // Sphere 1
        Point sphere1Location = new Point(-25, 0, 0);
        Sphere sphere1 = new Sphere(sphere1Location, 20);
        MirrorMaterial mirrorMaterial = new MirrorMaterial();
        //ColorMaterial mirrorMaterial = new ColorMaterial(Color.BLUE);
        Body sphere1Body = new Body(sphere1, mirrorMaterial, "Mirrored Sphere");
        bodies.add(sphere1Body);
        // Sphere 2
        Point sphere2Location = new Point(25, 0, 0);
        Sphere sphere2 = new Sphere(sphere2Location, 20);
        GlassMaterial glassMaterial = new GlassMaterial(GlassMaterial.RefractionIndices.GLASS);
        //ColorMaterial glassMaterial = new ColorMaterial(Color.RED);
        Body sphere2Body = new Body(sphere2, glassMaterial, "Glass Sphere");
        bodies.add(sphere2Body);
        //System.out.println("Added sphere");
        // Make a plane below the sphere
        Point point1 = new Point(55, 55, -55);
        Point point2 = new Point(-55, 55, -55);
        Point point3 = new Point(-55, -55, -55);
        Point point4 = new Point(55, -55, -55);
        TriangleMeshBuilder belowBuilder = new TriangleMeshBuilder();
        belowBuilder.addTriangle(point1, point2, point3);
        belowBuilder.addTriangle(point1, point3, point4);
        TriangleMesh belowTriangleMesh = belowBuilder.build();
        ColorMaterial white = new ColorMaterial(Color.WHITE);
        Body belowPlane = new Body(belowTriangleMesh, white, "White Lower Plane");
        bodies.add(belowPlane);
        //System.out.println("Added below plane");
        // Make a plane behind the sphere
        Point point9 = new Point(55, 55, 55);
        Point pointA = new Point(-55, 55, 55);
        Point pointB = new Point(-55, 55, -55);
        Point pointC = new Point(55, 55, -55);
        TriangleMeshBuilder behindBuilder = new TriangleMeshBuilder();
        behindBuilder.addTriangle(point9, pointA, pointB);
        behindBuilder.addTriangle(point9, pointB, pointC);
        TriangleMesh behindTriangleMesh = behindBuilder.build();
        ColorMaterial orange = new ColorMaterial(Color.ORANGE);
        Body behindPlane = new Body(behindTriangleMesh, orange, "Orange Back Plane");
        bodies.add(behindPlane);
        //System.out.println("Added behind plane");
        // Make a plane to the left of the sphere
        Point pointD = new Point(-55, 55, 55);
        Point pointE = new Point(-55, 55, -55);
        Point pointF = new Point(-55, -55, -55);
        Point pointG = new Point(-55, -55, 55);
        TriangleMeshBuilder leftBuilder = new TriangleMeshBuilder();
        leftBuilder.addTriangle(pointD, pointE, pointF);
        leftBuilder.addTriangle(pointD, pointF, pointG);
        TriangleMesh leftTriangleMesh = leftBuilder.build();
        ColorMaterial cyan = new ColorMaterial(Color.CYAN);
        Body leftPlane = new Body(leftTriangleMesh, cyan, "Cyan Left Plane");
        bodies.add(leftPlane);
        //System.out.println("Added left plane");
        // Make a plane to the right of the sphere
        Point pointH = new Point(55, 55, 55);
        Point pointI = new Point(55, 55, -55);
        Point pointJ = new Point(55, -55, -55);
        Point pointK = new Point(55, -55, 55);
        TriangleMeshBuilder rightBuilder = new TriangleMeshBuilder();
        rightBuilder.addTriangle(pointH, pointI, pointJ);
        rightBuilder.addTriangle(pointH, pointJ, pointK);
        TriangleMesh rightTriangleMesh = rightBuilder.build();
        ColorMaterial magenta = new ColorMaterial(Color.MAGENTA);
        Body rightPlane = new Body(rightTriangleMesh, magenta, "Magenta Right Plane");
        bodies.add(rightPlane);
        //System.out.println("Added right plane");
        // Lights
        Set<Light> lights = new HashSet<>();
        Light pointLight1 = new PointLight(new Point(0, 0, 120));
        lights.add(pointLight1);
        //System.out.println("Added lights");
        // Camera
        Point cameraLocation = new Point(0, -70, 0);
        Vector up = new Vector(0, 0, 1);
        Vector lookAt = new Vector(0, 1, 0);
        Lens lens = new PinholeLens(20);
        Film film = new Film(0.1, 600, 600, 1);
        Camera camera = new Camera(film, lens, cameraLocation, lookAt, up);
        //System.out.println("Added camera");
        // Put it all in the scene
        return new Scene(camera, lights, bodies);
    }
}
