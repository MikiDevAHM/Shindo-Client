package me.miki.shindo.feature.option;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

public class OptionManager {

    public ArrayList<Option> optionList  = new ArrayList<>();

    public OptionManager() {
        init();
    }

    public void init() {
    	addOption(new Option("Style"));
    	addOption(new Option("Font Changer", "Arial", 0,
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        addOption(new Option("Color", new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));

        addOption(new Option("Misc"));
        addOption(new Option("Bordeless FullScreen", false));
        addOption(new Option("Minimal View Bobbing", true));
        addOption(new Option("Fire Height", 50, 0));
        

        addOption(new Option("Performance"));
        addOption(new Option("Rounded Corners", true));
        addOption(new Option("Chunk Optimization", false));
        
        addOption(new Option("Chat"));
        addOption(new Option("Transparent Chat", false));
        addOption(new Option("Compact Chat", false));
        
        addOption(new Option("Network"));
        addOption(new Option("Resource Exploit Fix", false));
        
        
        addOption(new Option("Fixes"));
        addOption(new Option("Hit Dalay Fix", false));
        addOption(new Option("Mouse Delay Fix", false));
        addOption(new Option("TCP No Delay Fix", true));
        
        //addOption(new Option("Fix Vbo Sky", false));
        addOption(new Option("Fix Stitching", false));
        addOption(new Option("Fix Smooth Lighting", false));
        addOption(new Option("Fix Brightness", false));

        addOption(new Option("Controls"));
        addOption(new Option("ModMenu Keybinding", Keyboard.KEY_RSHIFT));
    	
    }

    /**
     * Adds an option to the options List
     *
     * @param option The option to be added
     */

    public void addOption(Option option) {
        optionList.add(option);
    }

    /**
     * @return Returns an Arraylist of all options
     */

    public ArrayList<Option> getOptions() {
        return optionList;
    }

    /**
     * Returns an option with a given name
     *
     * @param name The option name
     * @return The option
     */

    public Option getOptionByName(String name) {
        for (Option option : optionList) {
            if (option.getName().equalsIgnoreCase(name)) {
                return option;
            }
        }
        return null;
    }
}