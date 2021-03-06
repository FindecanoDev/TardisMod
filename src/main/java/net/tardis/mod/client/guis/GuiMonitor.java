package net.tardis.mod.client.guis;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.tardis.mod.Tardis;
import net.tardis.mod.client.guis.elements.MonitorButton;
import net.tardis.mod.common.protocols.ITardisProtocol;
import net.tardis.mod.common.protocols.TardisProtocol;
import net.tardis.mod.common.tileentity.TileEntityTardis;
import net.tardis.mod.network.NetworkHandler;
import net.tardis.mod.network.packets.MessageProtocol;

public class GuiMonitor extends GuiScreen {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Tardis.MODID, "textures/gui/monitor_ui.png");
	static final int GUI_WIDTH = 256;
	static final int GUI_HEIGHT = 192;
	public BlockPos pos = BlockPos.ORIGIN;

	public GuiMonitor(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		mc.getTextureManager().bindTexture(TEXTURE);
		int x = (width - GUI_WIDTH) / 2;
		int y = (height - GUI_HEIGHT) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		int x_change = 0;
		int y_change = 0;
		int id = 0;
		for (ITardisProtocol p : TardisProtocol.protocols.values()) {
			this.addButton(new MonitorButton(id, ((width - GUI_WIDTH) / 2) + 11 + x_change, ((height - GUI_HEIGHT) / 2) + 8 + y_change, new TextComponentTranslation(p.getNameKey()).getFormattedText()));
			id++;
			x_change += 80;
			if (id % 3 == 0) {
				x_change = 0;
				y_change += 36;
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		Minecraft.getMinecraft().displayGuiScreen(null);
		NetworkHandler.NETWORK.sendToServer(new MessageProtocol(pos, button.id));
		TardisProtocol.getProtocolFromId(button.id).onActivated(mc.world, (TileEntityTardis) mc.world.getTileEntity(pos));
		super.actionPerformed(button);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
