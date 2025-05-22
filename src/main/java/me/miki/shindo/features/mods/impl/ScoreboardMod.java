package me.miki.shindo.features.mods.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.events.impl.EventRenderScoreboard;
import me.miki.shindo.features.mods.HUDMod;
import me.miki.shindo.features.nanovg.NanoVGManager;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.helpers.ColorHelper;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.RenderHelper;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ScoreboardMod extends HUDMod {

    private ScoreObjective objective;
    private boolean isFirstLoad;

    public ScoreboardMod() {
        super(
                "Scoreboard",
                "Adds Tweaks to the Scoreboard"
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Background", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Numbers", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Shadow", this, false));
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {

        NanoVGManager nvg = Shindo.getInstance().getNanoVGManager();

        if(isFirstLoad) {
            isFirstLoad = false;
        }

        if(mc.isSingleplayer()) {
            objective = null;
        }

        if(objective != null) {

            Scoreboard scoreboard = objective.getScoreboard();
            Collection<Score> scores = scoreboard.getSortedScores(objective);
            List<Score> filteredScores = Lists.newArrayList(Iterables.filter(scores, p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")));
            Collections.reverse(filteredScores);

            nvg.setupAndDraw(() -> {
                if(isShadow()) {
                    this.drawShadow(0, 0, this.getWidth() / this.getScale(), this.getHeight() / this.getScale(), 0);
                }
            });

            if(filteredScores.size() > 15) {
                scores = Lists.newArrayList(Iterables.skip(filteredScores, scores.size() - 15));
            } else {
                scores = filteredScores;
            }

            int maxWidth = fr.getStringWidth(objective.getDisplayName());

            for(Score score : scores) {

                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName());

                if(isNumber()) {
                    s +=  ": " + EnumChatFormatting.RED + score.getScorePoints();
                }

                maxWidth = Math.max(maxWidth, mc.fontRendererObj.getStringWidth(s));
            }

            int index = 0;

            GLHelper.startScale(this.getX(), this.getY(), this.getScale());

            for(Score score : scores) {

                index++;

                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                String playerName = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName());
                String scorePoints = EnumChatFormatting.RED + "" + score.getScorePoints();

                RenderHelper.drawRect(this.getX(), this.getY() + (index * fr.FONT_HEIGHT) + 1, maxWidth + 4, fr.FONT_HEIGHT, isBackground() ? ColorHelper.getColorByInt(1342177280) : new Color(0, 0, 0, 0));

                fr.drawString(playerName, this.getX() + 2, this.getY() + (index * fr.FONT_HEIGHT) + 1, 553648127);

                if(isNumber()) {
                    fr.drawString(scorePoints, (this.getX() + 2 + maxWidth + 2) - fr.getStringWidth(scorePoints), this.getY() + (index * fr.FONT_HEIGHT) + 1, 553648127);
                }

                if(index == scores.size()) {

                    String displayName = objective.getDisplayName();

                    RenderHelper.drawRect(this.getX(), this.getY(), 2 + maxWidth + 2, fr.FONT_HEIGHT, isBackground() ? ColorHelper.getColorByInt(1610612736) : new Color(0, 0, 0, 0));
                    RenderHelper.drawRect(this.getX(), this.getY() + fr.FONT_HEIGHT, 2 + maxWidth + 2, 1, isBackground() ? ColorHelper.getColorByInt(1610612736) : new Color(0, 0, 0, 0));

                    fr.drawString(displayName, this.getX() + 2 + maxWidth / 2 - fr.getStringWidth(displayName) / 2, this.getY() + 1, 553648127);
                }
            }

            GLHelper.endScale();

            int lastMaxWidth = maxWidth + 4;
            int lastMaxHeight = (index * fr.FONT_HEIGHT) + 10;

            this.setWidth(lastMaxWidth);
            this.setHeight(lastMaxHeight);
        }
    }

    @EventTarget
    public void onRenderScoreboard(EventRenderScoreboard event) {
        event.setCancelled(true);
        objective = event.getObjective();
    }

    private boolean isBackground() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private boolean isNumber() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Numbers").isCheckToggled();
    }

    private boolean isShadow() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Shadow").isCheckToggled();
    }

}
