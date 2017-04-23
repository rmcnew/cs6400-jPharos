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

package com.starrypenguin.jpharos.materials;

import com.starrypenguin.jpharos.core.Intersection;
import com.starrypenguin.jpharos.geometry.BarycentricCoordinate;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.shapes.Triangle;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.util.Shared;

import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

/**
 * ColorMaterial
 * <p/>
 * A Material with a given color or colors that does not specify light interaction
 */
public class ColorMaterial extends Material {

    private Map<Point, Color> colorMap = null;
    private Color color = null;

    public ColorMaterial(Color color) {
        Shared.notNull(color, "Parameter color cannot be null!");
        this.color = color;
    }

    public ColorMaterial(Map<Point, Color> colorMap) {
        Shared.notNull(colorMap, "Parameter colorMap cannot be null!");
        this.colorMap = colorMap;
    }

    @Override
    public Queue<Color> getColor(Intersection intersection) {
        return Material.calculateLambertianAndShadow(intersection);
    }

    @Override
    protected Color getColorInternal(Intersection intersection) {
        Shared.notNull(intersection, "Parameter intersection cannot be null!");
        if (colorMap != null) {
            Color mappedColor = getColorFromTriangleMesh(intersection);
            if (mappedColor == null) {
                return Color.BLACK;  // need to interpolate an arbitrary point to get the color
            } else {
                return mappedColor;
            }

        }
        return color;
    }

    private Color getColorFromTriangleMesh(Intersection intersection) {
        // get the Triangle from the TriangleMesh
        TriangleMesh triangleMesh = null;
        if (intersection.body.shape instanceof TriangleMesh) {
            triangleMesh = (TriangleMesh) intersection.body.shape;

            // get the Barycentric Coordinate from the Triangle using the intersectionPoint
            Optional<Triangle> maybeTriangle = triangleMesh.getIntersectingTriangle(intersection.ray);
            Triangle triangle = maybeTriangle.get();
            if (triangle == null) {
                throw new IllegalArgumentException("Inconsistent Intersection!");
            }
            BarycentricCoordinate barycentricCoordinate = triangle.calculateBarycentricCoordinateForPoint(intersection.intersectionPoint);
            //System.out.println("Found BarycentricCoordinate: " + barycentricCoordinate);

            // blend / scale the colors of the vertex points to get the Color for the intersectionPoint
            Color blendedColor = barycentricBlendColor(barycentricCoordinate, triangle);

            // return the blended Color
            return blendedColor;
        } else {
            throw new IllegalArgumentException("The intersected Shape is not a TriangleMesh!");
        }
    }

    private Color barycentricBlendColor(BarycentricCoordinate barycentricCoordinate, Triangle triangle) {
        Color v1Color = colorMap.get(triangle.v1);
        Color v2Color = colorMap.get(triangle.v2);
        Color v3Color = colorMap.get(triangle.v3);
        double v1RedScaled = (v1Color.getRed() / 255.0) * barycentricCoordinate.u;
        double v1GreenScaled = (v1Color.getGreen() / 255.0) * barycentricCoordinate.u;
        double v1BlueScaled = (v1Color.getBlue() / 255.0) * barycentricCoordinate.u;
        //System.out.println(String.format("v1 Scaled colors{ red: %f, green: %f, blue: %f }", v1RedScaled, v1GreenScaled, v1BlueScaled));

        double v2RedScaled = (v2Color.getRed() / 255.0) * barycentricCoordinate.v;
        double v2GreenScaled = (v2Color.getGreen() / 255.0) * barycentricCoordinate.v;
        double v2BlueScaled = (v2Color.getBlue() / 255.0) * barycentricCoordinate.v;
        //System.out.println(String.format("v2 Scaled colors{ red: %f, green: %f, blue: %f }", v2RedScaled, v2GreenScaled, v2BlueScaled));

        double v3RedScaled = (v3Color.getRed() / 255.0) * barycentricCoordinate.w;
        double v3GreenScaled = (v3Color.getGreen() / 255.0) * barycentricCoordinate.w;
        double v3BlueScaled = (v3Color.getBlue() / 255.0) * barycentricCoordinate.w;
        //System.out.println(String.format("v3 Scaled colors{ red: %f, green: %f, blue: %f }", v3RedScaled, v3GreenScaled, v3BlueScaled));

        float scaledRed = (float) Math.max(Math.min(v1RedScaled + v2RedScaled + v3RedScaled, 1.0f), 0.0f);
        float scaledGreen = (float) Math.max(Math.min(v1GreenScaled + v2GreenScaled + v3GreenScaled, 1.0f), 0.0f);
        float scaledBlue = (float) Math.max(Math.min(v1BlueScaled + v2BlueScaled + v3BlueScaled, 1.0f), 0.0f);
        //System.out.println(String.format("Returning blended Color: red: %f, green: %f, blue: %f", scaledRed, scaledGreen, scaledBlue));

        Color blended = new Color(scaledRed, scaledGreen, scaledBlue);
        return blended;
    }

}
