package net.tardis.mod.common.entities.controls;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tardis.mod.common.systems.SystemStabilizers;
import net.tardis.mod.common.tileentity.TileEntityTardis;
import net.tardis.mod.common.tileentity.consoles.TileEntityTardis01;
import net.tardis.mod.common.tileentity.consoles.TileEntityTardis02;
import net.tardis.mod.util.helpers.Helper;

public class ControlStabilizers extends EntityControl{

	public ControlStabilizers(TileEntityTardis tardis) {
		super(tardis);
	}
	
	public ControlStabilizers(World world) {
		super(world);
		this.setSize(0.125F, 0.125F);
	}

	@Override
	public Vec3d getOffset(TileEntityTardis tardis) {
		if(tardis.getClass() == TileEntityTardis01.class || tardis.getClass() == TileEntityTardis02.class) {
			return Helper.convertToPixels(-3, -3, 13);
		}
		return new Vec3d(0, 0, 0);
	}

	@Override
	public void preformAction(EntityPlayer player) {
		if(!world.isRemote) {
			TileEntityTardis tardis = (TileEntityTardis)world.getTileEntity(this.getConsolePos());
			SystemStabilizers stab = tardis.getSystem(SystemStabilizers.class);
			stab.setOn(!stab.isOn());
		}
	}

}
