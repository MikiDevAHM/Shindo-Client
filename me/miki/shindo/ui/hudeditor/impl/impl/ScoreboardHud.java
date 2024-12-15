package me.miki.shindo.ui.hudeditor.impl.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.render.Render2DEvent;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.hudeditor.HudEditor;
import me.miki.shindo.ui.hudeditor.impl.HudMod;
import me.miki.shindo.util.render.GLHelper;
import me.miki.shindo.util.render.Helper2D;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

public class ScoreboardHud extends HudMod {

    public ScoreboardHud() {
        super("Scoreboard", 10, 10);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled()) {
            drawScoreboardPlaceHolder(isBackground(), isRemoveNumbers(), getColor(), isModern());
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled() && !(mc.currentScreen instanceof HudEditor)) {
            ScoreObjective scoreobjective = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreobjective != null) {
                drawScoreboard(scoreobjective, isBackground(), isRemoveNumbers(), getColor(), isModern());
            }
        }
        GLHelper.endScale();
    }

    private void drawScoreboard(ScoreObjective objective, boolean background, boolean numbers, int color, boolean modern) {
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
            int calculatedY2 = y + index * Shindo.getInstance().getFontHelper().size15.getFontHeight();

            if (index == 0) {
                String topText = objective.getDisplayName();
                if (modern) {
                	if (background) {
                		Helper2D.drawRoundedRectangle(x, calculatedY2, displayText + 4, Shindo.getInstance().getFontHelper().size15.getFontHeight(), 2, 0x70000000, -1);
                		//Helper2D.drawRoundedRectangle(x, calculatedY2 + Shindo.getInstance().getFontHelper().size15.getFontHeight(), displayText + 4, 1, 2, Style.getColor(50).getRGB(), 0);
                	}
                	Shindo.getInstance().getFontHelper().size15.drawString(topText, x + 2 + displayText / 2 - Shindo.getInstance().getFontHelper().size15.getStringWidth(topText) / 2, calculatedY2 + 3, color);
                } else {
                	
	                if (background) {
	                    Helper2D.drawRectangle(x, calculatedY, displayText + 4, textHeight, 1610612736);
	                    Helper2D.drawRectangle(x, calculatedY + textHeight, displayText + 4, 1, 1342177280);
	                }
	                mc.fontRendererObj.drawString(topText, x + 2 + displayText / 2 - mc.fontRendererObj.getStringWidth(topText) / 2, calculatedY + 1, color);
                }
            }
            
            if (modern) {
	            if (background) {
	                Helper2D.drawRoundedRectangle(x, calculatedY2 + Shindo.getInstance().getFontHelper().size15.getFontHeight(), displayText + 4, Shindo.getInstance().getFontHelper().size15.getFontHeight(), 2, 0x50000000, -1);
	            }
	            Shindo.getInstance().getFontHelper().size15.drawString(mainText, x + 2, calculatedY2 + Shindo.getInstance().getFontHelper().size15.getFontHeight() + 3, color);
	            if (!numbers) {
	            	Shindo.getInstance().getFontHelper().size15.drawString(redNumbers, x - 1 + displayText - Shindo.getInstance().getFontHelper().size15.getStringWidth(redNumbers), calculatedY2 + Shindo.getInstance().getFontHelper().size15.getFontHeight() + 3, -1);
	            }
            } else {
            	
	            if (background) {
	                Helper2D.drawRectangle(x, calculatedY + textHeight + 1, displayText + 4, textHeight, 1342177280);
	            }
	            mc.fontRendererObj.drawString(mainText, x + 2, calculatedY + textHeight + 1, color);
	            if (!numbers) {
	                mc.fontRendererObj.drawString(redNumbers, x + 4 + displayText - mc.fontRendererObj.getStringWidth(redNumbers), calculatedY + textHeight + 1, -1);
	            }
            }
            index--;
        }

        setW(displayText + 4);
        setH((collection.size() + 1) * textHeight);
    }

    private void drawScoreboardPlaceHolder(boolean background, boolean numbers, int color, boolean modern) {
        String objective = "Scoreboard";
        int displayText = mc.fontRendererObj.getStringWidth(objective) + 2;

        String[] names = {"Steve", "Alex", "Zuri", "Sunny", "Noor", "Makena", "Kai", "Efe", "Ari"};
        int collectionSize = names.length;

        for (int i = 0; i < collectionSize; i++) {
            String text = names[i] + ": " + EnumChatFormatting.RED + i;
            displayText = Math.max(displayText, mc.fontRendererObj.getStringWidth(text));
        }

        int y = getY();
        int x = getX();

        int textHeight = mc.fontRendererObj.FONT_HEIGHT;
        
        

        int index = collectionSize - 1;
        for (int i = 0; i < collectionSize; i++) {
            String mainText = names[i];
            String redNumbers = EnumChatFormatting.RED + "" + i;
            int calculatedY = y + index * textHeight;
            int calculatedY2 = y + index * Shindo.getInstance().getFontHelper().size15.getFontHeight();

            if (index == 0) {
                if (modern) {
                	if (background) {
                		Helper2D.drawRoundedRectangle(x, calculatedY2, displayText + 4, Shindo.getInstance().getFontHelper().size15.getFontHeight(), 2, Style.getColor(70).getRGB(), -1);
                		//Helper2D.drawRoundedRectangle(x, calculatedY2 + Shindo.getInstance().getFontHelper().size15.getFontHeight(), displayText + 4, 1, 2, Style.getColor(50).getRGB(), 0);
                	}
                	Shindo.getInstance().getFontHelper().size15.drawString(objective, x + 2 + displayText / 2 - Shindo.getInstance().getFontHelper().size15.getStringWidth(objective) / 2, calculatedY2 + 3, color);
                } else {
                	
	                if (background) {
	                    Helper2D.drawRectangle(x, calculatedY, displayText + 4, textHeight, 1610612736);
	                    Helper2D.drawRectangle(x, calculatedY + textHeight, displayText + 4, 1, 1342177280);
	                }
	                mc.fontRendererObj.drawString(objective, x + 2 + displayText / 2 - mc.fontRendererObj.getStringWidth(objective) / 2, calculatedY + 1, color);
                }
            }
            
            if (modern) {
	            if (background) {
	                Helper2D.drawRoundedRectangle(x, calculatedY2 + Shindo.getInstance().getFontHelper().size15.getFontHeight(), displayText + 4, Shindo.getInstance().getFontHelper().size15.getFontHeight(), 2, Style.getColor(50).getRGB(), -1);
	            }
	            Shindo.getInstance().getFontHelper().size15.drawString(mainText, x + 2, calculatedY2 + Shindo.getInstance().getFontHelper().size15.getFontHeight() + 3, color);
	            if (!numbers) {
	            	Shindo.getInstance().getFontHelper().size15.drawString(redNumbers, x - 1 + displayText - Shindo.getInstance().getFontHelper().size15.getStringWidth(redNumbers), calculatedY2 + Shindo.getInstance().getFontHelper().size15.getFontHeight() + 3, -1);
	            }
            } else {
            	
	            if (background) {
	                Helper2D.drawRectangle(x, calculatedY + textHeight + 1, displayText + 4, textHeight, 1342177280);
	            }
	            mc.fontRendererObj.drawString(mainText, x + 2, calculatedY + textHeight + 1, color);
	            if (!numbers) {
	                mc.fontRendererObj.drawString(redNumbers, x + 4 + displayText - mc.fontRendererObj.getStringWidth(redNumbers), calculatedY + textHeight + 1, -1);
	            }
            }

            index--;
        }

        setW(displayText + 4);
        setH((collectionSize + 1) * textHeight);
    }
    
    private int getColor() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private boolean isRemoveNumbers() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Remove Red Numbers").isCheckToggled();
    }
}
