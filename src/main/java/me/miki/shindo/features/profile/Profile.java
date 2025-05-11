package me.miki.shindo.features.profile;

import java.io.File;

public class Profile {

    private final File jsonFile;
    private final int id;
    private final String name;
    private String serverIp;
    private ProfileType type;
    private final ProfileIcon icon;

    public Profile(int id, String serverIp, File jsonFile, ProfileIcon icon) {
        this.id = id;
        this.jsonFile = jsonFile;
        this.name = jsonFile != null ? jsonFile.getName().replace(".json", "") : "";
        this.serverIp = serverIp;
        this.icon = icon;
        this.type = ProfileType.ALL;
    }

    public int getId() {
        return id;
    }

    public File getJsonFile() {
        return jsonFile;
    }

    public ProfileIcon getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public ProfileType getType() {
        return type;
    }

    public void setType(ProfileType type) {
        this.type = type;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
