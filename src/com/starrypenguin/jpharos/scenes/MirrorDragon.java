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
import com.starrypenguin.jpharos.materials.MirrorMaterial;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.shapes.TriangleMeshBuilder;
import com.starrypenguin.jpharos.util.TriangleMeshReader;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * MirrorDragon
 * <p/>
 * Second scene for Assignment 3:
 * <p>
 * Add reflective and refractive materials to your ray tracer.
 * <p>
 * Generate an image showing off your new capabilities.
 * <p>
 * You do not need to have caustics working correctly.
 */
public class MirrorDragon implements SceneBuilder {

    private static final double PLANE_LOCATION = 0.3;

    public Scene build() {
        // Bodies
        Set<Body> bodies = new HashSet<>();
        // read in shape from PLY file
        TriangleMesh triangleMesh = TriangleMeshReader.fromPlyFile("ply-input-files/dragon_quick.ply");
        MirrorMaterial mirrorMaterial = new MirrorMaterial();
        Body meshBody = new Body(triangleMesh, mirrorMaterial);
        bodies.add(meshBody);
        // Make a plane below the dragon
        Point point1 = new Point(PLANE_LOCATION, PLANE_LOCATION, -PLANE_LOCATION);
        Point point2 = new Point(-PLANE_LOCATION, PLANE_LOCATION, -PLANE_LOCATION);
        Point point3 = new Point(-PLANE_LOCATION, -PLANE_LOCATION, -PLANE_LOCATION);
        Point point4 = new Point(PLANE_LOCATION, -PLANE_LOCATION, -PLANE_LOCATION);
        TriangleMeshBuilder belowBuilder = new TriangleMeshBuilder();
        belowBuilder.addTriangle(point1, point2, point3);
        belowBuilder.addTriangle(point1, point3, point4);
        TriangleMesh belowTriangleMesh = belowBuilder.build();
        ColorMaterial white = new ColorMaterial(Color.BLUE);
        Body belowPlane = new Body(belowTriangleMesh, white, "Blue Lower Plane");
        bodies.add(belowPlane);
        //System.out.println("Added below plane");
        // Make a plane behind the sphere
        Point point9 = new Point(PLANE_LOCATION, PLANE_LOCATION, PLANE_LOCATION);
        Point pointA = new Point(-PLANE_LOCATION, PLANE_LOCATION, PLANE_LOCATION);
        Point pointB = new Point(-PLANE_LOCATION, PLANE_LOCATION, -PLANE_LOCATION);
        Point pointC = new Point(PLANE_LOCATION, PLANE_LOCATION, -PLANE_LOCATION);
        TriangleMeshBuilder behindBuilder = new TriangleMeshBuilder();
        behindBuilder.addTriangle(point9, pointA, pointB);
        behindBuilder.addTriangle(point9, pointB, pointC);
        TriangleMesh behindTriangleMesh = behindBuilder.build();
        ColorMaterial orange = new ColorMaterial(Color.YELLOW);
        Body behindPlane = new Body(behindTriangleMesh, orange, "Yellow Back Plane");
        bodies.add(behindPlane);
        //System.out.println("Added behind plane");
        // Make a plane to the left of the sphere
        Point pointD = new Point(-PLANE_LOCATION, PLANE_LOCATION, PLANE_LOCATION);
        Point pointE = new Point(-PLANE_LOCATION, PLANE_LOCATION, -PLANE_LOCATION);
        Point pointF = new Point(-PLANE_LOCATION, -PLANE_LOCATION, -PLANE_LOCATION);
        Point pointG = new Point(-PLANE_LOCATION, -PLANE_LOCATION, PLANE_LOCATION);
        TriangleMeshBuilder leftBuilder = new TriangleMeshBuilder();
        leftBuilder.addTriangle(pointD, pointE, pointF);
        leftBuilder.addTriangle(pointD, pointF, pointG);
        TriangleMesh leftTriangleMesh = leftBuilder.build();
        ColorMaterial cyan = new ColorMaterial(Color.RED);
        Body leftPlane = new Body(leftTriangleMesh, cyan, "Red Left Plane");
        bodies.add(leftPlane);
        //System.out.println("Added left plane");
        // Make a plane to the right of the sphere
        Point pointH = new Point(PLANE_LOCATION, PLANE_LOCATION, PLANE_LOCATION);
        Point pointI = new Point(PLANE_LOCATION, PLANE_LOCATION, -PLANE_LOCATION);
        Point pointJ = new Point(PLANE_LOCATION, -PLANE_LOCATION, -PLANE_LOCATION);
        Point pointK = new Point(PLANE_LOCATION, -PLANE_LOCATION, PLANE_LOCATION);
        TriangleMeshBuilder rightBuilder = new TriangleMeshBuilder();
        rightBuilder.addTriangle(pointH, pointI, pointJ);
        rightBuilder.addTriangle(pointH, pointJ, pointK);
        TriangleMesh rightTriangleMesh = rightBuilder.build();
        ColorMaterial magenta = new ColorMaterial(Color.GREEN);
        Body rightPlane = new Body(rightTriangleMesh, magenta, "Green Right Plane");
        bodies.add(rightPlane);
        //System.out.println("Added right plane");
        // Lights
        Light pointLight = new PointLight(new Point(0, 0.2, 0));
        Set<Light> lights = new HashSet<>();
        lights.add(pointLight);

        // Camera
        Point cameraLocation = new Point(0, 0.12, 0.15);
        Point target = new Point(-0.005, 0.12, 0);
        Vector up = new Vector(0, 1, 0);
        Vector lookAt = new Vector(cameraLocation, target);
        Lens lens = new PinholeLens(200);
        Film film = new Film(1, 600, 600, 1);
        Camera camera = new Camera(film, lens, cameraLocation, lookAt, up);
        // Put it all in the scene
        return new Scene(camera, lights, bodies);
    }
}
