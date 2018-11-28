package net.tardis.mod.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockItemDrop extends BlockBase {
	
	private Item item;
	private int count;
	
	public BlockItemDrop(Item item, int count) {
		this.item = item;
		this.count = count;
		this.setResistance(1F);
	}

	
	public BlockItemDrop(Item item) {
		this(item, 1);
	}
	
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,int fortune) {
		drops.add(new ItemStack(item, count));
	}

}
