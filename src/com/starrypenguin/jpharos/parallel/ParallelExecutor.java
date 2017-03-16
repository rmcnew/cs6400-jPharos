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
import com.starrypenguin.jpharos.main.jPharos;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * ParallelExecutor
 * <p/>
 * Manages parallel execution of ray tracing and rendering tasks
 */
public class ParallelExecutor {

    final private static long WAIT_TIME = 2500; // milliseconds
    final public AtomicLong taskCount = new AtomicLong(0L);
    final private ConcurrentLinkedQueue<Future<Film.DevelopedPixel>> outputQueue = new ConcurrentLinkedQueue<>();
    final private ExecutorService executor = Executors.newWorkStealingPool();

    public void submit(Callable<Film.DevelopedPixel> task) {
        outputQueue.add(executor.submit(task));
    }

    public void finishExecuting() {
        while ((taskCount.get() > 0L) && (!jPharos.instance.camera.film.readyToDevelop())) {
            try {
                Thread.sleep(WAIT_TIME);
                System.out.println(String.format("Tasks queued: %d, rays cast: %d, rays hit: %d", taskCount.get(), jPharos.instance.raysCast.get(), jPharos.instance.raysHit.get()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(Runnable command) {
        executor.execute(command);
    }

    public Future<Film.DevelopedPixel> poll() throws InterruptedException {
        return outputQueue.poll();
    }

    public boolean isEmpty() {
        return outputQueue.isEmpty();
    }

    public int size() {
        return outputQueue.size();
    }

    public Stream<Future<Film.DevelopedPixel>> stream() {
        return outputQueue.stream();
    }

    public Stream<Future<Film.DevelopedPixel>> parallelStream() {
        return outputQueue.parallelStream();
    }
}
