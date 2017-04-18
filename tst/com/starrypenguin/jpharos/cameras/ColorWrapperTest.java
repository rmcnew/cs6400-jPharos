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

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

/**
 * ColorWrapperTest
 * <p/>
 * Tests for ColorWrapper
 */
public class ColorWrapperTest {

    @Test
    public void updateTest() {
        ColorWrapper colorWrapper = new ColorWrapper();
        Color nullColor = colorWrapper.getColor();
        System.out.println("nullColor is: " + nullColor);
        Assert.assertNull(nullColor);

        colorWrapper.update(Color.BLACK);
        Color blackColor = colorWrapper.getColor();
        System.out.println("blackColor is: " + blackColor);
        Assert.assertEquals(blackColor, Color.BLACK);

        colorWrapper.update(Color.WHITE);
        Color grayColor = colorWrapper.getColor();
        System.out.println("grayColor is: " + grayColor);

    }
}
