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
import com.starrypenguin.jpharos.lenses.ThinLens;
import com.starrypenguin.jpharos.lights.AreaLight;
import com.starrypenguin.jpharos.lights.Light;
import com.starrypenguin.jpharos.materials.ChromaticMaterial;
import com.starrypenguin.jpharos.materials.ColorMaterial;
import com.starrypenguin.jpharos.materials.MirrorMaterial;
import com.starrypenguin.jpharos.materials.RefractiveMaterial;
import com.starrypenguin.jpharos.shapes.Sphere;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.shapes.TriangleMeshBuilder;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * BeautifulSpheres
 * <p/>
 * Scene for final assignment
 */
public class BeautifulSpheres implements SceneBuilder {

    public Scene build() {
        // Bodies
        Set<Body> bodies = new HashSet<>();
        // Sphere 1
        Point sphere1Location = new Point(-25, -25, 0);
        Sphere sphere1 = new Sphere(sphere1Location, 15);
        MirrorMaterial mirrorMaterial = new MirrorMaterial();
        //ColorMaterial mirrorMaterial = new ColorMaterial(Color.BLUE);
        Body sphere1Body = new Body(sphere1, mirrorMaterial, "Mirrored Sphere");
        bodies.add(sphere1Body);

        // Sphere 2
        Point sphere2Location = new Point(0, 0, 0);
        Sphere sphere2 = new Sphere(sphere2Location, 15);
        RefractiveMaterial refractiveMaterial = new RefractiveMaterial(RefractiveMaterial.RefractionIndices.GLASS);
        //ColorMaterial refractiveMaterial = new ColorMaterial(Color.RED);
        Body sphere2Body = new Body(sphere2, refractiveMaterial, "Glass Sphere");
        bodies.add(sphere2Body);

        // Sphere 3
        Point sphere3Location = new Point(25, 25, 0);
        Sphere sphere3 = new Sphere(sphere3Location, 15);
        ChromaticMaterial chromaticMaterial = new ChromaticMaterial();
        Body sphere3Body = new Body(sphere3, chromaticMaterial, "Chromatic Sphere");
        bodies.add(sphere3Body);

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

        // Lights
        Point lightCenter = new Point(0, 0, 120);
        double lightRadius = 10;
        Sphere lightSphere = new Sphere(lightCenter, lightRadius);
        AreaLight areaLight = new AreaLight(lightSphere);
        Set<Light> lights = new HashSet<>();
        lights.add(areaLight);
        ColorMaterial lightMaterial = new ColorMaterial(Color.YELLOW);
        Body areaLightBody = new Body(lightSphere, lightMaterial, "Light", true);
        bodies.add(areaLightBody);

        // Camera
        Point cameraLocation = new Point(0, -80, 0);
        Vector up = new Vector(0, 0, 1);
        Vector lookAt = new Vector(0, 1, 0);
        Lens lens = new ThinLens(80, cameraLocation, 5.0, lookAt, up);
        Film film = new Film(0.1, 50, 50, 4);
        Camera camera = new Camera(film, lens, cameraLocation, lookAt, up);

        // Put it all in the scene
        return new Scene(camera, lights, bodies);
    }
}
