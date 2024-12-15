package me.miki.shindo.ui.titlescreen;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import me.miki.shindo.Shindo;
import me.miki.shindo.ui.components.buttons.HeadButton;
import me.miki.shindo.ui.components.buttons.IconButton;
import me.miki.shindo.ui.components.buttons.TextButton;
import me.miki.shindo.util.font.GlyphPageFontRenderer;
import me.miki.shindo.util.render.Helper2D;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

public class MainMenu extends GuiScreen {
	
	private int shouldDrawCredits = 0;
	
	
    private final ArrayList<TextButton> textButtons = new ArrayList<>();
    private final ArrayList<IconButton> iconButtons = new ArrayList<>();
    private final ArrayList<HeadButton> headButtons = new ArrayList<>();
    
    
    public MainMenu() {
        textButtons.add(new TextButton("Singleplayer", width / 8 - 75, height / 2));
        textButtons.add(new TextButton("Multiplayer", width / 8 - 75, height / 2 + 25));
        textButtons.add(new TextButton("Settings", width / 8 - 75, height / 2 + 50));
        textButtons.add(new TextButton("Credits", width / 8 - 75, height / 2 + 75 ));
        iconButtons.add(new IconButton("cross.png", width - 25, 5));
        
        Shindo.getInstance().getIpc().update("Idle", "Main Menu");
    }

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		
		
		GlStateManager.disableAlpha();
		Helper2D.drawPicture(0, 0, width, height, -1, "bg.png");
		GlStateManager.enableAlpha();
		Helper2D.drawRectangle(0, 0, width, height, 0x70000000);
		
		
		Helper2D.drawRoundedRectangle(width / 2 + width / 4 - 25, (height / 3 * 2) - 25, width / 4, height / 3, 2, 0x70000000, 0);
		Helper2D.drawRectangle(0, 0, width / 4, height, 0x70000000);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		

	    int y = 0;
	    for (TextButton textButton : textButtons) {
	    	textButton.renderButton(width / 8 - 70, height / 2 + y * 25, mouseX, mouseY);
	        y++;
	    }

	    for (IconButton iconButton : iconButtons) {
	    	if (iconButton.getIcon().equals("cross.png")) {
	    		iconButton.renderButton(width - 25, 5, mouseX, mouseY);
	        }
	    }
	    if (shouldDrawCredits == 1) {
	    	drawCredits();
	    }
	    
	    drawChangeLog();
	    drawLogo();
	    drawCopyright();
	}
	
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (TextButton textButton : textButtons) {
            if (textButton.isHovered(mouseX, mouseY)) {
                switch (textButton.getText()) {
                    case "Singleplayer":
                        mc.displayGuiScreen(new GuiSelectWorld(this));
                        break;
                    case "Multiplayer":
                        mc.displayGuiScreen(new GuiMultiplayer(this));
                        break;
                    case "Settings":
                        mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                        break;
                    case "Credits":
                    	if(shouldDrawCredits == 0) {
                    		shouldDrawCredits = 1;
                    	} else {
                    		shouldDrawCredits = 0;
                    	}
                }
            }
        }

        for (IconButton iconButton : iconButtons) {
            if (iconButton.isHovered(mouseX, mouseY)) {
                if (iconButton.getIcon().equals("cross.png")) {
                    mc.shutdown();
                }
            }
        }
        

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    private void drawCredits() {
		Helper2D.drawRoundedRectangle(width / 2 + width / 4 - 25, 30, width / 4, height / 3, 2, 0x70000000, 0);
		Helper2D.drawRoundedRectangle(width / 2 + width / 4 - 25, 30, width / 4, 30, 2, 0x70000000, 0);
		
		
		Shindo.getInstance().getFontHelper().size20.drawString("Credits. Thank You", width / 2 + width / 4 - 25 + 5, 35, -1);
		
		GlyphPageFontRenderer font = Shindo.getInstance().getFontHelper().size15;
		
		font.drawString("[ Cloud Client Dev ]", width / 2 + width / 4 - 25 + 5, 70, -1);
		font.drawString("UI, Mods, Huds, Helpers Systems", width / 2 + width / 4 - 25 + 5, 80, -1);
		font.drawString("", width / 2 + width / 4 - 25 + 5, 90, -1);
		font.drawString("[ Sk1er ]", width / 2 + width / 4 - 25 + 5, 100, -1);
		font.drawString("Bug Fixes, Full Screen Options ", width / 2 + width / 4 - 25 + 5, 110, -1);
		font.drawString("1.7 Animations", width / 2 + width / 4 - 25 + 5, 120, -1);
		font.drawString("", width / 2 + width / 4 - 25 + 5, 130, -1);
		font.drawString("[ Egold555 ]", width / 2 + width / 4 - 25 + 5, 140, -1);
		font.drawString("Discord RPC", width / 2 + width / 4 - 25 + 5, 150, -1);
		font.drawString("", width / 2 + width / 4 - 25 + 5, 160, -1);
		font.drawString("[ superblaubeere27 ]", width / 2 + width / 4 - 25 + 5, 170, -1);
		font.drawString("Notification System", width / 2 + width / 4 - 25+ 5, 180, -1);
    }
    
    private void drawLogo() {
        Helper2D.drawPicture(width / 8 - 27, height / 2 - 88, 60, 60, 0, "logo.png");
        Helper2D.drawPicture(width / 8 - 50, height / 2 - 30, 105, 27, 0, "shindo-text.png");
    }

    /**
     * Draws the "Cloud Client" Text and Mojang Copyright Notice on the bottom
     */

    private void drawCopyright() {
        String copyright = "Copyright Mojang Studios. Do not distribute!";
        String text = Shindo.NAME + " Client " + Shindo.VERSION + " | by " + Shindo.AUTHOR;
        Shindo.getInstance().getFontHelper().size20.drawString(copyright, width - Shindo.getInstance().getFontHelper().size20.getStringWidth(copyright) - 2, height - Shindo.getInstance().getFontHelper().size20.getFontHeight(), 0x50ffffff);
        Shindo.getInstance().getFontHelper().size20.drawString(text, 4, height - Shindo.getInstance().getFontHelper().size20.getFontHeight(), 0x50ffffff);
    }
    
    private void drawChangeLog() {
    	Shindo.getInstance().getFontHelper().size30.drawString("Changelog", (width / 2 + width / 4 - 25) + width / 12, (height / 3 * 2) - 25 + (height / 24) - 10, new Color(221, 67, 67).getRGB());
    	
    	GlyphPageFontRenderer font = Shindo.getInstance().getFontHelper().size15;
    	String plus = EnumChatFormatting.GOLD +"[ " + EnumChatFormatting.GREEN + "+" + EnumChatFormatting.GOLD + " ] " + EnumChatFormatting.GREEN;
    	String minus = EnumChatFormatting.GOLD +"[ " + EnumChatFormatting.RED + "-" + EnumChatFormatting.GOLD + " ] " + EnumChatFormatting.RED;
    	font.drawString(plus + "Click Gui", (width / 2 + width / 4 - 25) + 10, (height / 3 * 2) - 25 + (height / 24) + 20, -1);
    	font.drawString(plus + "Optimizations", (width / 2 + width / 4 - 25) + 10, (height / 3 * 2) - 25 + (height / 24) + 30, -1);
    	font.drawString(plus + "Mods", (width / 2 + width / 4 - 25) + 10, (height / 3 * 2) - 25 + (height / 24) + 40, -1);
    	font.drawString(plus + "Discord RPC", (width / 2 + width / 4 - 25) + 10, (height / 3 * 2) - 25 + (height / 24) + 50, -1);
    	font.drawString(plus + "Bug Fixes", (width / 2 + width / 4 - 25) + 10, (height / 3 * 2) - 25 + (height / 24) + 60, -1);
    	font.drawString(minus + "Time Changer", (width / 2 + width / 4 - 25) + 10, (height / 3 * 2) - 25 + (height / 24) + 70, -1);
    }
	
}
