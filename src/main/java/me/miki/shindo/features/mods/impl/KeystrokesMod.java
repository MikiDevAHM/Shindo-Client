/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.features.mods.HUDMod;
import me.miki.shindo.features.nanovg.NanoVGManager;
import me.miki.shindo.features.nanovg.font.Fonts;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.helpers.animation.simple.SimpleAnimation;
import org.lwjgl.input.Keyboard;

public class KeystrokesMod extends HUDMod {

    private SimpleAnimation[] animations = new SimpleAnimation[5];

    public KeystrokesMod() {
        super(
                "Keystrokes",
                "Shows your Keystrokes on the HUD."
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Space", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Unmarked", this, false));

        for (int i = 0; i < 5; i++) {
            animations[i] = new SimpleAnimation();
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {

        NanoVGManager nvg = Shindo.getInstance().getNanoVGManager();

        nvg.setupAndDraw(() -> drawNanoVG());
    }

    private void drawNanoVG() {

        boolean openGui = mc.currentScreen != null;

        animations[0].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) ? 1.0F : 0.0F, 16);
        animations[1].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) ? 1.0F : 0.0F, 16);
        animations[2].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) ? 1.0F : 0.0F, 16);
        animations[3].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()) ? 1.0F : 0.0F, 16);
        animations[4].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()) ? 1.0F : 0.0F, 16);

        // W
        this.drawBackground(32, 0, 28, 28);

        // A
        this.drawBackground(0, 32, 28, 28);

        // S
        this.drawBackground(32, 32, 28, 28);

        // D
        this.drawBackground(64, 32, 28, 28);

        // W
        this.save();
        this.scale(32, 0, 28, 28, animations[0].getValue());
        this.drawRoundedRect(32, 0, 28, 28, 6, this.getFontColor((int) (120 * animations[0].getValue())));
        this.restore();

        // A
        this.save();
        this.scale(0, 32, 28, 28, animations[1].getValue());
        this.drawRoundedRect(0, 32, 28, 28, 6, this.getFontColor((int) (120 * animations[1].getValue())));
        this.restore();

        // S
        this.save();
        this.scale(32, 32, 28, 28, animations[2].getValue());
        this.drawRoundedRect(32, 32, 28, 28, 6, this.getFontColor((int) (120 * animations[2].getValue())));
        this.restore();

        // D
        this.save();
        this.scale(64, 32, 28, 28, animations[3].getValue());
        this.drawRoundedRect(64, 32, 28, 28, 6, this.getFontColor((int) (120 * animations[3].getValue())));
        this.restore();

        if(!isUnmarked()) {
            this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()), 32 + (28F / 2F), (28F / 2F) - 4, 12, Fonts.REGULAR);
            this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()), 0 + (28F / 2F), 32 + (28F / 2F) - 4, 12, Fonts.REGULAR);
            this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()), 32 + (28F / 2F), 32 + (28F / 2F) - 4, 12, Fonts.REGULAR);
            this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()), 64 + (28F / 2F), 32 + (28F / 2F) - 4, 12, Fonts.REGULAR);
        }

        if(isSpace()) {

            this.drawBackground(0, 64, (28 * 3) + 8, 22);

            this.save();
            this.scale(0, 64, (28 * 3) + 8, 22, animations[4].getValue());
            this.drawRoundedRect(0, 64, (28 * 3) + 8, 22, 6, this.getFontColor((int) (120 * animations[4].getValue())));
            this.restore();

            if(!isUnmarked()) {
                this.drawRoundedRect(10, 74F, (26 * 3) - 6, 2, 1);
            }
        }

        this.setWidth(28 * 3 + 8);
        this.setHeight(isSpace() ? 64 + 22 : 32 + 28);
    }

    private boolean isSpace() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Space").isCheckToggled();
    }
    private boolean isUnmarked() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Unmarked").isCheckToggled();
    }
}
