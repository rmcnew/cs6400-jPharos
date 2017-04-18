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
import com.starrypenguin.jpharos.core.CastRayForDevelopedPixel;
import com.starrypenguin.jpharos.core.DevelopPixel;
import com.starrypenguin.jpharos.core.Ray;
import com.starrypenguin.jpharos.core.Scene;
import com.starrypenguin.jpharos.parallel.ParallelExecutor;
import com.starrypenguin.jpharos.scenes.ImprovedSphereOnAPlane;
import com.starrypenguin.jpharos.scenes.SceneBuilder;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * jPharos
 * <p/>
 * Main class for jPharos start-up
 */
final public class jPharos {

    final public static jPharos instance = new jPharos();
    final private static long WAIT_TIME = 2500; // milliseconds
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
        System.out.println("Reading scene . . .");
        SceneBuilder sceneBuilder;

        // select the scene to render:
        //sceneBuilder = new SphereOnAPlane();
        //sceneBuilder = new ArmadilloTriangleMesh();
        //sceneBuilder = new DragonTriangleMesh();
        //sceneBuilder = new MirrorAndGlassSpheres();
        //sceneBuilder = new GlassApple();
        sceneBuilder = new ImprovedSphereOnAPlane();

        instance.scene = sceneBuilder.build();
        instance.camera = instance.scene.camera;
        System.out.println("Rendering . . .");
        instance.render(outFilename);
    }

    private void render(String outFilename) {
        java.util.List<Ray> rays = instance.camera.generateRays();
        for (Ray ray : rays) {
            if (ray != null) {
                instance.executor.submit(new CastRayForDevelopedPixel(ray));
            }
        }
        System.out.println("All initial rays submitted!");
        try {
            // wait a moment for the queue to prime
            Thread.sleep(WAIT_TIME);

            while (!instance.executor.isEmpty()) {
                Future<Film.DevelopedPixel> futureDevelopedPixel = instance.executor.pollPixels();
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
