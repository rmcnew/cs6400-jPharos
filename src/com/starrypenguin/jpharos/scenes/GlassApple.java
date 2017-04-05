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
import com.starrypenguin.jpharos.geometry.Rectangle;
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.geometry.BoundingBox;
import com.starrypenguin.jpharos.lenses.Lens;
import com.starrypenguin.jpharos.lenses.PinholeLens;
import com.starrypenguin.jpharos.lights.Light;
import com.starrypenguin.jpharos.lights.PointLight;
import com.starrypenguin.jpharos.materials.ColorMaterial;
import com.starrypenguin.jpharos.materials.RefractiveMaterial;
import com.starrypenguin.jpharos.materials.ChromaticMaterial;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.util.TriangleMeshReader;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * GlassApple
 * <p/>
 * Second scene for Assignment 3:
 * <p>
 * Add reflective and refractive materials to your ray tracer.
 * <p>
 * Generate an image showing off your new capabilities.
 * <p>
 * You do not need to have caustics working correctly.
 */
public class GlassApple implements SceneBuilder {

    public Scene build() {
        // Bodies
        Set<Body> bodies = new HashSet<>();
        // read in shape from PLY file
        TriangleMesh triangleMesh = TriangleMeshReader.fromPlyFile("ply-input-files/apple.ply");
        RefractiveMaterial glassMaterial = new RefractiveMaterial(RefractiveMaterial.RefractionIndices.GLASS);
        //ChromaticMaterial glassMaterial = new ChromaticMaterial();
        Body meshBody = new Body(triangleMesh, glassMaterial);
        bodies.add(meshBody);
        // Use the bounding box to create colorful walls nearby
        BoundingBox boundingBox = triangleMesh.getBoundingBox();
        BoundingBox expandedBoundingBox = new BoundingBox(boundingBox.min.plus(new Point(-0.5, -0.5, -0.5)), boundingBox.max.plus(new Point(0.5, 0.5, 0.5))); 
        List<Rectangle> rectangles = expandedBoundingBox.toRectangles();
        for (int i = 0; i < rectangles.size(); i++) {
            if ((i <= 1) || (i == 3) || (i == 5)) { // 2: top, 4: front; exclude these so the camera and light are not blocked
                TriangleMesh rectMesh = rectangles.get(i).toTriangleMesh();
                Color color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
                Body body = new Body(rectMesh, new ColorMaterial(color));
                bodies.add(body);
            }
        }

        // Lights
        Light pointLight = new PointLight(new Point(0, 0.2, 0));
        Set<Light> lights = new HashSet<>();
        lights.add(pointLight);

        // Camera
        Point cameraLocation = new Point(0, 0.1, 0.11);
        Point target = new Point(-0.005, 0, 0);
        Vector up = new Vector(0, 1, 0);
        Vector lookAt = new Vector(cameraLocation, target);
        Lens lens = new PinholeLens(200);
        Film film = new Film(1, 300, 300, 1);
        Camera camera = new Camera(film, lens, cameraLocation, lookAt, up);
        // Put it all in the scene
        return new Scene(camera, lights, bodies);
    }
}
