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

    private TextField nameBox;
    private TextField serverIpBox;

   private TextBox searchBox = new TextBox("Pesquisar Profile", getPanel().getX() + getPanel().getW() - 205, getPanel().getY() + 35, 80, 20);

    private boolean openProfile;

    public ProfileCategory(Panel panel) {
        super(panel);
        setName("Profiles");
        setScrollHelper(scrollHelperProfile);
        setValue(6);
    }

    @Override
    public void initGui() {
        currentType = ProfileType.ALL;
        currentIcon = ProfileIcon.COMMAND;
        openProfile = false;
        nameBox = new TextField(getPanel().getX() + 80 + 32, getPanel().getY() + getPanel().getH()  + 266, 80, 14, "Name");
        serverIpBox = new TextField(getPanel().getX() + 80 + 32, getPanel().getY() + getPanel().getH()  + 284, 80, 14, "Server IP");
        searchBox.setFocused(false);
    }

    @Override
    public void initCategory() {
        currentType = ProfileType.ALL;
        currentIcon = ProfileIcon.COMMAND;
        openProfile = false;
        nameBox = new TextField(getPanel().getX() + 80 + 32, getPanel().getY() + getPanel().getH()  + 266, 80, 14, "Name");
        serverIpBox = new TextField(getPanel().getX() + 80 + 32, getPanel().getY() + getPanel().getH()  + 284, 80, 14, "Server IP");

        searchBox.setFocused(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        Shindo instance  = Shindo.getInstance();
        ProfileManager profileManager = instance.getProfileManager();
        GlyphPageFontRenderer font = instance.getFontHelper().size20;

        nameBox.update();
        serverIpBox.update();
        this.setCanClose(true);



        int i = 0;
        for (ProfileType t : ProfileType.values()) {

            int textWidth = font.getStringWidth(t.getName());
            boolean isCurrentType = t.equals(currentType);

            int x = getPanel().getX() + 130 ;
            int y = getPanel().getY() + 38;
            int w = textWidth + 20;
            int h = 16;

            Helper2D.drawRoundedRectangle(x + i, y, w, h, 6, Style.getColorTheme(isCurrentType ? 8 : 5).getRGB(), 0 );
            font.drawStringWidthShadow(t.getName(), x + 10 + i, y + 2, Style.getColorTheme(19).getRGB());
            i+=textWidth+30;
        }

        int offsetX = 0;
        int offsetY = 13;
        int index = 1;

        offsetX = 0;;

        for(Profile p : profileManager.getProfiles()) {

            if(filter(p)) {
                continue;
            }

            int x = getPanel().getX() + 80 + offsetX ;
            int y = getPanel().getY() + 70 + offsetY;
            Helper2D.drawRoundedRectangle(x, y, 120, 46, 6, Style.getColorTheme(4).getRGB(), 0 );

            if(p.getIcon() != null) {
                Helper2D.drawPicture(x + 6, y + 6, 34, 34, 0, p.getIcon().getIcon());
            }

            if(!p.getName().isEmpty()) {
                font.drawStringLimited(p.getName(), x + 42, y + 9, 70, Style.getColorTheme(19).getRGB());
            }

            if(p.getId() == 999) {
                Helper2D.drawPicture( x + (120 / 2) - 10, y + 13, 20, 20, 0, "icon/plus.png");
            } else {

                boolean isFav = p.getType().equals(ProfileType.FAVORITE);
                if (isFav) {
                    Helper2D.drawPicture(x + 98, y + 2, 10, 10, Color.YELLOW.getRGB(), "icon/star.png");
                } else {
                    Helper2D.drawPicture(x + 98, y + 2, 10, 10, Color.YELLOW.getRGB(), "icon/star_empty.png");
                }


                Helper2D.drawPicture(x + 109, y + 2, 10, 10, 0, "icon/cross.png");
            }

            offsetX+=125;

            if(index % 3 == 0) {
                offsetX = 0;
                offsetY+=56;
            }

            index++;
        }


        offsetY = 15;
        offsetX = 0;


        if (openProfile) {
            Helper2D.drawRectangle(getPanel().getX() + 70, getPanel().getY() + getPanel().getH() + 260, getPanel().getW() - 70, 40, Style.getColorTheme(4).getRGB());
            //font.drawString("Add Profile", getPanel().getX() + 80, getPanel().getY() + getPanel().getH()  + 270, Style.getColorTheme(19).getRGB());
            boolean hovered = MathHelper.withinBox(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 270 - 8, 60, 15, mouseX, mouseY);
            Helper2D.drawRoundedRectangle(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 270 - 8, 60, 15, 3, hovered ? Style.getColorTheme(9).getRGB() : Style.getColorTheme(6).getRGB(), 0);
            Helper2D.drawPicture(getPanel().getX() + 202, getPanel().getY() + getPanel().getH() + 270 - 5, 8, 8, hovered ? new Color(200, 200, 200).getRGB() : 0, "icon/plus.png");
            Shindo.getInstance().getFontHelper().size15.drawStringWidthShadow("Add Icon", getPanel().getX() + 200 + 12, getPanel().getY() + getPanel().getH() + 265, Style.getColorTheme(19).getRGB());

            boolean hovered1 = MathHelper.withinBox(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 280, 60, 15, mouseX, mouseY);
            Helper2D.drawRoundedRectangle(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 280, 60, 15, 3, hovered1 ? Style.getColorTheme(9).getRGB() : Style.getColorTheme(6).getRGB(), 0);
            Helper2D.drawPicture(getPanel().getX() + 202 , getPanel().getY() + getPanel().getH() + 284, 8, 8, hovered ? new Color(200, 200, 200).getRGB() : 0, "icon/plus.png");
            Shindo.getInstance().getFontHelper().size15.drawStringWidthShadow("Add Profile", getPanel().getX() + 200 + 12, getPanel().getY() + getPanel().getH() + 284, Style.getColorTheme(19).getRGB());


            font.drawString("Name", getPanel().getX() + 80, getPanel().getY() + getPanel().getH() + 269, Style.getColorTheme(19).getRGB());

            nameBox.render(mouseX, mouseY);


            font.drawString("Server", getPanel().getX() + 80, getPanel().getY() + getPanel().getH() + 287, Style.getColorTheme(19).getRGB());
            serverIpBox.render(mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        Shindo instance = Shindo.getInstance();
        ProfileManager profileManager = instance.getProfileManager();
        ModManager modManager = instance.getModManager();
        FileManager fileManager = instance.getFileManager();

        if (openProfile) {

            for (ProfileIcon icon : ProfileIcon.values()) {
                if (MathHelper.withinBox(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 270 - 8, 60, 15, mouseX, mouseY)) {

                }
            }
            nameBox.onClick(mouseX, mouseY, mouseButton);
            serverIpBox.onClick(mouseX, mouseY, mouseButton);

            if (MathHelper.withinBox(getPanel().getX() + 200 - 2, getPanel().getY() + getPanel().getH() + 280, 60, 15, mouseX, mouseY)) {

                if (!nameBox.getText().isEmpty()) {

                    String serverIp = "";

                    if (!serverIpBox.getText().isEmpty()) {
                        serverIp = serverIpBox.getText();
                    }

                    profileManager.save(new File(fileManager.getProfileDir(), nameBox.getText() + ".json"), serverIp, ProfileType.ALL, currentIcon);
                    profileManager.loadProfiles(false);

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

                if (mouseButton == 0) {

                    boolean favorite = MathHelper.withinBox(x + 98, y + 2, 10, 10, mouseX, mouseY);
                    boolean delete = MathHelper.withinBox(x + 109, y + 2, 10, 10, mouseX, mouseY);
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

                offsetX += 100;

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

        if(currentType.equals(ProfileType.FAVORITE) && !p.getType().equals(ProfileType.FAVORITE)) {
            return true;
        }

       if(!this.searchBox.getText().isEmpty() && !(p.getName().toLowerCase().contains(this.searchBox.getText().toLowerCase()))) {
           return true;
       }

        return false;
    }
}