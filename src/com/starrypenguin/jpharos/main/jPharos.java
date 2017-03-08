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
import com.starrypenguin.jpharos.core.*;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.geometry.Vector;
import com.starrypenguin.jpharos.lenses.Lens;
import com.starrypenguin.jpharos.lenses.PinholeLens;
import com.starrypenguin.jpharos.lights.Light;
import com.starrypenguin.jpharos.lights.PointLight;
import com.starrypenguin.jpharos.materials.Material;
import com.starrypenguin.jpharos.parallel.ParallelExecutor;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.util.TriangleMeshReader;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * jPharos
 * <p/>
 * Main class for jPharos start-up
 */
final public class jPharos {
/*  TODO:
    == jPharos start-up ==
    1.  Read and validate any command line parameters:  "jpharos sceneDescriptionFile.json"
    Default output file is named the same as the input filename, but different extension
    -o outputFileName   :  uses "outputFileName" instead of default
    -r numberOfRaysToCast : number of rays to cast per pixel;  default to ???

    2.  Open and parse the sceneDescriptionFile.json:
    Verify file exists and we have permissions to open it
    Open file
    Slurp file contents with Jackson to produce Scene object
    Halt and fail noisly if any error occurs

    3.  Run Bounding Volume division algorithm to populate acceleration structure

    4.  Start up tracer engine:
    Determine number of CPU Cores available
    Create fixedThreadPool ExecutorService with $threadsToUse threads
    "Overseer" thread checks queue length and puts more tasks on the queue

    5.  Main Render loop:

    For each pixel in the output film:
            For j in $numberOfRaysToCast:
                    Put RayCastTask on ThreadPool
*/


    // For now, hard-code the Scene
    public static final long WAIT_TIME = 2500; // milliseconds
    public static Scene scene;
    public static Camera camera;
    public static ParallelExecutor executor = new ParallelExecutor();
    public static AtomicInteger raysCast = new AtomicInteger(0);
    public static AtomicInteger raysHit = new AtomicInteger(0);

    static {
        // Bodies
        Set<Body> bodies = new HashSet<>();

        // read in shape from PLY file
        TriangleMesh triangleMesh = TriangleMeshReader.fromPlyFile("ply-input-files/teapot.ply");
        Material greenMaterial = new Material(Color.RED);
        Body meshBody = new Body(triangleMesh, greenMaterial);
        bodies.add(meshBody);

        // Lights
        Light pointLight = new PointLight(new Point(-1, 0, 7));
        Set<Light> lights = new HashSet<>();
        lights.add(pointLight);

        // Camera
        Point cameraLocation = new Point(0, 4, 4);
        Point target = new Point(0, 0, 0);
        Vector up = new Vector(0, 0, 1);
        Vector lookAt = new Vector(cameraLocation, target).normalized();
        Lens lens = new PinholeLens(220);
        Film film = new Film(1, 300, 300, 1);
        camera = new Camera(film, lens, cameraLocation, lookAt, up);

        // Put it all in the scene
        scene = new Scene(camera, lights, bodies);
    }

    public static void main(String[] args) {
        String outFilename = "out.ppm";
        if (args.length > 0 && !args[0].isEmpty()) {
            outFilename = args[0];
        }
        List<Ray> rays = camera.generateRays();
        for (Ray ray : rays) {
            executor.submit(new CastRay(ray));
        }

        try {
            // wait a moment for the queue to prime
            Thread.sleep(WAIT_TIME);

            while (!executor.isEmpty()) {
                executor.execute(new DevelopPixel(executor.poll()));
            }
            // wait a moment for the executor to run
            Thread.sleep(WAIT_TIME);
            executor.finishExecuting();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // write out the image
        camera.develop(outFilename);

        System.out.println("Total rays cast: " + raysCast.get() + ", total rays hit: " + raysHit.get());
    }

}
