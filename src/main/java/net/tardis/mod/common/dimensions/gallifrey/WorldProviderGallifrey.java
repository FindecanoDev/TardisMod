package net.tardis.mod.common.dimensions.gallifrey;

import javax.annotation.Nullable;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tardis.mod.client.renderers.sky.RenderVoid;
import net.tardis.mod.common.dimensions.TDimensions;

public class WorldProviderGallifrey extends WorldProviderSurface {


	@Override
	protected void init() {
		super.init();
		this.biomeProvider = new GallifreyBiomeProvider(world);
	}

	@Override
	public IChunkGenerator createChunkGenerator() {
		return new ChunkGeneratorGallifrey(world, world.getSeed());
	}
	
	public static RenderGallifreySky SKY = new RenderGallifreySky();
	
	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer() {
		return SKY;
	}
	
	@Nullable
	@Override
	public IRenderHandler getCloudRenderer() {
		return new RenderVoid();
	}
	
	@Override
	public DimensionType getDimensionType() {
		return TDimensions.GALLIFREY_TYPE;
	}
	
}
