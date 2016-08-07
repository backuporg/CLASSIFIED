package growthcraft.bamboo.common.block;

import java.util.ArrayList;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IShearable;

public class BlockBambooLeaves extends BlockLeaves implements IShearable
{
	private int[] adjacentTreeBlocks;

	public BlockBambooLeaves()
	{
		super();
		setLightOpacity(1);
		setStepSound(soundTypeGrass);
		setHardness(0.2F);
		setTickRandomly(true);
		setUnlocalizedName("grc.bamboo_leaves");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
		setDefaultState(blockState.getBaseState().withProperty(BlockLeaves.DECAYABLE, false));
	}

	@Override
	public BlockPlanks.EnumType getWoodType(int meta)
	{
		return null;
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(Blocks.leaves, 1));
		return ret;
	}
}
