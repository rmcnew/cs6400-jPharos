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

package com.starrypenguin.jpharos.util;

import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.shapes.TriangleMeshBuilder;
import org.smurn.jply.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * TriangleMeshReader
 * <p/>
 * Reads triangle meshes from PLY files
 */
public class TriangleMeshReader {

    private static String VERTEX_INDICES = "vertex_indices";
    private static String VERTEX_INDEX = "vertex_index";

    private static void addVertexType(ElementReader elementReader, TriangleMeshBuilder triangleMeshBuilder) {
        try {
            Element element = elementReader.readElement();
            while (element != null) {
                double x = element.getDouble("x");
                double y = element.getDouble("y");
                double z = element.getDouble("z");
                //System.out.println(String.format("Adding Triangle Mesh vertex: x=%f, y=%f, z=%f", x, y, z));
                Point vertex = new Point(x, y, z);
                triangleMeshBuilder.addVertex(vertex);
                try {
                    int red = element.getInt("red");
                    int green = element.getInt("green");
                    int blue = element.getInt("blue");
                    Color pointColor = new Color(red, green, blue);
                    triangleMeshBuilder.addColor(vertex, pointColor);
                    //System.out.println("Added color:  " + pointColor + " for vertex:  " + vertex);
                } catch (IllegalArgumentException e) {
                    // do nothing
                }

                element = elementReader.readElement();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addFaceType(ElementReader elementReader, TriangleMeshBuilder  triangleMeshBuilder) {
        try {
            Element element = elementReader.readElement();
            while (element != null) {
                int[] vertexIndices = null;
                if (element.getIntList(VERTEX_INDICES) != null) {
                    vertexIndices = element.getIntList(VERTEX_INDICES);
                } else if (element.getIntList(VERTEX_INDEX) != null) {
                    vertexIndices = element.getIntList(VERTEX_INDEX);
                }
                //System.out.println("vertexIndices length is: " + vertexIndices.length);
                if (vertexIndices != null && vertexIndices.length == 3) {
                    int vertexIndex1 = vertexIndices[0];
                    int vertexIndex2 = vertexIndices[1];
                    int vertexIndex3 = vertexIndices[2];
                    //System.out.println(String.format("Adding Triangle Mesh face: v1_index=%d, v2_index=%d, v3_index=%d", vertexIndex1, vertexIndex2, vertexIndex3));
                    triangleMeshBuilder.addTriangleByVertexIndex(vertexIndex1, vertexIndex2, vertexIndex3);
                } else {
                    //throw new IllegalArgumentException("Unknown PLY triangle face property or strange number of indices used!");
                    System.err.println("Unknown PLY triangle face property or strange number of indices used!");
                }
                element = elementReader.readElement();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static TriangleMesh fromPlyFile(File file) {
        Shared.notNullExistsAndReadable(file, "file parameter cannot be null, must exist, and be readable!");
        TriangleMeshBuilder triangleMeshBuilder = new TriangleMeshBuilder();

        PlyReader plyReader = null;
        try {
            plyReader = new PlyReaderFile(file);
            ElementReader elementReader = plyReader.nextElementReader();
            while (elementReader != null) {
                ElementType elementType = elementReader.getElementType();
                switch (elementType.getName()) {
                    case "vertex":
                        addVertexType(elementReader, triangleMeshBuilder);
                        break;

                    case "face":
                        addFaceType(elementReader, triangleMeshBuilder);
                        break;

                    case "edge":
                        throw new NotImplementedException();
                        //break;

                    case "material":
                        throw new NotImplementedException();
                        //break;

                    default:
                        throw new UnsupportedOperationException("Unknown PLY element type: " + elementType.getName() +
                                                                "jPharos does not know how to handle this PLY element type!");
                }
                elementReader.close();
                elementReader = plyReader.nextElementReader();
            }


            plyReader.close();
            plyReader = null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (plyReader != null) {
                try {
                    plyReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return triangleMeshBuilder.build();
    }

    public static TriangleMesh fromPlyFile(String filename) {
        Shared.notNullAndNotEmpty(filename, "filename parameter cannot be null or empty!");
        return fromPlyFile(new File(filename));
    }

}
