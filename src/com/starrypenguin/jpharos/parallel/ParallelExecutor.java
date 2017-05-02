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

package com.starrypenguin.jpharos.parallel;

import com.starrypenguin.jpharos.cameras.Film;
import com.starrypenguin.jpharos.core.Intersection;
import com.starrypenguin.jpharos.main.jPharos;

import java.util.concurrent.*;

/**
 * ParallelExecutor
 * <p/>
 * Manages parallel execution of ray tracing and rendering tasks
 */
public class ParallelExecutor {

    final private static long WAIT_TIME = 2500; // milliseconds
    final private ConcurrentLinkedQueue<Future<Film.DevelopedPixel>> pixelOutputQueue = new ConcurrentLinkedQueue<>();
    final private ExecutorService executor = Executors.newWorkStealingPool();


    public void submit(Callable<Film.DevelopedPixel> task) {
        pixelOutputQueue.add(executor.submit(task));
    }

    public Future<Intersection> castRayForFutureIntersection(Callable<Intersection> task) {
        jPharos.instance.raysCast.incrementAndGet();
        return executor.submit(task);
    }

    public void finishExecuting() {
        while (!jPharos.instance.camera.film.readyToDevelop()) {
            try {
                Thread.sleep(WAIT_TIME * 4);
                System.out.println(String.format("Rays cast: %d, rays hit: %d", jPharos.instance.raysCast.get(), jPharos.instance.raysHit.get()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        for (int count = 0; !executor.isShutdown() && count < 10; count++) {
            try {
                Thread.sleep(WAIT_TIME * 4);
                System.out.println("Waiting for executor to shutdown. Count is " + count + " of 10 max.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(Runnable command) {
        executor.execute(command);
    }

    public Future<Film.DevelopedPixel> pollPixels() throws InterruptedException {
        return pixelOutputQueue.poll();
    }

    public boolean isEmpty() {
        return pixelOutputQueue.isEmpty();
    }

}
