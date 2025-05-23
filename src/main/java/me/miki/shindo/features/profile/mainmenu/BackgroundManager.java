package me.miki.shindo.features.profile.mainmenu;

import com.google.gson.*;
import me.miki.shindo.Shindo;
import me.miki.shindo.features.profile.mainmenu.impl.Background;
import me.miki.shindo.features.profile.mainmenu.impl.CustomBackground;
import me.miki.shindo.features.profile.mainmenu.impl.DefaultBackground;
import me.miki.shindo.helpers.file.FileHelper;
import me.miki.shindo.features.file.FileManager;
import me.miki.shindo.helpers.logger.ShindoLogger;
import me.miki.shindo.helpers.network.JsonHelper;
import me.miki.shindo.helpers.render.Helper2D;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class BackgroundManager {

    private final CopyOnWriteArrayList<Background> backgrounds = new CopyOnWriteArrayList<Background>();
    private final CopyOnWriteArrayList<CustomBackground> removeBackgrounds = new CopyOnWriteArrayList<CustomBackground>();
    private Background currentBackground;

    public BackgroundManager() {

        FileManager fileManager = Shindo.getInstance().getFileManager();
        File bgCacheDir = new File(fileManager.getCacheDir(), "background");
        File dataJson = new File(bgCacheDir, "Data.json");

        if (!bgCacheDir.exists()) {
            fileManager.createDir(bgCacheDir);
        }

        if (!dataJson.exists()) {
            fileManager.createFile(dataJson);
        }

        backgrounds.add(new DefaultBackground(0, "Fox", new ResourceLocation("shindo/mainmenu/background.png")));
        backgrounds.add(new DefaultBackground(1, "Night", new ResourceLocation("shindo/mainmenu/background-night.png")));
        backgrounds.add(new DefaultBackground(2, "Dolphin", new ResourceLocation("shindo/mainmenu/background-dolphin.png")));
        backgrounds.add(new DefaultBackground(999, "Add", null));

        ArrayList<String> removeImages = load();

        for (File f : bgCacheDir.listFiles()) {
            if (FileHelper.getExtension(f).equals("png")) {
                if (!removeImages.isEmpty() && removeImages.contains(f.getName())) {
                    f.delete();
                } else {
                    addCustomBackground(f);
                }
            }
        }

        currentBackground = getBackgroundById(0);
    }

    public ArrayList<String> load() {

        FileManager fileManager = Shindo.getInstance().getFileManager();
        File bgCacheDir = new File(fileManager.getCacheDir(), "background");
        File dataJson = new File(bgCacheDir, "Data.json");
        ArrayList<String> output = new ArrayList<String>();

        try (FileReader reader = new FileReader(dataJson)) {

            Gson gson = new GsonBuilder()
                    .create();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            if (jsonObject != null) {

                JsonArray jsonArray = JsonHelper.getArrayProperty(jsonObject, "Remove Images");

                if (jsonArray != null) {

                    for (JsonElement jsonElement : jsonArray) {

                        JsonObject rJsonObject = gson.fromJson(jsonElement, JsonObject.class);

                        output.add(JsonHelper.getStringProperty(rJsonObject, "Image", "null"));
                    }
                }
            }
        } catch (Exception e) {
            ShindoLogger.error("An error occurred while loading backgrounds.", e);
        }

        return output;
    }

    public void save() {

        FileManager fileManager = Shindo.getInstance().getFileManager();
        File bgCacheDir = new File(fileManager.getCacheDir(), "background");
        File dataJson = new File(bgCacheDir, "Data.json");

        try (FileWriter writer = new FileWriter(dataJson)) {

            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            for (CustomBackground bg : removeBackgrounds) {

                JsonObject innerJsonObject = new JsonObject();

                innerJsonObject.addProperty("Image", bg.getImage().getName());

                jsonArray.add(innerJsonObject);
            }

            jsonObject.add("Remove Images", jsonArray);

            gson.toJson(jsonObject, writer);

        } catch (Exception e) {
            ShindoLogger.error("An error occurred while saving backgrounds.", e);
        }
    }

    public CopyOnWriteArrayList<Background> getBackgrounds() {
        return backgrounds;
    }

    public Background getBackgroundById(int id) {

        for (Background bg : backgrounds) {
            if (bg.getId() == id) {
                return bg;
            }
        }

        return getBackgroundById(0);
    }

    public int getMaxId() {

        int maxId = 0;

        for (Background bg : backgrounds) {
            if (bg.getId() != 999 && bg.getId() > maxId) {
                maxId = bg.getId();
            }
        }

        return maxId;
    }

    public void addCustomBackground(File image) {

        int maxId = getMaxId();
        int index = backgrounds.indexOf(getBackgroundById(999));

        backgrounds.add(index, new CustomBackground(maxId + 1, image.getName().replace(".png", ""), image));
    }

    public void removeCustomBackground(CustomBackground cusBackground) {


        Helper2D.unregisterImage(cusBackground.getImage());
        backgrounds.remove(cusBackground);
        removeBackgrounds.add(cusBackground);

        save();
    }

    public Background getCurrentBackground() {
        return currentBackground;
    }

    public void setCurrentBackground(Background currentBackground) {
        this.currentBackground = currentBackground;
    }
}
