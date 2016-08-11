package growthcraft.bamboo.common.block;

import java.util.ArrayList;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
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
		setUnlocalizedName("bamboo_leaves");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
		setDefaultState(blockState.getBaseState().withProperty(DECAYABLE, false).withProperty(CHECK_DECAY, true));
	}

	@Override
	@SuppressWarnings({"rawtypes"})
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {DECAYABLE,CHECK_DECAY});
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState()
			.withProperty(DECAYABLE, (meta & 8) > 0)
			.withProperty(CHECK_DECAY, (meta & 4) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(DECAYABLE) ? 8 : 0) |
			(state.getValue(CHECK_DECAY) ? 4 : 0);
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
