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

package com.starrypenguin.jpharos.core;

import com.starrypenguin.jpharos.cameras.Film;
import com.starrypenguin.jpharos.main.jPharos;
import com.starrypenguin.jpharos.util.Shared;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * DevelopPixel
 * <p/>
 * Wait for a Film.DevelopedPixel to become ready and then persist it
 */
public class DevelopPixel implements Runnable {

    private Future<Film.DevelopedPixel> futureDevelopedPixel;

    public DevelopPixel(Future<Film.DevelopedPixel> futureDevelopedPixel) {
        Shared.notNull(futureDevelopedPixel, "Parameter futureDevelopedPixel cannot be null!");
        this.futureDevelopedPixel = futureDevelopedPixel;
    }

    @Override
    public void run() {
        jPharos.instance.executor.taskCount.incrementAndGet();
        try {
            if (futureDevelopedPixel != null) {
                Film.DevelopedPixel developedPixel = futureDevelopedPixel.get();
                if (developedPixel != null) {
                    jPharos.instance.camera.film.capture(developedPixel);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.exit(1);
        }
        jPharos.instance.executor.taskCount.decrementAndGet();
    }
}
