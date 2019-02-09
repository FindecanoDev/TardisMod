package net.tardis.mod.common.entities.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.BlockPos;
import net.tardis.mod.common.entities.EntityDalek;

import java.util.Random;

public class DalekAiRandomFly extends EntityAIBase {
	private final EntityDalek parentEntity;
	
	public DalekAiRandomFly(EntityDalek dalek) {
		this.parentEntity = dalek;
		this.setMutexBits(1);
	}
	
	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		
		
		
		EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
		
		if (!entitymovehelper.isUpdating()) {
			return true;
		} else {
			double d0 = entitymovehelper.getX() - this.parentEntity.posX;
			double d1 = entitymovehelper.getY() - this.parentEntity.posY;
			double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			return d3 < 1.0D || d3 > 3600.0D;
		}
	}
	
	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}
	
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
			Random random = this.parentEntity.getRNG();
			double d0 = this.parentEntity.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d1 = this.parentEntity.posY + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			double d2 = this.parentEntity.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
	}
}
