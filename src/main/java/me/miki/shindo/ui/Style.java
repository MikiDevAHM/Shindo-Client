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

    public static Color getColorTheme(int color) {
        int index = 0; // init index = Shindo.getInstance().getVisualManager().getVisual("Theme Color").getModeIndex();
        switch (index) {
            case 0:
            switch (color) {
                case 0:
                    return new Color(15, 10, 10);
                case 1:
                    return new Color(20, 15, 15);
                case 2:
                    return new Color(25, 20, 20);
                case 3:
                    return new Color(30, 25, 25);
                case 4:
                    return new Color(35, 30, 30);
                case 5:
                    return new Color(40, 35, 35);
                case 6:
                    return new Color(45, 40, 40);
                case 7:
                    return new Color(50, 45, 45);
                case 8:
                    return new Color(55, 50, 50);
                case 9:
                    return new Color(60, 55, 55);
                case 10:
                    return new Color(65, 60, 60);
                case 11:
                    return new Color(70, 65, 65);
                case 12:
                    return new Color(255, 10, 10);
                case 13:
                    return new Color(10, 255, 10);
                case 14:
                    return new Color(10, 10, 255);
                case 15:
                    return new Color(255, 255, 10);
                case 16:
                    return new Color(10, 255, 255);
                case 17:
                    return new Color(255, 10, 255);
                case 18:
                    return new Color(0, 0, 0);
                case 19:
                    return new Color(255, 255, 255);
            }
            case 1:
                switch (color) {
                    case 0:
                        return new Color(25, 10, 10);
                    case 1:
                        return new Color(30, 15, 15);
                    case 2:
                        return new Color(35, 20, 20);
                    case 3:
                        return new Color(40, 25, 25);
                    case 4:
                        return new Color(45, 30, 30);
                    case 5:
                        return new Color(50, 35, 35);
                    case 6:
                        return new Color(55, 40, 40);
                    case 7:
                        return new Color(60, 45, 45);
                    case 8:
                        return new Color(65, 50, 50);
                    case 9:
                        return new Color(70, 55, 55);
                    case 10:
                        return new Color(75, 60, 60);
                    case 11:
                        return new Color(80, 65, 65);
                    case 12:
                        return new Color(255, 10, 10);
                    case 13:
                        return new Color(10, 255, 10);
                    case 14:
                        return new Color(10, 10, 255);
                    case 15:
                        return new Color(255, 255, 10);
                    case 16:
                        return new Color(10, 255, 255);
                    case 17:
                        return new Color(255, 10, 255);
                    case 18:
                        return new Color(0, 0, 0);
                    case 19:
                        return new Color(255, 255, 255);
                }
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
