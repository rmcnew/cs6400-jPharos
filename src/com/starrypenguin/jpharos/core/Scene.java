package com.starrypenguin.jpharos.core;

import com.starrypenguin.jpharos.cameras.Camera;

import java.util.List;

/**
 * Scene
 * <p/>
 * A scene that will be rendered
 * <p/>
 * Author: Richard Scott McNew
 */
public class Scene {

    private Camera camera;
    private List<Light> lights;
    private List<Body> bodies;
}
