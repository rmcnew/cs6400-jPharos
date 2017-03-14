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
import com.starrypenguin.jpharos.materials.ColorMaterial;
import com.starrypenguin.jpharos.parallel.ParallelExecutor;
import com.starrypenguin.jpharos.shapes.Sphere;
import com.starrypenguin.jpharos.shapes.Triangle;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.util.TriangleMeshReader;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
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
    final public static jPharos instance = new jPharos();
    public Scene scene;
    public Camera camera;
    public ParallelExecutor executor = new ParallelExecutor();
    public AtomicInteger raysCast = new AtomicInteger(0);
    public AtomicInteger raysHit = new AtomicInteger(0);

    private jPharos() {
    }

    public static void main(String[] args) {
        String outFilename = "out.ppm";
        if (args.length > 0 && !args[0].isEmpty()) {
            outFilename = args[0];
        }
        // instance.prepareSphereAndTrianglesScene();
        System.out.println("Reading scene . . .");
        instance.prepareSceneWithTriangleMesh();
        System.out.println("Rendering . . .");
        instance.render(outFilename);

    }

    private void prepareSphereAndTrianglesScene() {
        // Bodies
        Set<Body> bodies = new HashSet<>();
        // Sphere
        Point sphereLocation = new Point(0, 0, 70);
        Sphere sphere = new Sphere(sphereLocation, 40);
        ColorMaterial redStuff = new ColorMaterial(Color.RED);
        Body sphereBody = new Body(sphere, redStuff);
        bodies.add(sphereBody);

        //Triangles to make a plane
        Point quadrant1 = new Point(150, 100, 0);
        Point quadrant2 = new Point(-150, 100, 0);
        Point quadrant3 = new Point(-150, -100, 0);
        Point quadrant4 = new Point(150, -100, 0);
        Triangle triangle1 = new Triangle(quadrant4, quadrant1, quadrant3);
        Triangle triangle2 = new Triangle(quadrant1, quadrant2, quadrant3);
        ColorMaterial whiteStuff = new ColorMaterial(Color.WHITE);
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

    private void prepareSceneWithTriangleMesh() {
        // Bodies
        Set<Body> bodies = new HashSet<>();

        // read in shape from PLY file
        TriangleMesh triangleMesh = TriangleMeshReader.fromPlyFile("ply-input-files/dolphins_colored.ply");
        ColorMaterial material = new ColorMaterial(triangleMesh.getColorMap());
        //ColorMaterial material = new ColorMaterial(Color.BLUE);
        Body meshBody = new Body(triangleMesh, material);
        bodies.add(meshBody);

        // Lights
        Light pointLight = new PointLight(new Point(0, 0, 500));
        Set<Light> lights = new HashSet<>();
        lights.add(pointLight);

        // Camera
        Point cameraLocation = new Point(0, 500, 4);
        Point target = new Point(0, 0, 0);
        Vector up = new Vector(0, 0, 1);
        Vector lookAt = new Vector(cameraLocation, target).normalized();
        Lens lens = new PinholeLens(220);
        Film film = new Film(1, 300, 300, 1);
        camera = new Camera(film, lens, cameraLocation, lookAt, up);

        // Put it all in the scene
        scene = new Scene(camera, lights, bodies);
    }

    public void render(String outFilename) {
        java.util.List<Ray> rays = instance.camera.generateRays();
        for (Ray ray : rays) {
            if (ray != null) {
                instance.executor.submit(new CastRay(ray));
            }
        }

        try {
            // wait a moment for the queue to prime
            Thread.sleep(WAIT_TIME);

            while (!instance.executor.isEmpty()) {
                Future<Film.DevelopedPixel> futureDevelopedPixel = instance.executor.poll();
                if (futureDevelopedPixel != null) {
                    instance.executor.execute(new DevelopPixel(futureDevelopedPixel));
                } else {
                    Thread.sleep(WAIT_TIME);
                }
            }
            // wait a moment for the executor to run
            Thread.sleep(WAIT_TIME);
            instance.executor.finishExecuting();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // write out the image
        instance.camera.develop(outFilename);

        System.out.println("Total rays cast: " + instance.raysCast.get() + ", total rays hit: " + instance.raysHit.get());
    }

}
