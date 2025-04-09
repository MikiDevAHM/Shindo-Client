/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui;

import java.awt.*;

public class Style {

    private static boolean darkMode = false;
    private static boolean snapping = true;

    /**
     * Returns a color with the given transparency depending on if dark mode is active
     *
     * @param transparency The transparency the returned color should have
     * @return The black or white color which is returned
     */

    public static Color getColor(int transparency) {
        return isDarkMode() ?
                new Color(0, 0, 0, transparency) :
                new Color(255, 255, 255, transparency);
    }

    public static Color getColorPallet(int index) {

        switch (index) {
            case 0:
                return new Color(10, 10, 20);
            case 1:
                return new Color(20, 20, 40);
            case 2:
                return new Color(30, 30, 60);
            case 3:
                return new Color(40, 40, 80);
            case 4:
                return new Color(50, 50, 100);
            case 5:
                return new Color(60, 60, 120);
            case 6:
                return new Color(70, 70, 140);
            case 7:
                return new Color(80, 80, 160);
            case 8:
                return new Color(90, 90, 180);
            case 9:
                return new Color(100, 100, 200);
        }
        return new Color(10, 10, 20);
    }

    /**
     * Returns a color with the given transparency depending on if dark mode is active but reversed
     *
     * @param transparency The transparency the returned color should have
     * @return The black or white color which is returned
     */

    public static Color getReverseColor(int transparency) {
        return Style.isDarkMode() ?
                new Color(255, 255, 255, transparency) :
                new Color(0, 0, 0, transparency);
    }

    /**
     * Returns a boolean which says if the client should be in dark or light mode
     *
     * @return Boolean stating if client should be dark or light
     */
    public static boolean isDarkMode() {
        return darkMode;
    }

    public static void setDarkMode(boolean darkMode) {
        Style.darkMode = darkMode;
    }

    public static boolean isSnapping() {
        return snapping;
    }

    public static void setSnapping(boolean snapping) {
        Style.snapping = snapping;
    }
}
