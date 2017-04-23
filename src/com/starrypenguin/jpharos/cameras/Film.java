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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

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
    final public ColorGrid colorGrid;     // used to capture results
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
        this.colorGrid = new ColorGrid(filmHeightInPixels, filmWidthInPixels);
    }

    public FilmCoordinate newFilmCoordinate(int heightIndex, int widthIndex) {
        return new FilmCoordinate(heightIndex, widthIndex);
    }

    public DevelopedPixel newDevelopedPixel(FilmCoordinate filmCoordinate, Queue<Color> colors) {
        return new DevelopedPixel(filmCoordinate, colors);
    }

    public void capture(FilmCoordinate filmCoordinate, Queue<Color> colors) {
        Shared.notNull(filmCoordinate, "Parameter filmCoordinate cannot be null!");
        Shared.notNull(colors, "Parameter colors cannot be null!");
        this.colorGrid.put(filmCoordinate.heightIndex, filmCoordinate.widthIndex, colors);
        //System.out.println(String.format("Captured pixel at heightIndex=%d, widthIndex=%d as color=%s", filmCoordinate.heightIndex, filmCoordinate.widthIndex, color));
    }

    public void capture(DevelopedPixel developedPixel) {
        Shared.notNull(developedPixel, "Parameter developedPixel cannot be null!");
        this.capture(developedPixel.filmCoordinate, developedPixel.colors);
    }

    public boolean readyToDevelop() {
        return colorGrid.readyToDevelop();
    }

    private String colorToStr(Color color) {
        return color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " ";
    }

    /**
     * Write the finished film out to disk
     */
    public void develop(String outFilename) {
        try (FileWriter fileWriter = new FileWriter(outFilename)) {
        fileWriter.write(Shared.GraphicsFileFormat.ASCII_COLOR_PPM_MAGIC_NUMBER + "\n");
        fileWriter.write(filmWidthInPixels + " " + filmHeightInPixels + "\n");
        fileWriter.write(COLOR_DEPTH + "\n");
        for (int heightIndex = 0; heightIndex < filmHeightInPixels; heightIndex++) {
            for (int widthIndex = 0; widthIndex < filmWidthInPixels; widthIndex++) {
                Color color = colorGrid.get(heightIndex, widthIndex);
                if (color != null) {
                    fileWriter.write(colorToStr(color));
                } else {
                    String errorMessage = String.format("colorGrid is null at heightIndex=%d, widthIndex=%d", heightIndex, widthIndex);
                    System.err.println(errorMessage);
                    throw new IllegalStateException(errorMessage);
                }
            }
            fileWriter.write("\n");
            fileWriter.flush();
        }
        fileWriter.write("\n");
        fileWriter.flush();
        fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final public class DevelopedPixel {

        final public FilmCoordinate filmCoordinate;
        final public Queue<Color> colors;

        public DevelopedPixel(FilmCoordinate filmCoordinate, Queue<Color> colors) {
            Shared.notNull(filmCoordinate, "Parameter filmCoordinate cannot be null!");
            Shared.notNull(colors, "Parameter colors cannot be null!");
            this.filmCoordinate = filmCoordinate;
            this.colors = colors;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DevelopedPixel that = (DevelopedPixel) o;

            if (!filmCoordinate.equals(that.filmCoordinate)) return false;
            return colors.equals(that.colors);
        }

        @Override
        public int hashCode() {
            int result = filmCoordinate.hashCode();
            result = 31 * result + colors.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "DevelopedPixel{" +
                    "filmCoordinate=" + filmCoordinate +
                    ", color=" + colors +
                    '}';
        }
    }

    // represent a coordinate on the film as a single object to improve error checking and prevent accidental swapping of width and height indices
    final public class FilmCoordinate {

        final public int heightIndex;
        final public int widthIndex;

        public FilmCoordinate(int heightIndex, int widthIndex) {
            Shared.inclusiveRangeCheck(heightIndex, 0, filmHeightInPixels - 1, "FilmCoordinate heightIndex must be between 0 and " + (filmHeightInPixels - 1));
            Shared.inclusiveRangeCheck(widthIndex, 0, filmWidthInPixels - 1, "FilmCoordinate widthIndex must be between 0 and " + (filmWidthInPixels - 1));
            this.heightIndex = heightIndex;
            this.widthIndex = widthIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FilmCoordinate that = (FilmCoordinate) o;

            if (heightIndex != that.heightIndex) return false;
            return widthIndex == that.widthIndex;
        }

        @Override
        public int hashCode() {
            int result = heightIndex;
            result = 31 * result + widthIndex;
            return result;
        }

        @Override
        public String toString() {
            return "FilmCoordinate{" +
                    "heightIndex=" + heightIndex +
                    ", widthIndex=" + widthIndex +
                    '}';
        }
    }
}
