package me.miki.shindo.management.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.management.color.AccentColor;
import me.miki.shindo.management.event.EventTarget;
import me.miki.shindo.management.event.impl.EventRender2D;
import me.miki.shindo.management.event.impl.EventRenderVisualizer;
import me.miki.shindo.management.event.impl.EventUpdate;
import me.miki.shindo.management.language.TranslateText;
import me.miki.shindo.management.mods.SimpleHUDMod;
import me.miki.shindo.management.mods.settings.impl.BooleanSetting;
import me.miki.shindo.management.mods.settings.impl.ComboSetting;
import me.miki.shindo.management.mods.settings.impl.combo.Option;
import me.miki.shindo.management.music.MusicManager;
import me.miki.shindo.management.music.MusicWaveform;
import me.miki.shindo.management.nanovg.NanoVGManager;
import me.miki.shindo.management.nanovg.font.Fonts;
import me.miki.shindo.management.nanovg.font.LegacyIcon;
import me.miki.shindo.utils.ColorUtils;
import me.miki.shindo.utils.TimerUtils;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.Arrays;

public class MusicInfoMod extends SimpleHUDMod {

    private static MusicInfoMod instance;

    private TimerUtils timer = new TimerUtils();
    private TimerUtils timer2 = new TimerUtils();

    private float addX;
    private boolean back;

    private BooleanSetting iconSetting = new BooleanSetting(TranslateText.ICON, this, true);

    private ComboSetting designSetting = new ComboSetting(TranslateText.DESIGN, this, TranslateText.OVERLAY, new ArrayList<Option>(Arrays.asList(
            new Option(TranslateText.SIMPLE), new Option(TranslateText.OVERLAY), new Option(TranslateText.WAVEFORM))));

    public MusicInfoMod() {
        super(TranslateText.MUSIC_INFO, TranslateText.MUSIC_INFO_DESCRIPTION);

        instance = this;
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {

        NanoVGManager nvg = Shindo.getInstance().getNanoVGManager();
        Option option = designSetting.getOption();

        if(option.getTranslate().equals(TranslateText.SIMPLE)) {
            this.draw();
        } else if(option.getTranslate().equals(TranslateText.WAVEFORM)) {
            nvg.setupAndDraw(() -> drawWaveformNanoVG());
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        Option option = designSetting.getOption();

        this.setDraggable(!option.getTranslate().equals(TranslateText.OVERLAY));
    }

    @EventTarget
    public void onRenderVisualizer(EventRenderVisualizer event) {

        Option option = designSetting.getOption();

        if(option.getTranslate().equals(TranslateText.OVERLAY)) {

            NanoVGManager nvg = Shindo.getInstance().getNanoVGManager();

            nvg.setupAndDraw(() -> drawVisualizerNanoVG(nvg));
        }
    }

    private void drawVisualizerNanoVG(NanoVGManager nvg) {

        ScaledResolution sr = new ScaledResolution(mc);
        Shindo instance = Shindo.getInstance();
        MusicManager musicManager = instance.getMusicManager();
        AccentColor currentColor = instance.getColorManager().getCurrentColor();

        int offsetX = 0;

        if(musicManager.isPlaying()) {

            int index = 0;

            for(int i = 0; i < 120; i++) {

                MusicWaveform.animation[i].setAnimation(MusicWaveform.visualizer[i], 100);
                nvg.drawRect(offsetX, sr.getScaledHeight() + MusicWaveform.animation[i].getValue(), 10, sr.getScaledHeight(), ColorUtils.applyAlpha(currentColor.getInterpolateColor(index), 80));

                offsetX+=10;
                index--;
            }
        }
    }

    private void drawWaveformNanoVG() {

        MusicManager musicManager = Shindo.getInstance().getMusicManager();

        this.drawBackground(155, 100);
        this.drawRect(0, 17.5F, 155, 1);

        if(musicManager.isPlaying() && musicManager.getCurrentMusic() != null) {

            this.save();
            this.scissor(0, 0, 155, 100);

            this.drawText("Playing: " + musicManager.getCurrentMusic().getName(), 5.5F + addX, 6F, 10.5F, Fonts.MEDIUM);

            this.restore();

            float fontWidth = this.getTextWidth("Playing: " + musicManager.getCurrentMusic().getName(), 10.5F, Fonts.MEDIUM);

            if(fontWidth > this.getWidth()) {

                if(timer.delay(30)) {
                    if(((this.getWidth()) - fontWidth) - 10 < addX && !back) {
                        addX = addX - 1;
                    }else if(back && addX != 0) {
                        addX = addX + 1;
                    }
                    timer.reset();
                }

                if(addX <= ((this.getWidth()) - fontWidth) - 10) {
                    if(timer2.delay(3000)) {
                        back = true;
                    }
                }else {
                    if(!back) {
                        timer2.reset();
                    }
                }

                if(back){
                    if(addX == 0) {
                        if(timer2.delay(3000)) {
                            back = false;
                        }
                    }else {
                        if(back) {
                            timer2.reset();
                        }
                    }
                }
            }else {
                addX = 0;
                back = false;
            }

            for(int i = 0; i < 29; i++) {

                MusicWaveform.animation[i].setAnimation(MusicWaveform.visualizer[i], 100);

                this.drawRoundedRect(6 + (i * 5), MusicWaveform.animation[i].getValue() + 86, 3F, -MusicWaveform.animation[i].getValue(), 1.6F);
                this.drawRoundedRect(6 + (i * 5), MusicWaveform.animation[i].getValue() + 80, 3.5F, 3.5F, 3.5F / 2);
            }

            float current = musicManager.getCurrentTime();
            float end = musicManager.getEndTime();

            this.drawRoundedRect(6, 90.5F,  (current / end) * 144 - 4, 2.5F, 1.3F);
            this.drawRoundedRect(6 + ((current / end) * 144) - 2.4F, 89.8F, 4, 4, 2);
            this.drawRoundedRect(6 + ((current / end) * 144) + 3, 90.5F, 142 - ((current / end) * 150), 2.5F, 1.3F);
        } else {
            this.drawText("Nothing is playing", 5.5F, 6F, 10.5F, Fonts.MEDIUM);
            this.drawRoundedRect(6, 90.5F, 145, 2.5F, 1.3F);
        }

        this.setWidth(155);
        this.setHeight(100);
    }

    @Override
    public String getText() {

        MusicManager musicManager = Shindo.getInstance().getMusicManager();

        if(musicManager.isPlaying()) {
            return "Playing: " + musicManager.getCurrentMusic().getName();
        }else {
            return "Nothing is Playing";
        }
    }

    @Override
    public String getIcon() {
        return iconSetting.isToggled() ? LegacyIcon.MUSIC : null;
    }

    public static MusicInfoMod getInstance() {
        return instance;
    }

    public ComboSetting getDesignSetting() {
        return designSetting;
    }
}
