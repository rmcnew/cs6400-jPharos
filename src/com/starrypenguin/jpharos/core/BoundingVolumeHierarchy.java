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

package com.starrypenguin.jpharos.core;

import com.starrypenguin.jpharos.geometry.BoundingBox;
import com.starrypenguin.jpharos.materials.NullMaterial;

import java.util.HashSet;
import java.util.Set;

/**
 * BoundingVolumeHierarchy
 * <p/>
 * Implements a bounding volume hierarchy (BVH) to accelerate ray intersection detection
 * The current implementation uses a surface area heuristic to construct the BVH and a
 * simple binary tree to represent the hierarchy.
 */
public class BoundingVolumeHierarchy {

    private Body head;

    public BoundingVolumeHierarchy(Set<Body> bodies) {
        Set<Body> unpairedBodies = new HashSet<>();
        unpairedBodies.addAll(bodies);

        while (unpairedBodies.size() > 1) {
            Body left = null;
            Body right = null;
            double bestSurfaceAreaHeuristic = 0.0;
            for (Body bodyA : bodies) {
                for (Body bodyB : bodies) {
                    if (bodyA == bodyB) {
                        continue; // we cannot group a body with itself
                    }
                    double surfaceAreaHeuristic = calculateSurfaceAreaHeuristic(bodyA, bodyB);
                    if (surfaceAreaHeuristic > bestSurfaceAreaHeuristic) {
                        bestSurfaceAreaHeuristic = surfaceAreaHeuristic;
                        left = bodyA;
                        right = bodyB;
                    }
                }
            }
            if (left != null && right != null) {
                // combine the best
                BvhNode node = new BvhNode(left, right);
                // remove the newly combined children
                unpairedBodies.remove(left);
                unpairedBodies.remove(right);
                // add back the parent
                unpairedBodies.add(node);
            }
        }
        head = unpairedBodies.iterator().next();
    }

    /**
     * If Body b1 and Body b2 were grouped together as BvhNode siblings,
     * how would their actual combined surface area compare to the surface area
     * of their BoundingBox?
     *
     * @param b1 Body to consider grouping together
     * @param b2 Another Body to consider grouping together
     * @return double giving the surface area heuristic ratio; a higher
     * surface area heuristic ratio indicates a better fit and less empty
     * space in the resulting BoundingBox
     */
    private double calculateSurfaceAreaHeuristic(Body b1, Body b2) {
        double b1SurfaceArea = b1.surfaceArea();
        double b2SurfaceArea = b2.surfaceArea();
        double boundingBoxSurfaceArea = b1.getBoundingBox().union(b2.getBoundingBox()).surfaceArea();
        return (b1SurfaceArea / boundingBoxSurfaceArea) + (b2SurfaceArea / boundingBoxSurfaceArea);
    }

    public Intersection castRay(Ray ray) {
        Intersection maybeIntersection = null;
        Body currentBody = head;
        while (currentBody instanceof BvhNode) {
            BvhNode currentNode = (BvhNode) currentBody;
            if (currentNode.IntersectsP(ray)) {
                // Implement a recursive traversal of the BVH tree
            }
        }
        return maybeIntersection;
    }

    /**
     * BvhNodes are the non-leaf nodes in the BVH
     * Leaf nodes are the actual Shape objects in the Scene; we
     * extend Body to allow BvhNodes to be nested, but BvhNodes
     * are not Body objects since they have no corresponding Material
     */
    class BvhNode extends Body {
        final private Body left;
        final private Body right;
        // the BvhNode's BoundingBox is in the "shape" member variable

        BvhNode(Body left, Body right) {
            // we cannot validate non-null parameters since we need to call super first;
            // to overcome this, validate outside the constructor call
            super(left.getBoundingBox().union(right.getBoundingBox()), NullMaterial.instance());
            this.left = left;
            this.right = right;

        }

        @Override
        public boolean IntersectsP(Ray ray) {
            return left.IntersectsP(ray) || right.IntersectsP(ray);
        }

        @Override
        public Intersection Intersects(Ray ray) {
            throw new IllegalArgumentException("Call the Intersects method on true Body objects, not BvhNodes!");
        }

        @Override
        public BoundingBox getBoundingBox() {
            return shape.getBoundingBox();
        }

        @Override
        public double surfaceArea() {
            return shape.surfaceArea();
        }
    } // ============ End BvhNode ==================


}
