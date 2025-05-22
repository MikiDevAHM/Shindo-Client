/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.hudeditor.impl.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.hudeditor.impl.HudMod;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardHud extends HudMod {


    public ScoreboardHud() {
        super("Scoreboard", 100, 100);
    }


    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled()) {

            ScoreObjective scoreobjective = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreobjective != null) {
                drawScoreboard(scoreobjective, isBackground(), isRemoveNumbers());
            }
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled()) {

            ScoreObjective scoreobjective = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);

            if (scoreobjective != null) {
                drawScoreboard(scoreobjective, isBackground(), isRemoveNumbers());
            }
        }
        GLHelper.endScale();
    }

    private void drawScoreboard(ScoreObjective objective, boolean background, boolean numbers) {
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        List<Score> list = collection.stream().filter(score -> score.getPlayerName() != null &&
                !score.getPlayerName().startsWith("#")).collect(Collectors.toList());

        if (list.size() > 15) {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        } else {
            collection = list;
        }

        int displayText = mc.fontRendererObj.getStringWidth(objective.getDisplayName()) + 2;

        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String text = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            displayText = Math.max(displayText, mc.fontRendererObj.getStringWidth(text));
        }

        int y = getY();
        int x = getX();

        int textHeight = mc.fontRendererObj.FONT_HEIGHT;

        int index = collection.size() - 1;
        for (Score score1 : collection) {
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score1.getPlayerName());
            String mainText = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score1.getPlayerName());
            String redNumbers = EnumChatFormatting.RED + "" + score1.getScorePoints();
            int calculatedY = y + index * textHeight;

            if (index == 0) {
                String topText = objective.getDisplayName();
                if (background) {
                    Helper2D.drawRectangle(x, calculatedY, displayText + 4, textHeight, 1610612736);
                    Helper2D.drawRectangle(x, calculatedY + textHeight, displayText + 4, 1, 1342177280);
                }
                mc.fontRendererObj.drawString(topText, x + 2 + displayText / 2 - mc.fontRendererObj.getStringWidth(topText) / 2, calculatedY + 1, -1);
            }

            if (background) {
                Helper2D.drawRectangle(x, calculatedY + textHeight + 1, displayText + 4, textHeight, 1342177280);
            }
            mc.fontRendererObj.drawString(mainText, x + 2, calculatedY + textHeight + 1, -1);
            if (!numbers) {
                mc.fontRendererObj.drawString(redNumbers, x + 4 + displayText - mc.fontRendererObj.getStringWidth(redNumbers), calculatedY + textHeight + 1, -1);
            }

            index--;
        }

        setW(displayText + 4);
        setH((collection.size() + 1) * textHeight);
    }

    private boolean isBackground() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private boolean isRemoveNumbers() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Remove Red Numbers").isCheckToggled();
    }
}
