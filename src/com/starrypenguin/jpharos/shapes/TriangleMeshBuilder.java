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

package com.starrypenguin.jpharos.shapes;

import com.starrypenguin.jpharos.geometry.Point;

/**
 * TriangleMeshBuilder
 * <p/>
 * TriangleMesh helper class that is used to build a TriangleMesh
 */
public class TriangleMeshBuilder {

    private TriangleMeshVertices vertices = new TriangleMeshVertices();

    public TriangleMeshBuilder addTriangle(Point v1arg, Point v2arg, Point v3arg) {
        vertices.addTriangle(v1arg, v2arg, v3arg);
        return this;
    }

    public void addVertex(Point vertex) {
        vertices.addVertex(vertex);
    }

    public void addTriangleByVertexIndex(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
        vertices.addTriangleByVertexIndex(vertexIndex1, vertexIndex2, vertexIndex3);
    }

    public TriangleMesh build() {
        if (vertices.isEmpty()) {
            throw new IllegalStateException("Cannot create a TriangleMesh from empty TriangleMeshVertices!");
        }
        Point center = vertices.getCenterPoint();
        return new TriangleMesh(center, vertices);
    }

}
