package me.miki.shindo.ui.modmenu.category.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.music.Music;
import me.miki.shindo.features.music.MusicManager;
import me.miki.shindo.features.music.MusicType;
import me.miki.shindo.features.notification.NotificationType;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.font.GlyphPageFontRenderer;
import me.miki.shindo.helpers.hud.PositionHelper;
import me.miki.shindo.helpers.hud.ScrollHelper;
import me.miki.shindo.helpers.multithreading.Multithreading;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.comp.TextField;
import me.miki.shindo.ui.comp.frames.MusicFrame;
import me.miki.shindo.ui.modmenu.category.Category;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.TextBox;

import java.util.ArrayList;
import java.util.List;

public class MusicCategory extends Category {

    private final ScrollHelper scrollHelperMusic = new ScrollHelper(0, 300, 35, 300);
    private final PositionHelper posHelper = new PositionHelper(125);
    List<MusicFrame> musicFrames = new ArrayList<>();
    private final TextBox searchBox = new TextBox("Pesquisar Musica", getPanel().getX() + getPanel().getW() - 205, getPanel().getY() + 35, 200, 18);
    private TextField musicDownloader;
    private boolean drag;

    private MusicType currentType;


    public MusicCategory(Panel panel) {
        super(panel);
        setName("Music");
        setIcon("icon/button/sidebar/music.png");
        setScrollHelper(scrollHelperMusic);
        setValue(5);
    }

    @Override
    public void initGui() {
        currentType = MusicType.ALL;

        musicDownloader = new TextField(getPanel().getX() + getPanel().getW() - 215, getPanel().getY() + getPanel().getH() + 280, 170, 14, "URL");
        searchBox.setFocused(false);
    }


    @Override
    public void initCategory() {
        currentType = MusicType.ALL;

        musicDownloader = new TextField(getPanel().getX() + getPanel().getW() - 215, getPanel().getY() + getPanel().getH() + 280, 170, 14, "URL");
        searchBox.setFocused(false);
    }


    @Override
    public void drawScreen(int mouseX, int mouseY) {

        searchBox.renderTextBox(getPanel().getX() + getPanel().getW() - 205, getPanel().getY() + 35, mouseX, mouseY);

        Shindo instance = Shindo.getInstance();
        MusicManager musicManager = instance.getMusicManager();

        musicDownloader.update();

        int offsetX = 0;
        int offsetY = 13;

        int alignmentX = 120;
        int alignmentY = 38;

        for (MusicType type : MusicType.values()) {

            GlyphPageFontRenderer fr = Shindo.getInstance().getFontHelper().size20;
            int textWidth = fr.getStringWidth(type.getName());
            boolean isCurrentType = type.equals(currentType);
            Helper2D.drawRoundedRectangle(getPanel().getX() + 15 + offsetX + alignmentX, getPanel().getY() + alignmentY, textWidth + 6, fr.getFontHeight() + 4, 2, Style.getColorTheme(isCurrentType ? 8 : 5).getRGB(), 0);
            fr.drawString(type.getName(), getPanel().getX() + 15 + offsetX + 4 + alignmentX, getPanel().getY() + 4 + alignmentY, -1);
            offsetX += textWidth + 10;
        }

        offsetY = offsetY + 23;

        int boxesPerRow = 5;
        int boxW = 80;
        int boxH = 100;
        int padding = 10;

        int totalHeight = 0;
        int count = 0;

        // Conta quantas linhas serão renderizadas
        for (Music music : musicManager.getMusics()) {
            if (filter(music)) continue;
            if (count % boxesPerRow == 0) {
                totalHeight += boxH + padding;
            }
            count++;
        }

        scrollHelperMusic.setHeight(totalHeight + 55);
        scrollHelperMusic.updateScroll();


        int i = 0;
        int row = 0;
        int col = 0;

        musicFrames.clear();

        GLHelper.startScissor(getPanel().getX(), getPanel().getY() + 60, getPanel().getW(), getPanel().getH() + 240);
        for (Music music : musicManager.getMusics()) {
            if (filter(music)) continue;

            col = i % boxesPerRow;
            row = i / boxesPerRow;

            int x = getPanel().getX() + 80 + col * (boxW + padding);
            int y = getPanel().getY() + 70 + row * (boxH + padding);

            y += (int) scrollHelperMusic.getCalculatedScroll(); // <-- aplica o scroll

            MusicFrame frame = new MusicFrame(x - 4, y, boxW, boxH, music);
            boolean hovered = MathHelper.withinBox(x - 4, y, boxW, boxH, mouseX, mouseY);


            frame.renderFrame(mouseX, mouseY, hovered,
                    music.getType().equals(MusicType.FAVORITE),
                    Style.getColorTheme(4).getRGB());

            musicFrames.add(frame);

            i++;
        }
        GLHelper.endScissor();

        // Posição e tamanho da área de exibição
        int viewX = getPanel().getX() + 80;
        int viewY = getPanel().getY() + 70;
        int viewHeight = getPanel().getH() + 180;

        int scrollBarX = getPanel().getX() + getPanel().getW() - 10;
        int scrollBarWidth = 6;

        // Só desenha barra se conteúdo maior que a área visível
        if (scrollHelperMusic.getHeight() > scrollHelperMusic.getMaxScroll()) {

            // Desenhar trilho (fundo)
            Helper2D.drawRoundedRectangle(
                    scrollBarX, viewY, scrollBarWidth, viewHeight,
                    1, Style.getColorTheme(4).getRGB(), 0
            );

            // Calcular proporção da "thumb"
            float proportion = scrollHelperMusic.getMaxScroll() / scrollHelperMusic.getHeight();
            int thumbHeight = (int) (viewHeight * proportion);
            thumbHeight = Math.max(20, thumbHeight); // tamanho mínimo

            // Calcular posição do "thumb"
            float maxScrollPixels = scrollHelperMusic.getHeight() - scrollHelperMusic.getMaxScroll();
            float scrollY = scrollHelperMusic.getCalculatedScroll();
            float thumbY = viewY - (scrollY / maxScrollPixels) * (viewHeight - thumbHeight);

            // Desenhar thumb
            Helper2D.drawRoundedRectangle(
                    scrollBarX + 1, (int) thumbY, scrollBarWidth - 1, thumbHeight - 12,
                    1, Style.getColorTheme(19).getRGB(), 0
            );
        }


        Helper2D.drawRectangle(getPanel().getX() + 70, getPanel().getY() + getPanel().getH() + 260, getPanel().getW() - 70, 40, Style.getColorTheme(3).getRGB());

        // Previous
        Helper2D.drawRoundedRectangle(getPanel().getX() + 75, getPanel().getY() + getPanel().getH() + 280, 10, 10, 4, 0xFF555555, 0);
        Helper2D.drawPicture(getPanel().getX() + 75 + 2, getPanel().getY() + getPanel().getH() + 280 + 2, 6, 6, 0, "icon/previous.png");

        // Play/pause button
        Helper2D.drawRoundedRectangle(getPanel().getX() + 75 + 15, getPanel().getY() + getPanel().getH() + 280, 10, 10, 4, 0xFF555555, 0);
        Helper2D.drawPicture(getPanel().getX() + 75 + 2 + 15, getPanel().getY() + getPanel().getH() + 280 + 2, 6, 6, 0, musicManager.isPlaying() ? "icon/pause.png" : "icon/play.png");

        // Next
        Helper2D.drawRoundedRectangle(getPanel().getX() + 75 + 30, getPanel().getY() + getPanel().getH() + 280, 10, 10, 4, 0xFF555555, 0);
        Helper2D.drawPicture(getPanel().getX() + 75 + 2 + 30, getPanel().getY() + getPanel().getH() + 280 + 2, 6, 6, 0, "icon/next.png");

        // Volume slider background
        Helper2D.drawRoundedRectangle(getPanel().getX() + 75 + 45, getPanel().getY() + getPanel().getH() + 280 + 2, 100, 6, 3, 0xFF444444, 0);

        int knobX = (int) Shindo.getInstance().getMusicManager().getVolume();
        int sliderWidth = 100;

        posHelper.pre(knobX);

        if (drag) {
            knobX = mouseX - (getPanel().getX() + 75 + 45);
            if (knobX < 0) {
                knobX = 0;
            } else if (knobX > sliderWidth) {
                knobX = sliderWidth;
            }
            musicManager.setVolume(knobX);
            musicManager.saveData();
        }

        posHelper.post(knobX);
        posHelper.update();

        Helper2D.drawRoundedRectangle(getPanel().getX() + 75 + 45, getPanel().getY() + getPanel().getH() + 280 + 2, knobX, 6, 2, 0xFFFFFFFF, 0);
        Shindo.getInstance().getFontHelper().size20.drawString("Volume: " + MathHelper.round(knobX, 1), getPanel().getX() + 75 + 150, getPanel().getY() + getPanel().getH() + 280 + 2, -1);

        musicDownloader.render(mouseX, mouseY);

        boolean hovered = MathHelper.withinBox(getPanel().getX() + getPanel().getW() - 30 - 5, getPanel().getY() + getPanel().getH() + 280, 15, 15, mouseX, mouseY);
        Helper2D.drawRectangle(getPanel().getX() + getPanel().getW() - 30 - 5, getPanel().getY() + getPanel().getH() + 280, 15, 15, Style.getColorTheme(hovered ? 9 : 6).getRGB());
        Helper2D.drawPicture(getPanel().getX() + getPanel().getW() - 30 + 2 - 5, getPanel().getY() + getPanel().getH() + 280 + 2, 11, 11, 0, "icon/plus.png");
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        Shindo instance = Shindo.getInstance();
        MusicManager musicManager = instance.getMusicManager();
        GlyphPageFontRenderer fr = Shindo.getInstance().getFontHelper().size20;

        int offsetX = 0;
        int offsetY = 13;
        int alignmentX = 120;
        int alignmentY = 38;

        for (MusicType t : MusicType.values()) {


            int textWidth = fr.getStringWidth(t.getName());
            if (MathHelper.withinBox(getPanel().getX() + 15 + offsetX + alignmentX, getPanel().getY() + alignmentY, textWidth + 6, fr.getFontHeight() + 4, mouseX, mouseY) && mouseButton == 0) {
                currentType = t;
            }

            offsetX += textWidth + 10;
        }
        offsetY = offsetY + 23;


        int startX = getPanel().getX() + 80;
        int startY = getPanel().getY() + 70;
        int i = 0;

        for (MusicFrame frame : musicFrames) {
            if (MathHelper.withinBox(getPanel().getX() + 70, getPanel().getY() + 60, getPanel().getW() - 70, 200, mouseX, mouseY)) {
                frame.mouseClicked(mouseX, mouseY, musicManager);
            }
        }
        // Play/Pause
        if (MathHelper.withinBox(getPanel().getX() + 75 + 15, getPanel().getY() + getPanel().getH() + 280, 10, 10, mouseX, mouseY)) {
            musicManager.switchPlayBack();
        }

        // Back
        if (MathHelper.withinBox(getPanel().getX() + 75, getPanel().getY() + getPanel().getH() + 280, 10, 10, mouseX, mouseY)) {
            musicManager.back();
        }

        // Next
        if (MathHelper.withinBox(getPanel().getX() + 75 + 30, getPanel().getY() + getPanel().getH() + 280, 10, 10, mouseX, mouseY)) {
            musicManager.next();
        }

        // Volume slider (simple horizontal area)
        if (MathHelper.withinBox(getPanel().getX() + 75 + 45, getPanel().getY() + getPanel().getH() + 280 + 2, 100, 6, mouseX, mouseY)) {
            drag = true;
        }

        if (MathHelper.withinBox(getPanel().getX() + getPanel().getW() - 30 - 5, getPanel().getY() + getPanel().getH() + 280, 15, 15, mouseX, mouseY)) {
            Multithreading.runAsync(() -> {

                String music = musicDownloader.getText();
                instance.getNotificationManager().post(music, "Adicionada A Fila de Download", NotificationType.INFO);

                if (musicManager.getYtdlp().download(music)) {
                    instance.getNotificationManager().post(music, "Download Concluido Com Successo", NotificationType.SUCCESS);
                    musicManager.load();
                } else {
                    instance.getNotificationManager().post(music, "Falha no Download", NotificationType.ERROR);
                }
            });
        }


        searchBox.setFocused(MathHelper.withinBox(getPanel().getX() + getPanel().getW() - 205, getPanel().getY() + 35, 200, 18, mouseX, mouseY));

        musicDownloader.onClick(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        drag = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        searchBox.keyTyped(typedChar, keyCode);

        musicDownloader.keyTyped(typedChar, keyCode);
    }

    public boolean filter(Music m) {

        if (!currentType.equals(MusicType.ALL) && !m.getType().equals(currentType)) {
            return true;
        }


        return !this.searchBox.getText().isEmpty() && !(m.getName().toLowerCase().contains(this.searchBox.getText().toLowerCase()));
    }


}
