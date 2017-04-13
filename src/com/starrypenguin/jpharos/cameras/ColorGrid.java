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
 * ColorGrid
 * <p/>
 * Thread-safe 2D "grid" of Colors for use in Film
 */
public class ColorGrid {

    final private int filmHeightInPixels;
    final private int filmWidthInPixels;
    final private ColorWrapper[][] colorGrid;

    public ColorGrid(int filmHeightInPixels, int filmWidthInPixels) {
        Shared.positive(filmHeightInPixels, "Parameter filmHeightInPixels must be positive!");
        Shared.positive(filmWidthInPixels, "Parameter filmWidthInPixels must be positive!");
        this.filmHeightInPixels = filmHeightInPixels;
        this.filmWidthInPixels = filmWidthInPixels;
        this.colorGrid = new ColorWrapper[filmHeightInPixels][filmWidthInPixels];
    }

    public void put(int heightIndex, int widthIndex, Color color) {
        this.colorGrid[heightIndex][widthIndex].update(color);
    }

    public Color get(int heightIndex, int widthIndex) {
        return this.colorGrid[heightIndex][widthIndex].getColor();
    }

    public boolean readyToDevelop() {
        for (int heightIndex = 0; heightIndex < filmHeightInPixels; heightIndex++) {
            for (int widthIndex = 0; widthIndex < filmWidthInPixels; widthIndex++) {
                if (colorGrid[heightIndex][widthIndex] == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
