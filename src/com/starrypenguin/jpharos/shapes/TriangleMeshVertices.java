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

import java.awt.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * TriangleMeshVertices
 * <p/>
 * Used to manage and synchronize triangle mesh vertices and component triangles
 */
public class TriangleMeshVertices {

    private ArrayList<Point> verticesList = new ArrayList<>();
    private HashMap<Point, Color> colorMap = new HashMap<>();
    private Set<Triangle> triangles = new LinkedHashSet<>();
    private BoundingBox boundingBox = null;
    private double surfaceArea = 0.0;

    public TriangleMeshVertices() {
    }

    public void addVertex(Point vertex) {
        Shared.notNull(vertex, "Parameter vertex cannot be null!");
        verticesList.add(vertex);
    }

    public void addColor(Point point, Color color) {
        Shared.notNull(point, "Parameter point cannot be null!");
        Shared.notNull(color, "Parameter color cannot be null!");
        colorMap.put(point, color);
    }

    private void addTriangle(Triangle triangleToAdd) {
        triangles.add(triangleToAdd);
        if (boundingBox == null) {
            boundingBox = triangleToAdd.getBoundingBox();
        } else {
            boundingBox = boundingBox.union(triangleToAdd.getBoundingBox());
        }
        surfaceArea += triangleToAdd.surfaceArea(); // assume that this is a convex hull
    }

    public void addTriangle(Point v1arg, Point v2arg, Point v3arg) {
        Shared.notNull(v1arg, "Parameter v1arg cannot be null!");
        Shared.notNull(v2arg, "Parameter v2arg cannot be null!");
        Shared.notNull(v3arg, "Parameter v3arg cannot be null!");
        addVertex(v1arg);
        addVertex(v2arg);
        addVertex(v3arg);
        // Create and add the new triangle
        Triangle triangleToAdd = new Triangle(v1arg, v2arg, v3arg);
        addTriangle(triangleToAdd);
    }

    public void addTriangleByVertexIndex(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
        //System.out.println(String.format("addTriangleByVertexIndex: vI1=%d, vI2=%d, vI3=%d", vertexIndex1, vertexIndex2, vertexIndex3));
        int vertexMaxIndex = verticesList.size() - 1;
        try {
            Shared.inclusiveRangeCheck(vertexIndex1, 0, vertexMaxIndex, String.format("Parameter vertexIndex1 must be between 0 and %d!  vertexIndex1 value was %d", vertexMaxIndex, vertexIndex1));
            Shared.inclusiveRangeCheck(vertexIndex2, 0, vertexMaxIndex, String.format("Parameter vertexIndex2 must be between 0 and %d!  vertexIndex2 value was %d", vertexMaxIndex, vertexIndex2));
            Shared.inclusiveRangeCheck(vertexIndex3, 0, vertexMaxIndex, String.format("Parameter vertexIndex3 must be between 0 and %d!  vertexIndex3 value was %d", vertexMaxIndex, vertexIndex3));
            Triangle triangleToAdd = new Triangle(verticesList.get(vertexIndex1), verticesList.get(vertexIndex2), verticesList.get(vertexIndex3));
            addTriangle(triangleToAdd);
        } catch (IllegalArgumentException e) {
            // do nothing
        }
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

    public Map<Point, Color> getColorMap() {
        return colorMap;
    }
}
