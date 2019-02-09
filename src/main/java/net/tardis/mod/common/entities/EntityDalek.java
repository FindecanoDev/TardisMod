package net.tardis.mod.common.entities;


import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tardis.mod.api.entities.IDontSufficate;
import net.tardis.mod.common.TDamageSources;
import net.tardis.mod.common.entities.ai.DalekAiRandomFly;
import net.tardis.mod.common.entities.ai.DalekMoveHelper;
import net.tardis.mod.common.items.TItems;
import net.tardis.mod.common.sounds.TSounds;
import net.tardis.mod.util.common.helpers.EntityHelper;

public class EntityDalek extends EntityMob implements IRangedAttackMob, EntityFlying, IDontSufficate {
	
	private ItemStack[] deathItems = new ItemStack[]{new ItemStack(TItems.power_cell, 20 + rand.nextInt(11)), new ItemStack(TItems.gunstick, 1), new ItemStack(TItems.circuts, 7 + rand.nextInt(3))};
	
	public EntityDalek(World world) {
		super(world);
		this.isImmuneToFire = true;
		((PathNavigateGround) this.getNavigator()).setCanSwim(true);
		this.moveHelper = new DalekMoveHelper(this);
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new DalekAiRandomFly(this));
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 40, 20.0F));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLiving.class, 0, false, false, (Predicate<Entity>) input -> !(input instanceof EntityDalek)));
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6000000238418579D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
	}
	
	
	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		EntityLaserRay laser = new EntityLaserRay(world, this, 7, TDamageSources.DALEK, new Vec3d(0, 1, 0));
		double d0 = target.posX - this.posX;
		double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - laser.posY;
		double d2 = target.posZ - this.posZ;
		double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
		laser.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getId() * 4));
		this.playSound(TSounds.dalek_ray, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		EntityHelper.lookAt(target.posX, target.posY, target.posZ, this);
		this.world.spawnEntity(laser);
	}
	
	
	@Override
	public void setSwingingArms(boolean swingingArms) {
	}
	
	@Override
	protected void jump() {
		super.jump();
	}
	
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.getAttackTarget() != null) {
			Entity target = getAttackTarget();
			EntityHelper.lookAt(target.posX, target.posY, target.posZ, this);
			
			if (this.hasNoGravity() && world.isRemote || world.isRemote && !onGround || world.isRemote && isAirBorne) {
				for (int x = 0; x <= 13; x++) {
					world.spawnParticle(EnumParticleTypes.REDSTONE, posX + (world.rand.nextDouble() - 0.5D) * 0.5D - 0.3D, this.posY, this.posZ + (world.rand.nextDouble() - 0.5D) * 0.5D - 0.2D, 1D, 1D, 2D);
					world.spawnParticle(EnumParticleTypes.REDSTONE, posX + (world.rand.nextDouble() + 0.5D) * 0.5D - 0.3D, this.posY, this.posZ + (world.rand.nextDouble() + 0.5D) * 0.5D - 0.2D, 1D, 1D, 2D);
				}
			}
		}
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!world.isRemote) {
			int index = rand.nextInt(deathItems.length);
			InventoryHelper.spawnItemStack(world, posX, posY, posZ, deathItems[index]);
		}
	}
	
	@Override
	public float getEyeHeight() {
		return 1F;
	}
}
