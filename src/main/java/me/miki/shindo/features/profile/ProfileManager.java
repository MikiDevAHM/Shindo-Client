package me.miki.shindo.features.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.miki.shindo.Shindo;
import me.miki.shindo.features.color.ColorManager;
import me.miki.shindo.features.color.Theme;
import me.miki.shindo.features.file.FileManager;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.ModManager;
import me.miki.shindo.features.options.Option;
import me.miki.shindo.features.profile.mainmenu.BackgroundManager;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.helpers.ColorHelper;
import me.miki.shindo.helpers.file.FileHelper;
import me.miki.shindo.helpers.logger.ShindoLogger;
import me.miki.shindo.helpers.network.JsonHelper;
import me.miki.shindo.ui.hudeditor.impl.HudMod;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProfileManager {

    private final CopyOnWriteArrayList<Profile> profiles = new CopyOnWriteArrayList<Profile>();
    private final BackgroundManager backgroundManager;

    public ProfileManager() {
        backgroundManager = new BackgroundManager();
        this.loadProfiles();
    }

    public void loadProfiles() {

        File profileDir = Shindo.getInstance().getFileManager().getProfileDir();
        int id = 0;

        profiles.clear();

        for (File f : profileDir.listFiles()) {

            if (FileHelper.getExtension(f).equals("json")) {
                if (f.getName().equals("Default.json")) {
                    load(f);
                }
                else {
                    try (FileReader reader = new FileReader(f)) {

                        String serverIp = "";
                        ProfileIcon icon = ProfileIcon.GRASS;
                        ProfileType type = ProfileType.ALL;

                        Gson gson = new GsonBuilder()
                                .create();

                        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                        JsonObject profileData = JsonHelper.getObjectProperty(jsonObject, "Profile Data");

                        serverIp = JsonHelper.getStringProperty(profileData, "Server", "");
                        icon = ProfileIcon.getIconById(JsonHelper.getIntProperty(profileData, "Icon", ProfileIcon.GRASS.getId()));
                        type = ProfileType.getTypeById(JsonHelper.getIntProperty(profileData, "Type", ProfileType.ALL.getId()));

                        Profile p = new Profile(id, serverIp, f, icon);

                        p.setType(type);

                        profiles.add(p);

                        id++;
                    } catch (Exception e) {
                        ShindoLogger.error("Failed to load profile", e);
                    }
                }
            }
        }

        profiles.add(new Profile(999, "", null, null));
    }

    public void save(File file, String serverIp, ProfileType type, ProfileIcon icon) {

        Shindo instance = Shindo.getInstance();
        ModManager modManager = instance.getModManager();
        ColorManager colorManager = instance.getColorManager();

        try (FileWriter writer = new FileWriter(file)) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonObject jsonObject = new JsonObject();
            JsonObject appJsonObject = new JsonObject();
            JsonObject modJsonObject = new JsonObject();
            JsonObject profileData = new JsonObject();

            profileData.addProperty("Icon", icon.getId());
            profileData.addProperty("Type", type.getId());
            profileData.addProperty("Server", serverIp);

            jsonObject.add("Profile Data", profileData);

            appJsonObject.addProperty("Accent Color", colorManager.getCurrentColor().getName());
            appJsonObject.addProperty("Theme", colorManager.getTheme().getId());
            appJsonObject.addProperty("Background", backgroundManager.getCurrentBackground().getId());

            jsonObject.add("Appearance", appJsonObject);

            for (Mod m : modManager.getMods()) {

                JsonObject mJsonObject = new JsonObject();

                mJsonObject.addProperty("Toggle", m.isToggled());

                if (instance.getHudEditor().getHudMod(m.getName()) != null) {

                    HudMod hMod = instance.getHudEditor().getHudMod(m.getName());

                    //mJsonObject.addProperty("Toggle", hMod.isToggled());
                    mJsonObject.addProperty("X", hMod.getX());
                    mJsonObject.addProperty("Y", hMod.getY());
                    mJsonObject.addProperty("Width", hMod.getW());
                    mJsonObject.addProperty("Height", hMod.getH());
                    mJsonObject.addProperty("Size", hMod.getSize());
                }

                modJsonObject.add(m.getName(), mJsonObject);

                if (instance.getSettingManager().getSettingsByMod(m) != null) {

                    JsonObject sJsonObject = new JsonObject();

                    for (Setting s : instance.getSettingManager().getSettingsByMod(m)) {

                        if (s.getMode().equalsIgnoreCase("ColorPicker")) {

                            sJsonObject.addProperty(s.getName(), s.getColor().getRGB());
                            sJsonObject.addProperty(s.getName(), s.getSideColor().getRGB());
                            sJsonObject.addProperty(s.getName(), s.getSideSlider());

                            float[] values = s.getMainSlider();
                            JsonArray sliderArray = new JsonArray();

                            for (float value : values) {
                                sliderArray.add(value);
                            }

                            sJsonObject.add(s.getName(), sliderArray);
                        }

                        if (s.getMode().equalsIgnoreCase("CheckBox")) {

                            sJsonObject.addProperty(s.getName(), s.isCheckToggled());
                        }

                        if (s.getMode().equalsIgnoreCase("ModePicker")) {

                            sJsonObject.addProperty(s.getName(), s.getCurrentMode());
                            sJsonObject.addProperty(s.getName(), s.getModeIndex());
                        }

                        if (s.getMode().equalsIgnoreCase("Slider")) {

                            sJsonObject.addProperty(s.getName(), s.getCurrentNumber());
                        }

                        if (s.getMode().equalsIgnoreCase("TextBox")) {

                            sJsonObject.addProperty(s.getName(), s.getText());
                        }

                        if (s.getMode().equalsIgnoreCase("Keybinding")) {

                            sJsonObject.addProperty(s.getName(), s.getKey());
                        }

                        if (s.getMode().equalsIgnoreCase("CellGrid")) {

                            JsonArray outerArray = new JsonArray();

                            boolean[][] cells = s.getCells();
                            for (boolean[] row : cells) {
                                JsonArray innerArray = new JsonArray();
                                for (boolean cell : row) {
                                    innerArray.add(cell);
                                }
                                outerArray.add(innerArray);
                            }
                            sJsonObject.add(s.getName(), outerArray);
                        }
                    }
                    mJsonObject.add("Settings", sJsonObject);
                }
            }

            jsonObject.add("Mods", modJsonObject);

            JsonObject oJsonObject = new JsonObject();

            for (Option o : instance.getOptionManager().getOptions()) {

                if (o.getMode().equalsIgnoreCase("ColorPicker")) {
                    oJsonObject.addProperty(o.getName(), o.getColor().getRGB());
                    oJsonObject.addProperty(o.getName(), o.getSideColor().getRGB());
                    oJsonObject.addProperty(o.getName(), o.getSideSlider());
                    float[] values = o.getMainSlider();
                    JsonArray sliderArray = new JsonArray();
                    for (float value : values) {
                        sliderArray.add(value);
                    }
                    oJsonObject.add(o.getName(), sliderArray);
                }

                if (o.getMode().equalsIgnoreCase("CheckBox")) {
                    oJsonObject.addProperty(o.getName(), o.isCheckToggled());
                }

                if (o.getMode().equalsIgnoreCase("ModePicker")) {
                    oJsonObject.addProperty(o.getName(), o.getCurrentMode());
                    oJsonObject.addProperty(o.getName(), o.getModeIndex());
                }

                if (o.getMode().equalsIgnoreCase("Slider")) {
                    oJsonObject.addProperty(o.getName(), o.getCurrentNumber());
                }

                if (o.getMode().equalsIgnoreCase("TextBox")) {
                    oJsonObject.addProperty(o.getName(), o.getText());
                }

                if (o.getMode().equalsIgnoreCase("Keybinding")) {
                    oJsonObject.addProperty(o.getName(), o.getKey());
                }
            }
            jsonObject.add("Options", oJsonObject);

            JsonObject cJsonObject = new JsonObject();

            for (Chat c : instance.getChatManager().getChat()) {

                if (c.getMode().equalsIgnoreCase("ColorPicker")) {
                    cJsonObject.addProperty(c.getName(), c.getColor().getRGB());
                    cJsonObject.addProperty(c.getName(), c.getSideColor().getRGB());
                    cJsonObject.addProperty(c.getName(), c.getSideSlider());
                    float[] values = c.getMainSlider();
                    JsonArray sliderArray = new JsonArray();
                    for (float value : values) {
                        sliderArray.add(value);
                    }
                    cJsonObject.add(c.getName(), sliderArray);
                }

                if (c.getMode().equalsIgnoreCase("CheckBox")) {
                    cJsonObject.addProperty(c.getName(), c.isCheckToggled());
                }

                if (c.getMode().equalsIgnoreCase("ModePicker")) {
                    cJsonObject.addProperty(c.getName(), c.getCurrentMode());
                    cJsonObject.addProperty(c.getName(), c.getModeIndex());
                }

                if (c.getMode().equalsIgnoreCase("Slider")) {
                    cJsonObject.addProperty(c.getName(), c.getCurrentNumber());
                }

                if (c.getMode().equalsIgnoreCase("Keybinding")) {
                    cJsonObject.addProperty(c.getName(), c.getKey());
                }
            }
            jsonObject.add("Chat", cJsonObject);

            JsonObject pJsonObject = new JsonObject();

            for (Patcher p : instance.getPatcherManager().getPatcher()) {

                if (p.getMode().equalsIgnoreCase("ColorPicker")) {
                    pJsonObject.addProperty(p.getName(), p.getColor().getRGB());
                    pJsonObject.addProperty(p.getName(), p.getSideColor().getRGB());
                    pJsonObject.addProperty(p.getName(), p.getSideSlider());
                    float[] values = p.getMainSlider();
                    JsonArray sliderArray = new JsonArray();
                    for (float value : values) {
                        sliderArray.add(value);
                    }
                    pJsonObject.add(p.getName(), sliderArray);
                }

                if (p.getMode().equalsIgnoreCase("CheckBox")) {
                    pJsonObject.addProperty(p.getName(), p.isCheckToggled());
                }

                if (p.getMode().equalsIgnoreCase("ModePicker")) {
                    pJsonObject.addProperty(p.getName(), p.getCurrentMode());
                    pJsonObject.addProperty(p.getName(), p.getModeIndex());
                }

                if (p.getMode().equalsIgnoreCase("Slider")) {
                    pJsonObject.addProperty(p.getName(), p.getCurrentNumber());
                }

                if (p.getMode().equalsIgnoreCase("Keybinding")) {
                    pJsonObject.addProperty(p.getName(), p.getKey());
                }
            }

            jsonObject.add("Patcher", pJsonObject);
            gson.toJson(jsonObject, writer);

        } catch (Exception e) {
            ShindoLogger.error("Failed to save profile", e);
        }
    }

    public void save() {
        save(new File(Shindo.getInstance().getFileManager().getProfileDir(), "Default.json"), "", ProfileType.ALL, ProfileIcon.GRASS);
    }

    public void load(File file) {

        Shindo instance = Shindo.getInstance();
        ModManager modManager = instance.getModManager();
        ColorManager colorManager = instance.getColorManager();
        FileManager fileManager = instance.getFileManager();

        if (file == null) {
            return;
        }

        try (FileReader reader = new FileReader(file)) {

            Gson gson = new GsonBuilder().create();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonObject appJsonObject = JsonHelper.getObjectProperty(jsonObject, "Appearance");
            JsonObject modJsonObject = JsonHelper.getObjectProperty(jsonObject, "Mods");
            JsonObject optionJsonObject = JsonHelper.getObjectProperty(jsonObject, "Options");
            JsonObject chatsJsonObject = JsonHelper.getObjectProperty(jsonObject, "Chat");
            JsonObject patchersJsonObject = JsonHelper.getObjectProperty(jsonObject, "Patcher");

            colorManager.setCurrentColor(colorManager.getColorByName(JsonHelper.getStringProperty(appJsonObject, "Accent Color", "Teal Love")));
            colorManager.setTheme(Theme.getThemeById(JsonHelper.getIntProperty(appJsonObject, "Theme", Theme.LIGHT.getId())));
            backgroundManager.setCurrentBackground(backgroundManager.getBackgroundById(JsonHelper.getIntProperty(appJsonObject, "Background", 0)));

            for (Mod m : modManager.getMods()) {

                JsonObject mJsonObject = JsonHelper.getObjectProperty(modJsonObject, m.getName());

                if (mJsonObject != null) {

                    m.setToggled(JsonHelper.getBooleanProperty(mJsonObject, "Toggle", false));

                    if (instance.getHudEditor().getHudMod(m.getName()) != null) {

                        HudMod hMod = instance.getHudEditor().getHudMod(m.getName());

                        hMod.setX(JsonHelper.getIntProperty(mJsonObject, "X", 100));
                        hMod.setY(JsonHelper.getIntProperty(mJsonObject, "Y", 100));
                        hMod.setW(JsonHelper.getIntProperty(mJsonObject, "Width", 100));
                        hMod.setH(JsonHelper.getIntProperty(mJsonObject, "Height", 100));
                        hMod.setSize(JsonHelper.getFloatProperty(mJsonObject, "Size", 1));
                    }

                    if (instance.getSettingManager().getSettingsByMod(m) != null) {

                        JsonObject sJsonObject = JsonHelper.getObjectProperty(mJsonObject, "Settings");

                        if (sJsonObject != null) {

                            for (Setting s : instance.getSettingManager().getSettingsByMod(m)) {

                                if (s.getMode().equalsIgnoreCase("ColorPicker")) {

                                    s.setColor(ColorHelper.getColorByInt(JsonHelper.getIntProperty(sJsonObject, s.getName(), Color.WHITE.getRGB())));
                                    s.setSideColor(ColorHelper.getColorByInt(JsonHelper.getIntProperty(sJsonObject, s.getName(), Color.WHITE.getRGB())));
                                    s.setSideSlider(JsonHelper.getFloatProperty(sJsonObject, s.getName(), 60));
                                    JsonArray sliderArray = sJsonObject.getAsJsonArray(s.getName());

                                    float[] sliderValues = new float[sliderArray.size()];

                                    for (int i = 0; i < sliderArray.size(); i++) {
                                        sliderValues[i] = sliderArray.get(i).getAsFloat();
                                    }

                                    s.setMainSlider(sliderValues);
                                }

                                if (s.getMode().equalsIgnoreCase("CheckBox")) {


                                    s.setCheckToggled(JsonHelper.getBooleanProperty(sJsonObject, s.getName(), false));
                                }

                                if (s.getMode().equalsIgnoreCase("ModePicker")) {

                                    s.setMode(JsonHelper.getStringProperty(sJsonObject, s.getName(), "Modern"));
                                    s.setModeIndex(JsonHelper.getIntProperty(sJsonObject, s.getName(), 0));
                                }

                                if (s.getMode().equalsIgnoreCase("Slider")) {


                                    s.setCurrentNumber(JsonHelper.getFloatProperty(sJsonObject, s.getName(), 1));
                                }

                                if (s.getMode().equalsIgnoreCase("TextBox")) {

                                    s.setText(JsonHelper.getStringProperty(sJsonObject, s.getName(), "NONE"));
                                }

                                if (s.getMode().equalsIgnoreCase("Keybinding")) {


                                    s.setKey(JsonHelper.getIntProperty(sJsonObject, s.getName(), Keyboard.KEY_NONE));
                                }

                                if (s.getMode().equalsIgnoreCase("CellGrid")) {

                                    JsonArray outerArray = sJsonObject.getAsJsonArray(s.getName());
                                    boolean[][] cells = new boolean[outerArray.size()][];

                                    for (int i = 0; i < outerArray.size(); i++) {
                                        JsonArray innerArray = outerArray.get(i).getAsJsonArray();
                                        cells[i] = new boolean[innerArray.size()];

                                        for (int j = 0; j < innerArray.size(); j++) {
                                            cells[i][j] = innerArray.get(j).getAsBoolean();
                                        }
                                    }
                                    s.setCells(cells);
                                }
                            }
                        }
                    }
                }
            }

            for (Option o : instance.getOptionManager().getOptions()) {

                JsonObject oJsonObject = JsonHelper.getObjectProperty(optionJsonObject, "Options");

                if (oJsonObject != null) {

                    if (o.getMode().equalsIgnoreCase("ColorPicker")) {

                        o.setColor(ColorHelper.getColorByInt(JsonHelper.getIntProperty(oJsonObject, o.getName(), Color.WHITE.getRGB())));
                        o.setSideColor(ColorHelper.getColorByInt(JsonHelper.getIntProperty(oJsonObject, o.getName(), Color.WHITE.getRGB())));
                        o.setSideSlider(JsonHelper.getFloatProperty(oJsonObject, o.getName(), 60));
                        JsonArray sliderArray = oJsonObject.getAsJsonArray(o.getName());
                        float[] sliderValues = new float[sliderArray.size()];
                        for (int i = 0; i < sliderArray.size(); i++) {
                            sliderValues[i] = sliderArray.get(i).getAsFloat();
                        }
                        o.setMainSlider(sliderValues);
                    }

                    if (o.getMode().equalsIgnoreCase("CheckBox")) {
                        o.setCheckToggled(JsonHelper.getBooleanProperty(oJsonObject, o.getName(), false));
                    }

                    if (o.getMode().equalsIgnoreCase("ModePicker")) {
                        o.setMode(JsonHelper.getStringProperty(oJsonObject, o.getName(), null));
                        o.setModeIndex(JsonHelper.getIntProperty(oJsonObject, o.getName(), 0));
                    }

                    if (o.getMode().equalsIgnoreCase("Slider")) {
                        o.setCurrentNumber(JsonHelper.getFloatProperty(oJsonObject, o.getName(), 1));
                    }

                    if (o.getMode().equalsIgnoreCase("TextBox")) {
                        o.setText(JsonHelper.getStringProperty(oJsonObject, o.getName(), null));
                    }

                    if (o.getMode().equalsIgnoreCase("Keybinding")) {
                        o.setKey(JsonHelper.getIntProperty(oJsonObject, o.getName(), Keyboard.KEY_NONE));
                    }
                }
            }

            for (Chat c : instance.getChatManager().getChat()) {

                JsonObject cJsonObject = JsonHelper.getObjectProperty(chatsJsonObject, "Chat");

                if (cJsonObject != null) {

                    if (c.getMode().equalsIgnoreCase("ColorPicker")) {
                        c.setColor(ColorHelper.getColorByInt(JsonHelper.getIntProperty(cJsonObject, c.getName(), Color.WHITE.getRGB())));
                        c.setSideColor(ColorHelper.getColorByInt(JsonHelper.getIntProperty(cJsonObject, c.getName(), Color.WHITE.getRGB())));
                        c.setSideSlider(JsonHelper.getFloatProperty(cJsonObject, c.getName(), 60));
                        JsonArray sliderArray = cJsonObject.getAsJsonArray(c.getName());
                        float[] sliderValues = new float[sliderArray.size()];
                        for (int i = 0; i < sliderArray.size(); i++) {
                            sliderValues[i] = sliderArray.get(i).getAsFloat();
                        }
                        c.setMainSlider(sliderValues);
                    }

                    if (c.getMode().equalsIgnoreCase("CheckBox")) {
                        c.setCheckToggled(JsonHelper.getBooleanProperty(cJsonObject, c.getName(), false));
                    }

                    if (c.getMode().equalsIgnoreCase("ModePicker")) {
                        c.setMode(JsonHelper.getStringProperty(cJsonObject, c.getName(), null));
                        c.setModeIndex(JsonHelper.getIntProperty(cJsonObject, c.getName(), 0));
                    }

                    if (c.getMode().equalsIgnoreCase("Slider")) {
                        c.setCurrentNumber(JsonHelper.getFloatProperty(cJsonObject, c.getName(), 1));
                    }

                    if (c.getMode().equalsIgnoreCase("Keybinding")) {
                        c.setKey(JsonHelper.getIntProperty(cJsonObject, c.getName(), Keyboard.KEY_NONE));
                    }
                }
            }

            for (Patcher p : instance.getPatcherManager().getPatcher()) {

                JsonObject pJsonObject = JsonHelper.getObjectProperty(patchersJsonObject, "Patcher");

                if (pJsonObject != null) {
                    if (p.getMode().equalsIgnoreCase("ColorPicker")) {
                        p.setColor(ColorHelper.getColorByInt(JsonHelper.getIntProperty(pJsonObject, p.getName(), Color.WHITE.getRGB())));
                        p.setSideColor(ColorHelper.getColorByInt(JsonHelper.getIntProperty(pJsonObject, p.getName(), Color.WHITE.getRGB())));
                        p.setSideSlider(JsonHelper.getFloatProperty(pJsonObject, p.getName(), 60));
                        JsonArray sliderArray = pJsonObject.getAsJsonArray(p.getName());
                        float[] sliderValues = new float[sliderArray.size()];
                        for (int i = 0; i < sliderArray.size(); i++) {
                            sliderValues[i] = sliderArray.get(i).getAsFloat();
                        }
                        p.setMainSlider(sliderValues);
                    }

                    if (p.getMode().equalsIgnoreCase("CheckBox")) {
                        p.setCheckToggled(JsonHelper.getBooleanProperty(pJsonObject, p.getName(), false));
                    }

                    if (p.getMode().equalsIgnoreCase("ModePicker")) {
                        p.setMode(JsonHelper.getStringProperty(pJsonObject, p.getName(), null));
                        p.setModeIndex(JsonHelper.getIntProperty(pJsonObject, p.getName(), 0));
                    }

                    if (p.getMode().equalsIgnoreCase("Slider")) {
                        p.setCurrentNumber(JsonHelper.getFloatProperty(pJsonObject, p.getName(), 1));
                    }

                    if (p.getMode().equalsIgnoreCase("Keybinding")) {
                        p.setKey(JsonHelper.getIntProperty(pJsonObject, p.getName(), Keyboard.KEY_NONE));
                    }
                }
            }
        } catch (Exception e) {
            ShindoLogger.error("Failed to load profile", e);
        }
    }

    public void delete(Profile profile) {
        profiles.remove(profile);
        profile.getJsonFile().delete();
    }

    public BackgroundManager getBackgroundManager() {
        return backgroundManager;
    }

    public CopyOnWriteArrayList<Profile> getProfiles() {
        return profiles;
    }
}
