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

import com.starrypenguin.jpharos.geometry.BoundingBox;
import com.starrypenguin.jpharos.geometry.Point;
import com.starrypenguin.jpharos.util.Shared;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * TriangleMeshVertices
 * <p/>
 * Used to manage and synchronize triangle mesh vertices and component triangles
 */
public class TriangleMeshVertices {

    private Map<Integer, Point> vertices = new LinkedMap<>();
    private Set<Triangle> triangles = new LinkedHashSet<>();
    private BoundingBox boundingBox = new BoundingBox();
    private double surfaceArea = 0.0;

    public TriangleMeshVertices() {
    }

    public Point addVertex(Point vertex) {
        Shared.notNull(vertex, "Parameter vertex cannot be null!");
        int vertexHash = vertex.hashCode();
        Point retVal;
        if (vertices.containsKey(vertexHash)) {
            retVal = vertices.get(vertexHash); // we already have this vertex, reuse it
        } else {
            retVal = vertex;
            vertices.put(vertexHash, vertex); // add this vertex so we can use it later
        }
        return retVal;
    }

    private void addTriangle(Triangle triangleToAdd) {
        triangles.add(triangleToAdd);
        boundingBox = boundingBox.union(triangleToAdd.getBoundingBox());
        surfaceArea += triangleToAdd.surfaceArea();
    }

    public void addTriangle(Point v1arg, Point v2arg, Point v3arg) {
        Shared.notNull(v1arg, "Parameter v1arg cannot be null!");
        Shared.notNull(v2arg, "Parameter v2arg cannot be null!");
        Shared.notNull(v3arg, "Parameter v3arg cannot be null!");
        Point v1, v2, v3;
        // check v1 for reuse
        v1 = addVertex(v1arg);
        // check v2 for reuse
        v2 = addVertex(v2arg);
        // check v3 for reuse
        v3 = addVertex(v3arg);
        // Create and add the new triangle
        Triangle triangleToAdd = new Triangle(v1, v2, v3);
        addTriangle(triangleToAdd);
    }

    public void addTriangleByVertexIndex(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
        int vertexMaxIndex = vertices.size() - 1;
        Shared.inclusiveRangeCheck(vertexIndex1, 0, vertexMaxIndex, String.format("Parameter vertexIndex1 must be between 0 and %d", vertexMaxIndex));
        Shared.inclusiveRangeCheck(vertexIndex2, 0, vertexMaxIndex, String.format("Parameter vertexIndex2 must be between 0 and %d", vertexMaxIndex));
        Shared.inclusiveRangeCheck(vertexIndex3, 0, vertexMaxIndex, String.format("Parameter vertexIndex3 must be between 0 and %d", vertexMaxIndex));
        Triangle triangleToAdd = new Triangle(vertices.get(vertexIndex1), vertices.get(vertexIndex2), vertices.get(vertexIndex3));
        addTriangle(triangleToAdd);
    }

    public int size() {
        return triangles.size();
    }

    public boolean isEmpty() {
        return triangles.isEmpty();
    }

    public boolean contains(Object o) {
        return triangles.contains(o);
    }

    public Iterator<Triangle> iterator() {
        return triangles.iterator();
    }

    public Spliterator<Triangle> spliterator() {
        return triangles.spliterator();
    }

    public Stream<Triangle> stream() {
        return triangles.stream();
    }

    public Stream<Triangle> parallelStream() {
        return triangles.parallelStream();
    }

    public void forEach(Consumer<? super Triangle> action) {
        triangles.forEach(action);
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public Point getCenterPoint() {
        return this.boundingBox.getCenterPoint();
    }

    public double getSurfaceArea() {
        return surfaceArea;
    }
}
