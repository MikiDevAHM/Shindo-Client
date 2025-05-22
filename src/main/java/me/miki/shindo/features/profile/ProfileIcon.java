package me.miki.shindo.features.profile;

import me.miki.shindo.helpers.animation.simple.SimpleAnimation;

public enum ProfileIcon {
    COMMAND(0, "command"), CRAFTING_TABLE(1, "crafting_table"), FURNACE(2, "furnace"), GRASS(3, "grass"),
    HAY(4, "hay"), PUMPKIN(5, "pumpkin"), TNT(6, "tnt");

    private SimpleAnimation animation = new SimpleAnimation();

    private final int id;
    private final String icon;

    ProfileIcon(int id, String name) {
        this.id = id;
        this.icon = "icon/profile/" + name + ".png";
    }

    public static ProfileIcon getIconById(int id) {

        for (ProfileIcon pi : ProfileIcon.values()) {
            if (pi.getId() == id) {
                return pi;
            }
        }

        return ProfileIcon.GRASS;
    }

    public SimpleAnimation getAnimation() {
        return animation;
    }

    public String getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }
}
