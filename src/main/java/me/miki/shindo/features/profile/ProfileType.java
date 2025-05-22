package me.miki.shindo.features.profile;

public enum ProfileType {
    ALL(0, "ALL"), FAVORITE(1, "FAVORITE");

    private final int id;
    private final String name;

    ProfileType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ProfileType getTypeById(int id) {

        for (ProfileType type : ProfileType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        return ProfileType.ALL;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
