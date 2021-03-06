package net.tardis.mod.common.entities;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tardis.mod.Tardis;

public class EntityDalekCasing extends EntityLiving {

	public EntityDalekCasing(World worldIn) {
		super(worldIn);
		this.stepHeight = 1.2F;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (this.getControllingPassenger() != null) {
			Entity e = this.getControllingPassenger();
			if (e != null && e instanceof EntityLivingBase) {
				if (((EntityLivingBase) e).moveForward > 0) {
					Vec3d look = e.getLookVec().normalize().scale(0.15);
					this.motionX = look.x;
					this.motionZ = look.z;
				}
				this.prevRotationYaw = this.rotationYaw;
				this.rotationYaw = ((EntityLivingBase) e).rotationYawHead;
			}
		}
	}

	@Override
	protected void despawnEntity() {
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return (source.getTrueSource() == null || !this.getPassengers().contains(source.getTrueSource())) && super.attackEntityFrom(source, amount);
	}

	@Override
	public void dismountEntity(Entity entityIn) {
		if (world.isRemote && entityIn instanceof EntityPlayer) {
			this.setCamera(0);
		}
		super.dismountEntity(entityIn);
	}

	@Override
	public void updatePassenger(Entity pas) {
		super.updatePassenger(pas);
		Vec3d look = this.getPositionVector().add(this.getLookVec().scale(0.5));
		pas.setPosition(look.x, look.y, look.z);
	}

	@SideOnly(Side.CLIENT)
	public void setCamera(int i) {
		Minecraft.getMinecraft().gameSettings.thirdPersonView = i;
	}

	@Override
	public boolean canTrample(World world, Block block, BlockPos pos, float fallDistance) {
		return false;
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return this.getPassengers().size() <= 0;
	}

	@Override
	protected boolean canFitPassenger(Entity passenger) {
		return this.getPassengers().size() == 0;
	}

	@Override
	public double getYOffset() {
		return super.getYOffset();
	}

	@Override
	public boolean canRiderInteract() {
		return false;
	}

	@Override
	public double getMountedYOffset() {
		return 0.1D;
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		player.startRiding(this);
		if (world.isRemote) {
			this.setCamera(1);
		}
		return super.applyPlayerInteraction(player, vec, hand);
	}

	@Override
	public Entity getControllingPassenger() {
		return this.getPassengers().size() >= 1 ? this.getPassengers().get(0) : null;
	}

	@SideOnly(Side.CLIENT)
	@EventBusSubscriber(modid = Tardis.MODID, value = Side.CLIENT)
	public static class ClientEvent {

		@SubscribeEvent
		public static void stopArmRender(RenderHandEvent event) {
			if (isRiding()) {
				event.setCanceled(true);
			}
		}

		public static boolean isRiding() {
			return Minecraft.getMinecraft().player.getRidingEntity() != null && Minecraft.getMinecraft().player.getRidingEntity() instanceof EntityDalekCasing;
		}
	}
}
