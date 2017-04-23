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

package com.starrypenguin.jpharos.cameras;

import java.awt.*;

/**
 * AverageColors
 * <p/>
 * Functor that "averages" colors by RGB component
 */
public class AverageColors {

    public static Color average(Color color1, Color color2) {
        if (color1 == null && color2 == null) {
            return null;
        } else if (color1 == null) {
            return color2;
        } else if (color2 == null) {
            return color1;
        } else { // both colors are not null
            int color1Red = color1.getRed();
            int color1Green = color1.getGreen();
            int color1Blue = color1.getBlue();

            int color2Red = color2.getRed();
            int color2Green = color2.getGreen();
            int color2Blue = color2.getBlue();

            int updatedRed = (color1Red + color2Red) / 2;
            int updatedGreen = (color1Green + color2Green) / 2;
            int updatedBlue = (color1Blue + color2Blue) / 2;

            return new Color(updatedRed, updatedGreen, updatedBlue);
        }
    }
}
