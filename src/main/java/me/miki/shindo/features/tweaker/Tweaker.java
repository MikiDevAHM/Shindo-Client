package me.miki.shindo.features.tweaker;

import com.google.gson.annotations.Expose;

public class Tweaker {

    @Expose
    private String name;

    @Expose
    private String mode;

    @Expose
    private String[] options;

    @Expose
    private String currentMode;

    @Expose
    private int modeIndex;

    @Expose
    private boolean checkToggled;

    @Expose
    private float maxNumber, currentNumber;

    /**
     * An option which can be toggled on and off
     */

    public Tweaker(String name, boolean checkToggled) {
        this.mode = "CheckBox";
        this.name = name;

        this.checkToggled = checkToggled;
    }

    /**
     * An option with a slider which can go from 0 to a given number
     */

    public Tweaker(String name, float maxNumber, float currentNumber) {
        this.mode = "Slider";
        this.name = name;

        this.maxNumber = maxNumber;
        this.currentNumber = currentNumber;
    }

    /**
     * An option which allows you to select a String from an Array of Strings also called Modes
     */

    public Tweaker(String name, String currentMode, int modeIndex, String[] options) {
        this.mode = "ModePicker";
        this.name = name;

        this.currentMode = currentMode;
        this.modeIndex = modeIndex;
        this.options = options;
    }

    public Tweaker(String name) {
        this.mode = "Category";
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isCheckToggled() {
        return checkToggled;
    }

    public void setCheckToggled(boolean checkToggled) {
        this.checkToggled = checkToggled;
    }

    public float getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(float maxNumber) {
        this.maxNumber = maxNumber;
    }

    public float getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(float currentNumber) {
        this.currentNumber = currentNumber;
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }

    public int getModeIndex() {
        return modeIndex;
    }

    public void setModeIndex(int modeIndex) {
        this.modeIndex = modeIndex;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
