package net.tardis.mod.client.guis.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class ButtonText extends GuiButton {

	FontRenderer fr;

	public ButtonText(int buttonId, int x, int y, FontRenderer fr, String buttonText) {
		super(buttonId, x - fr.getStringWidth(buttonText) / 2, y, fr.getStringWidth(buttonText), fr.FONT_HEIGHT, buttonText);
		this.fr = fr;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		this.drawString(fr, displayString, this.x, this.y, Color.WHITE.getRGB());
	}
}
