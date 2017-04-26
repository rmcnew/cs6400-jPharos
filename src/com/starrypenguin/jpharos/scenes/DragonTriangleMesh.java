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
import com.starrypenguin.jpharos.materials.ChromaticMaterial;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.util.TriangleMeshReader;

import java.util.HashSet;
import java.util.Set;

/**
 * DragonTriangleMesh
 * <p/>
 * Second Scene for Assignment 2:
 * <p>
 * Extend your ray tracer to be able to read large triangular meshes from a file as part of a scene.
 * <p>
 * Support the PLY format.
 * <p>
 * Also add a BVH acceleration structure with the surface area heuristic.
 * <p>
 * Turn in an image of a model containing at least 1 M triangles.
 */
public class DragonTriangleMesh implements SceneBuilder {

    public Scene build() {
        // Bodies
        Set<Body> bodies = new HashSet<>();

        // read in shape from PLY file
        TriangleMesh triangleMesh = TriangleMeshReader.fromPlyFile("ply-input-files/dragon_quick.ply");
        //ColorMaterial material = new ColorMaterial(triangleMesh.getColorMap()); // use with PLY files that have RGB values per vertex
        //ColorMaterial material = new ColorMaterial(Color.GREEN.darker()); // use for a fixed color
        ChromaticMaterial material = new ChromaticMaterial(); // use with PLY files that have no colors; colors are based on vertex location
        Body meshBody = new Body(triangleMesh, material);
        bodies.add(meshBody);

        // Lights
        Light pointLight = new PointLight(new Point(0, 0.2, 0));
        Set<Light> lights = new HashSet<>();
        lights.add(pointLight);

        // Camera
        Point cameraLocation = new Point(0, 0.12, 0.15);
        Point target = new Point(-0.005, 0.12, 0);
        Vector up = new Vector(0, 1, 0);
        Vector lookAt = new Vector(cameraLocation, target);
        Lens lens = new PinholeLens(200, cameraLocation);
        Film film = new Film(1, 300, 300, 1);
        Camera camera = new Camera(film, lens, cameraLocation, lookAt, up);

        // Put it all in the scene
        return new Scene(camera, lights, bodies);
    }
}
