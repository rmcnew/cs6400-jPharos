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

/**
 * jPharos
 * <p/>
 * Main class for jPharos start-up
 */
public class jPharos {
/*
    == jPharos start-up ==
    1.  Read and validate any command line parameters:  "jpharos sceneDescriptionFile.json"
    Default output file is named the same as the input filename, but different extension
    -o outputFileName   :  uses "outputFileName" instead of default
    -v verbose mode : print out verbose output
    -n threadsToUse : use the number of threads indicated up to a maximum number of cores available + 2;
    e.g. if user specifies 20 threads, but only 4 CPU cores detected, use a maximum of (CPU_Core_Count +2) = 6 threads
    and print a warning message;  default to $CPU_Core_Count threads
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

}
