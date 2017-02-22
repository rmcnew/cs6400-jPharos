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
 * Film
 * <p/>
 * Represents the film in the camera
 */
final public class Film {

    final public double pixelSize;       // pixels are square with side length of pixelSize
    final public int filmWidthInPixels;  // film size in pixels
    final public int filmHeightInPixels;
    final public int raysPerPixel;       // number of rays used for each pixel
    final public Color[][] colorGrid;     // used to capture results
    final private StringBuilder stringBuilder = new StringBuilder();
    final private String COLOR_DEPTH = "255";

    public Film(double pixelSize, int filmWidthInPixels, int filmHeightInPixels, int raysPerPixel) {
        Shared.notNaNAndPositive(pixelSize, "pixelSize must be positive!");
        Shared.positive(filmWidthInPixels, "filmWidthInPixels must be positive!");
        Shared.positive(filmHeightInPixels, "filmHeightInPixels must be positive!");
        Shared.positive(raysPerPixel, "raysPerPixel must be positive!");
        this.pixelSize = pixelSize;
        this.filmWidthInPixels = filmWidthInPixels;
        this.filmHeightInPixels = filmHeightInPixels;
        this.raysPerPixel = raysPerPixel;
        this.colorGrid = new Color[filmWidthInPixels][filmHeightInPixels];
    }

    public void capture(int widthIndex, int heightIndex, Color color) {
        Shared.inclusiveRangeCheck(widthIndex, 0, filmWidthInPixels - 1, "widthIndex parameter is out of range!");
        Shared.inclusiveRangeCheck(heightIndex, 0, filmHeightInPixels - 1, "heightIndex parameter is out of range!");
        Shared.notNull(color, "color Parameter cannot be null!");
        this.colorGrid[widthIndex][heightIndex] = color;
    }

    private void appendLine(String str) {
        stringBuilder.append(str);
        stringBuilder.append("\n");
    }

    private String colorToStr(Color color) {
        return color.getRed() + " " + color.getGreen() + color.getBlue();
    }

    /**
     * Write the finished film out to disk
     */
    public void develop(String outFilename) {
        appendLine(Shared.GraphicsFileFormat.ASCII_COLOR_PPM_MAGIC_NUMBER);
        appendLine(filmWidthInPixels + " " + filmHeightInPixels);
        appendLine(COLOR_DEPTH);
        for (int heightIndex = 0; heightIndex < filmHeightInPixels; heightIndex++) {
            for (int widthIndex = 0; widthIndex < filmWidthInPixels; widthIndex++) {
                stringBuilder.append(colorToStr(colorGrid[widthIndex][heightIndex]));
            }
            stringBuilder.append("\n");
        }
    }
}
