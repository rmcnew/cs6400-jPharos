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

import com.starrypenguin.jpharos.shapes.TriangleMesh;
import com.starrypenguin.jpharos.shapes.TriangleMeshBuilder;
import org.smurn.jply.*;
import org.smurn.jply.util.NormalMode;
import org.smurn.jply.util.NormalizingPlyReader;
import org.smurn.jply.util.TesselationMode;
import org.smurn.jply.util.TextureMode;

import java.io.File;
import java.io.IOException;

/**
 * TriangleMeshReader
 * <p/>
 * Reads triangle meshes from PLY files
 */
public class TriangleMeshReader {

    public static void addVertexType(ElementReader elementReader, TriangleMeshBuilder triangleMeshBuilder) {
        try {
            Element element = elementReader.readElement();
            if (element != null) {
                double x = element.getDouble("x");
                double y = element.getDouble("y");
                double z = element.getDouble("z");
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
            plyReader = new NormalizingPlyReader(new PlyReaderFile(file), TesselationMode.TRIANGLES, NormalMode.PASS_THROUGH, TextureMode.PASS_THROUGH);
            ElementReader elementReader = plyReader.nextElementReader();
            while (elementReader != null) {
                ElementType elementType = elementReader.getElementType();
                switch (elementType.getName()) {
                    case "vertex":
                        addVertexType(elementReader, triangleMeshBuilder);
                        break;

                    case "face":

                        break;

                    case "edge":

                        break;

                    case "material":

                        break;

                    default:

                        break;
                }
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
