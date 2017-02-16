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

import java.util.List;
import java.util.concurrent.*;

/**
 * ParallelExecutor
 * <p/>
 * Manages parallel execution of ray tracing and rendering tasks
 */
public class ParallelExecutor {

    final private static ExecutorService executor = Executors.newWorkStealingPool();

    public static void shutdown() {
        executor.shutdown();
    }

    public static List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }

    public static boolean isShutdown() {
        return executor.isShutdown();
    }

    public static boolean isTerminated() {
        return executor.isTerminated();
    }

    public static boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    public static Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    public static void execute(Runnable command) {
        executor.execute(command);
    }
}
