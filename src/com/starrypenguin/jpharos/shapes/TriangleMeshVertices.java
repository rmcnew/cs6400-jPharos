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
import com.starrypenguin.jpharos.util.Shared;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * TriangleMeshVertices
 * <p/>
 * Used to manage and synchronize triangle mesh vertices and component triangles
 */
public class TriangleMeshVertices {

    private Map<Integer, Point> vertices = new HashMap<>();
    private Set<Triangle> triangles = new LinkedHashSet<>();

    public TriangleMeshVertices() {
    }

    public void addTriangle(Point v1arg, Point v2arg, Point v3arg) {
        Shared.notNull(v1arg, "Parameter v1arg cannot be null!");
        Shared.notNull(v2arg, "Parameter v2arg cannot be null!");
        Shared.notNull(v3arg, "Parameter v3arg cannot be null!");
        Point v1, v2, v3;
        // check v1 for reuse
        int v1argHash = v1arg.hashCode();
        if (vertices.containsKey(v1argHash)) {
            v1 = vertices.get(v1argHash); // we already have this vertex, reuse it   
        } else {
            v1 = v1arg;
            vertices.put(v1argHash, v1arg); // add this vertex so we can use it later
        }
        // check v2 for reuse
        int v2argHash = v2arg.hashCode();
        if (vertices.containsKey(v2argHash)) {
            v2 = vertices.get(v2argHash); // we already have this vertex, reuse it   
        } else {
            v2 = v2arg;
            vertices.put(v2argHash, v2arg); // add this vertex so we can use it later
        }
        // check v3 for reuse
        int v3argHash = v3arg.hashCode();
        if (vertices.containsKey(v3argHash)) {
            v3 = vertices.get(v3argHash); // we already have this vertex, reuse it   
        } else {
            v3 = v3arg;
            vertices.put(v3argHash, v3arg); // add this vertex so we can use it later
        }
        // Create and add the new triangle
        triangles.add(new Triangle(v1, v2, v3));
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
}
