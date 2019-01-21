package net.tardis.mod.client.guis.manual;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tardis.mod.Tardis;

public class GuiManual extends GuiScreen {
	public static final ResourceLocation TEXTURE = new ResourceLocation(Tardis.MODID, "textures/gui/manual.png");
	private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
	//private static ManualPage[] pages = new ManualPage[]{};
	private static ScaledResolution res;
	private GuiButton nextPage;
	private GuiButton previousPage;
	public int gui_width = 281, gui_height = 208;
	private static int current_page = 0;
	Minecraft mc;

	public GuiManual() {
		mc = Minecraft.getMinecraft();
		res = new ScaledResolution(mc);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int posX = (res.getScaledWidth() - gui_width) / 2;
		int posY = (res.getScaledHeight() - gui_height) / 2;
		GlStateManager.pushMatrix();
		mc.getTextureManager().bindTexture(TEXTURE);
		drawModalRectWithCustomSizedTexture(posX, posY, 0, 0, gui_width, gui_height, 512, 512);
		
		/**int i = 17;
		int j = 64;
		for (String s : pages[current_page].getContents()) {
			this.drawCenteredString(this.fontRenderer, s, posX + j, posY + i, 0x000000);
			i += this.fontRenderer.FONT_HEIGHT;
			if(i > 208) {
				i = 17;
				j = 200;
			}
		}**/
		
		GlStateManager.popMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	

	@Override
	public void initGui() {
		this.buttonList.clear();
		this.nextPage = new NextPageButton(0, (this.width - gui_width) / 2 + 235, res.getScaledHeight() / 2 + gui_height / 4, true);
		buttonList.add(this.nextPage);
		this.previousPage = new NextPageButton(1, (this.width - gui_width) / 2 + 25, res.getScaledHeight() / 2 + gui_height / 4, false);
		buttonList.add(this.previousPage);
		this.previousPage.visible = true;
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button == this.nextPage) {
			this.current_page ++;
		} else {
			this.current_page --;
		}
		/*if(this.current_page > 0) {
			this.previousPage.visible = true;
		} else {
			this.previousPage.visible = false;
		}
		if(this.current_page == this.pages.length) {
			this.nextPage.visible = false;
		} else {
			this.nextPage.visible = true;
		} */
		System.out.println(this.current_page);
		super.actionPerformed(button);
	}

	@SideOnly(Side.CLIENT)
	static class NextPageButton extends GuiButton {
		private final boolean isForward;

		public NextPageButton(int buttonId, int x, int y, boolean isForwardIn) {
			super(buttonId, x, y, 23, 13, "");
			this.isForward = isForwardIn;
		}

		/** Stolen from minecraft's book GUI**/
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
				int i = 0;
				int j = 192;

				if (flag) {
					i += 23;
				}

				if (!this.isForward) {
					j += 13;
				}

				this.drawTexturedModalRect(this.x, this.y, i, j, 23, 13);
			}
		}
	}

}
