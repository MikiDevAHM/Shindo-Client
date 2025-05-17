package me.miki.shindo.ui.modmenu.category.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.mods.ModManager;
import me.miki.shindo.features.profile.Profile;
import me.miki.shindo.features.profile.ProfileIcon;
import me.miki.shindo.features.profile.ProfileManager;
import me.miki.shindo.features.profile.ProfileType;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.file.FileManager;
import me.miki.shindo.helpers.font.GlyphPageFontRenderer;
import me.miki.shindo.helpers.hud.ScrollHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.comp.TextField;
import me.miki.shindo.ui.modmenu.category.Category;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.TextBox;

import java.awt.*;
import java.io.File;

public class ProfileCategory extends Category {

    private final ScrollHelper scrollHelperProfile = new ScrollHelper(0, 300, 35, 300);

    private ProfileType currentType;
    private ProfileIcon currentIcon;

    private TextField nameBox = new TextField();
    private TextField serverIpBox = new TextField();

    private final TextBox searchBox = new TextBox("Pesquisar Profile", getPanel().getX() + getPanel().getW() - 205, getPanel().getY() + 35, 80, 20);

    private boolean openProfile;
    private boolean openIcon;

    public ProfileCategory(Panel panel) {
        super(panel);
        setName("Profiles");
        setIcon("icon/button/sidebar/profiles.png");
        setScrollHelper(scrollHelperProfile);
        setValue(6);
    }

    @Override
    public void initGui() {
        currentType = ProfileType.ALL;
        currentIcon = ProfileIcon.COMMAND;
        openProfile = false;
        openIcon = false;
        searchBox.setFocused(false);
    }

    @Override
    public void initCategory() {
        currentType = ProfileType.ALL;
        currentIcon = ProfileIcon.COMMAND;
        openProfile = false;
        openIcon = false;


        searchBox.setFocused(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        Shindo instance = Shindo.getInstance();
        ProfileManager profileManager = instance.getProfileManager();
        GlyphPageFontRenderer font = instance.getFontHelper().size20;

        nameBox.update();
        serverIpBox.update();
        this.setCanClose(true);


        int i = 0;
        for (ProfileType t : ProfileType.values()) {

            int textWidth = font.getStringWidth(t.getName());
            boolean isCurrentType = t.equals(currentType);

            int x = getPanel().getX() + 130;
            int y = getPanel().getY() + 38;
            int w = textWidth + 20;
            int h = 16;

            Helper2D.drawRoundedRectangle(x + i, y, w, h, 6, Style.getColorTheme(isCurrentType ? 8 : 5).getRGB(), 0);
            font.drawStringWidthShadow(t.getName(), x + 10 + i, y + 2, Style.getColorTheme(19).getRGB());
            i += textWidth + 30;
        }

        int offsetX = 0;
        int offsetY = 13;
        int index = 1;

        offsetX = 0;

        for (Profile p : profileManager.getProfiles()) {

            if (filter(p)) {
                continue;
            }

            int x = getPanel().getX() + 80 + offsetX;
            int y = getPanel().getY() + 70 + offsetY;
            boolean hovered = MathHelper.withinBox(x, y, 120, 46, mouseX, mouseY);
            Helper2D.drawRoundedRectangle(x, y, 140, 46, 6, Style.getColorTheme(hovered ? 8 : 5).getRGB(), 0);

            if (p.getIcon() != null) {
                Helper2D.drawPicture(x + 6, y + 6, 34, 34, 0, p.getIcon().getIcon());
            }

            if (!p.getName().isEmpty()) {
                font.drawStringLimited(p.getName(), x + 42, y + 9, 70, Style.getColorTheme(19).getRGB());
            }


            if (p.getId() == 999) {
                Helper2D.drawPicture(x + (140 / 2) - 10, y + 13, 20, 20, 0, "icon/plus.png");
            } else {

                if (!p.getName().equals("Default")) {

                    boolean isFav = p.getType().equals(ProfileType.FAVORITE);
                    if (isFav) {
                        Helper2D.drawPicture(x + 118, y + 2, 10, 10, Color.YELLOW.getRGB(), "icon/star.png");
                    } else {
                        Helper2D.drawPicture(x + 118, y + 2, 10, 10, Color.YELLOW.getRGB(), "icon/star_empty.png");
                    }


                    Helper2D.drawPicture(x + 129, y + 2, 10, 10, 0, "icon/cross.png");
                }
            }

            offsetX += 150;

            if (index % 3 == 0) {
                offsetX = 0;
                offsetY += 56;
            }

            index++;
        }


        if (openProfile) {
            Helper2D.drawRectangle(getPanel().getX() + 70, getPanel().getY() + getPanel().getH() + 260, getPanel().getW() - 70, 40, Style.getColorTheme(4).getRGB());
            //font.drawString("Add Profile", getPanel().getX() + 80, getPanel().getY() + getPanel().getH()  + 270, Style.getColorTheme(19).getRGB());
            boolean hovered = MathHelper.withinBox(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 270 - 5, 60, 15, mouseX, mouseY);
            Helper2D.drawRoundedRectangle(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 270 - 5, 60, 15, 3, hovered ? Style.getColorTheme(9).getRGB() : Style.getColorTheme(6).getRGB(), 0);
            Helper2D.drawPicture(getPanel().getX() + 202, getPanel().getY() + getPanel().getH() + 270 - 2, 8, 8, hovered ? new Color(200, 200, 200).getRGB() : 0, "icon/plus.png");
            Shindo.getInstance().getFontHelper().size15.drawStringWidthShadow("Add Icon", getPanel().getX() + 200 + 12, getPanel().getY() + getPanel().getH() + 265 + 3, Style.getColorTheme(19).getRGB());

            boolean hovered1 = MathHelper.withinBox(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 280 + 3, 60, 15, mouseX, mouseY);
            Helper2D.drawRoundedRectangle(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 280 + 3, 60, 15, 3, hovered1 ? Style.getColorTheme(9).getRGB() : Style.getColorTheme(6).getRGB(), 0);
            Helper2D.drawPicture(getPanel().getX() + 202, getPanel().getY() + getPanel().getH() + 284 + 3, 8, 8, hovered ? new Color(200, 200, 200).getRGB() : 0, "icon/plus.png");
            Shindo.getInstance().getFontHelper().size15.drawStringWidthShadow("Add Profile", getPanel().getX() + 200 + 12, getPanel().getY() + getPanel().getH() + 284 + 3, Style.getColorTheme(19).getRGB());


            font.drawString("Name", getPanel().getX() + 80, getPanel().getY() + getPanel().getH() + 269, Style.getColorTheme(19).getRGB());

            nameBox.setPosition(getPanel().getX() + 80 + 32, getPanel().getY() + getPanel().getH() + 266, 80, 14, "Name");
            nameBox.render(mouseX, mouseY);


            font.drawString("Server", getPanel().getX() + 80, getPanel().getY() + getPanel().getH() + 287, Style.getColorTheme(19).getRGB());
            serverIpBox.setPosition(getPanel().getX() + 80 + 32, getPanel().getY() + getPanel().getH() + 284, 80, 14, "Server IP");
            serverIpBox.render(mouseX, mouseY);

            offsetY = 15;
            offsetX = 0;
            if (openIcon) {
                for (ProfileIcon icon : ProfileIcon.values()) {


                    int x = getPanel().getX() + 275 + offsetX;
                    int y = getPanel().getY() + getPanel().getH() + 269;
                    boolean hovered2 = currentIcon.equals(icon);
                    Helper2D.drawPicture(x, y, 25, 25, 0, icon.getIcon());
                    Helper2D.drawOutlinedRectangle(x, y, 25, 25, 2, Style.getColorTheme(hovered2 ? 9 : 6).getRGB());

                    offsetX += 30;
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        Shindo instance = Shindo.getInstance();
        ProfileManager profileManager = instance.getProfileManager();
        ModManager modManager = instance.getModManager();
        FileManager fileManager = instance.getFileManager();

        int offsetX = 0;
        int offsetY = 13;
        int index = 1;

        if (openProfile) {

            offsetY = 15;
            offsetX = 0;
            if (MathHelper.withinBox(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 270 - 5, 60, 15, mouseX, mouseY)) {
                openIcon = true;
            }

            if (openIcon) {

                for (ProfileIcon icon : ProfileIcon.values()) {
                    if (MathHelper.withinBox(getPanel().getX() + 275 + offsetX, getPanel().getY() + getPanel().getH() + 269, 25, 25, mouseX, mouseY)) {
                        currentIcon = icon;
                    }

                    offsetX += 30;
                }
            }

            nameBox.onClick(mouseX, mouseY, mouseButton);
            serverIpBox.onClick(mouseX, mouseY, mouseButton);

            if (MathHelper.withinBox(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 280 + 3, 60, 15, mouseX, mouseY)) {

                if (!nameBox.getText().isEmpty()) {

                    String serverIp = "";

                    if (!serverIpBox.getText().isEmpty()) {
                        serverIp = serverIpBox.getText();
                    }

                    profileManager.save(new File(fileManager.getProfileDir(), nameBox.getText() + ".json"), serverIp, ProfileType.ALL, currentIcon);
                    profileManager.loadProfiles();
                    openProfile = false;
                    openIcon = false;
                }
            }
        } else {

            int i = 0;
            for (ProfileType t : ProfileType.values()) {

                int textWidth = Shindo.getInstance().getFontHelper().size20.getStringWidth(t.getName());

                int x = getPanel().getX() + 130;
                int y = getPanel().getY() + 38;
                int w = textWidth + 20;
                int h = 16;

                if (MathHelper.withinBox(x + i, y, w, h, mouseX, mouseY) && mouseButton == 0) {
                    currentType = t;
                }

                i += textWidth + 30;
            }


            offsetX = 0;

            for (Profile p : profileManager.getProfiles()) {

                if (filter(p)) {
                    continue;
                }

                int x = getPanel().getX() + 80 + offsetX;
                int y = getPanel().getY() + 70 + offsetY;

                if (mouseButton == 0) {

                    boolean favorite = MathHelper.withinBox(x + 118, y + 2, 10, 10, mouseX, mouseY);
                    boolean delete = MathHelper.withinBox(x + 129, y + 2, 10, 10, mouseX, mouseY);
                    boolean inside = MathHelper.withinBox(x, y, 120, 46, mouseX, mouseY);

                    if (inside) {

                        if (p.getId() == 999 && inside) {
                            openProfile = true;
                            this.setCanClose(false);
                        } else if (!favorite && !delete) {
                            modManager.disableAll();
                            profileManager.load(p.getJsonFile());
                        }
                    }

                    if (p.getId() != 999) {
                        if (!p.getName().equals("Default")) {
                            if (favorite) {

                                if (p.getType().equals(ProfileType.FAVORITE)) {
                                    p.setType(ProfileType.ALL);
                                } else {
                                    p.setType(ProfileType.FAVORITE);
                                }

                                profileManager.save(p.getJsonFile(), p.getServerIp(), p.getType(), p.getIcon());
                            }

                            if (delete) {
                                profileManager.delete(p);
                            }
                        }
                    }
                }

                offsetX += 150;

                if (index % 3 == 0) {
                    offsetX = 0;
                    offsetY += 56;
                }

                index++;
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

        nameBox.keyTyped(typedChar, keyCode);
        serverIpBox.keyTyped(typedChar, keyCode);
    }

    private boolean filter(Profile p) {

        if (currentType.equals(ProfileType.FAVORITE) && !p.getType().equals(ProfileType.FAVORITE)) {
            return true;
        }

        return !this.searchBox.getText().isEmpty() && !(p.getName().toLowerCase().contains(this.searchBox.getText().toLowerCase()));
    }
}