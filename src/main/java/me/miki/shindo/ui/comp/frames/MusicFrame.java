package me.miki.shindo.ui.comp.frames;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.music.Music;
import me.miki.shindo.features.music.MusicManager;
import me.miki.shindo.features.music.MusicType;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;

import java.awt.*;

public class MusicFrame {

    private final String starFilled = "icon/star.png";
    private final String starEmpty = "icon/star_empty.png";

    private int x, y, w, h;
    private Music music;

    public MusicFrame(int x, int y, int w, int h, Music music) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.music = music;
    }

    public void renderFrame(int mouseX, int mouseY, boolean hovered, boolean isFavorite, int themeColor) {

        Helper2D.drawRoundedRectangle(x, y, w, h, 6, themeColor, 0);
        Helper2D.drawRoundedRectangle(x, y + 80, w, 20, 6, Style.getColorTheme(6).getRGB(), 2);
        Helper2D.renderCover(music.getAudio(), x + 2, y + 2, 76);
        Shindo.getInstance().getFontHelper().size20.drawStringLimited(music.getName(), x + 4, y + 85, w - 8, 0xFFFFFF);

        if (hovered) {
            Helper2D.drawRoundedRectangle(x, y, w, h - 20, 6, new Color(255, 255, 255, 50).getRGB(), 1);
        }

        Helper2D.drawPicture(x + w - 14, y + 4, 10, 10, new Color(255, 255, 0).getRGB(), isFavorite ? starFilled : starEmpty);
    }

    public void mouseClicked(int mouseX, int mouseY, MusicManager musicManager) {

        boolean inside = MathHelper.withinBox(x, y, w, h, mouseX, mouseY);
        boolean favorite = MathHelper.withinBox(x + w - 14, y + 4, 10, 10, mouseX, mouseY);

        if (inside && !favorite) {
            musicManager.setCurrentMusic(music);
            musicManager.play();
        }

        if (favorite) {
            if (music.getType().equals(MusicType.ALL)) {
                music.setType(MusicType.FAVORITE);
            } else {
                music.setType(MusicType.ALL);
            }
            musicManager.saveData();
        }
    }

    // Getters and setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }

    public int getH() {
        return h;
    }
}