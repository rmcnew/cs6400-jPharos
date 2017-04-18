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

import com.starrypenguin.jpharos.util.Shared;

import java.awt.*;

/**
 * ColorWrapper
 * <p/>
 * Thread-safe wrapper for Color class
 */
public class ColorWrapper {

    private Color color = null;

    public ColorWrapper() {}

    public synchronized void update(Color newColor) {
        Shared.notNull(newColor, "Parameter newColor cannot be null!");
        if (this.color == null) {
            this.color = newColor;
        } else {
            int currentRed = color.getRed();
            int currentGreen = color.getGreen();
            int currentBlue = color.getBlue();

            int newRed = newColor.getRed();
            int newGreen = newColor.getGreen();
            int newBlue = newColor.getBlue();

            int updatedRed = (currentRed + newRed) / 2;
            int updatedGreen = (currentGreen + newGreen) / 2;
            int updatedBlue = (currentBlue + newBlue) / 2;

            this.color = new Color(updatedRed, updatedGreen, updatedBlue);
        }
    }

    public synchronized Color getColor() {
        return this.color;
    }
}
