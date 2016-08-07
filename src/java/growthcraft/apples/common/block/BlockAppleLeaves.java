package growthcraft.apples.common.block;

import java.util.ArrayList;
import java.util.Random;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.api.core.util.BlockFlags;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAppleLeaves extends BlockLeaves implements IGrowable
{
	public BlockAppleLeaves()
	{
		super();
		setTickRandomly(true);
		setHardness(0.2F);
		setLightOpacity(1);
		setStepSound(soundTypeGrass);
		setUnlocalizedName("grc.apple_leaves");
		setCreativeTab(GrowthCraftApples.creativeTab);
		setDefaultState(blockState.getBaseState().withProperty(DECAYABLE, false));
	}

	@Override
	public BlockPlanks.EnumType getWoodType(int meta)
	{
		return null;
	}

	private void growApple(World world, Random rand, BlockPos pos)
	{
		if (world.isAirBlock(pos.down()))
		{
			world.setBlockState(pos.down(), GrowthCraftApples.appleBlock.getBlock().getDefaultState());
		}
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		return world.isAirBlock(pos.down()) && state.getValue(DECAYABLE) == false;
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		growApple(world, rand, pos);
	}


	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote)
		{
			if (state.getValue(DECAYABLE) == false)
			{
				// Spawn Apple
				if (world.rand.nextInt(GrowthCraftApples.getConfig().appleLeavesGrowthRate) == 0)
				{
					growApple(world, rand, pos);
				}
			}
		}
		super.updateTick(world, pos, state, rand);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
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
