package net.tardis.mod.packets;

import java.util.ArrayList;
import java.util.Random;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.tardis.mod.common.dimensions.TDimensions;
import net.tardis.mod.common.tileentity.TileEntityTardis;
import net.tardis.mod.util.helpers.Helper;

public class MessageTelepathicCircut implements IMessage {

	public BlockPos pos = BlockPos.ORIGIN;
	public String name = "";
	
	public MessageTelepathicCircut() {
		
	}
	
	public MessageTelepathicCircut(BlockPos tardisPos, String name) {
		pos = tardisPos.toImmutable();
		if(name !=null) {
			this.name = name;
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		name = ByteBufUtils.readUTF8String(buf);
		pos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, name);
		buf.writeLong(pos.toLong());
	}

	public static class Handler implements IMessageHandler<MessageTelepathicCircut, IMessage>{
		
		@Override
		public IMessage onMessage(MessageTelepathicCircut message, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					MinecraftServer server = ctx.getServerHandler().player.getServer();
					WorldServer ws = DimensionManager.getWorld(TDimensions.id);
					EntityPlayer player = server.getPlayerList().getPlayerByUsername(message.name);
					TileEntity te = ws.getTileEntity(message.pos);
					if(te != null && te instanceof TileEntityTardis) {
						TileEntityTardis tardis = (TileEntityTardis)ws.getTileEntity(message.pos);
						WorldServer locationWorld = DimensionManager.getWorld(tardis.dimension);
						if(player != null) {
							tardis.setDesination(player.getPosition(), player.dimension);
							tardis.startFlight();
						}
						else {
							Biome b = Helper.findBiomeByName(message.name.toLowerCase().trim());
							if(b != null) {
								ArrayList<Biome> biomes = new ArrayList<>();
								biomes.add(b);
								BlockPos biomePos = locationWorld.getBiomeProvider().findBiomePosition(tardis.getLocation().getX(), tardis.getLocation().getZ(), 1000, biomes, new Random());
								if(biomePos != null && !biomePos.equals(BlockPos.ORIGIN)) {
									biomePos.add(0, locationWorld.getSeaLevel(), 0);
									tardis.setDesination(biomePos, tardis.dimension);
									tardis.startFlight();
								}
							}
							else {
								BlockPos structurePos = locationWorld.findNearestStructure(message.name.trim(), tardis.getLocation(), true);
								if(structurePos != null && !BlockPos.ORIGIN.equals(structurePos)) {
									tardis.setDesination(structurePos, tardis.dimension);
									tardis.startFlight();
								}
							}
						}
					}
				}});
			return null;
		}
		
	}
}