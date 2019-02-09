package net.tardis.mod.common.entities.ai;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.tardis.mod.common.entities.EntityDalek;

public class DalekMoveHelper extends EntityMoveHelper
{
	private final EntityDalek parentEntity;
	private int courseChangeCooldown;
	
	public DalekMoveHelper(EntityDalek dalek)
	{
		super(dalek);
		this.parentEntity = dalek;
	}
	
	@Override
	public void onUpdateMoveHelper()
	{
		if (this.action == EntityMoveHelper.Action.MOVE_TO)
		{
			double d0 = this.posX - this.parentEntity.posX;
			double d1 = this.posY - this.parentEntity.posY;
			double d2 = this.posZ - this.parentEntity.posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			
			if (this.courseChangeCooldown-- <= 0)
			{
				this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
				d3 = (double) MathHelper.sqrt(d3);
				
				if (this.isNotColliding(this.posX, this.posY, this.posZ, d3))
				{
					this.parentEntity.motionX += d0 / d3 * 0.1D;
					this.parentEntity.motionY += d1 / d3 * 0.1D;
					this.parentEntity.motionZ += d2 / d3 * 0.1D;
				}
				else
				{
					this.action = EntityMoveHelper.Action.WAIT;
				}
			}
		}
	}
	
	/**
	 * Checks if entity bounding box is not colliding with terrain
	 */
	private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
	{
		double d0 = (x - this.parentEntity.posX) / p_179926_7_;
		double d1 = (y - this.parentEntity.posY) / p_179926_7_;
		double d2 = (z - this.parentEntity.posZ) / p_179926_7_;
		AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();
		
		for (int i = 1; (double)i < p_179926_7_; ++i)
		{
			axisalignedbb = axisalignedbb.offset(d0, d1, d2);
			
			if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, axisalignedbb).isEmpty())
			{
				return false;
			}
		}
		
		return true;
	}
}
