package growthcraft.apples.common.block;

import java.util.Random;
import growthcraft.apples.common.world.WorldGenAppleTree;
import growthcraft.apples.GrowthCraftApples;
import growthcraft.core.GrowthCraftCore;
import growthcraft.api.core.util.BlockFlags;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BlockAppleSapling extends BlockBush implements IGrowable
{
	private final int growthRate = GrowthCraftApples.getConfig().appleSaplingGrowthRate;

	public BlockAppleSapling()
	{
		super(Material.plants);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setUnlocalizedName("grc.apple_sapling");
		setTickRandomly(true);
		setCreativeTab(GrowthCraftCore.creativeTab);
		final float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		setDefaultState(blockState.getBaseState().withProperty(BlockSapling.STAGE, 0));
	}

	public void growTree(World world, BlockPos pos, Random rand)
	{
		if (!TerrainGen.saplingGrowTree(world, rand, pos)) return;
		final IBlockState oldState = world.getBlockState(pos);
		final WorldGenerator generator = new WorldGenAppleTree(true);
		world.setBlockToAir(pos);
		if (!generator.generate(world, rand, pos))
		{
			world.setBlockState(pos, oldState, BlockFlags.ALL);
		}
	}

	public void markOrGrowMarked(World world, BlockPos pos, Random rand)
	{
		final IBlockState state = world.getBlockState(pos);
		final int meta = state.getValue(BlockSapling.STAGE);
		if (meta == 0)
		{
			world.setBlockState(pos, state.withProperty(BlockSapling.STAGE, 1), BlockFlags.SUPRESS_RENDER);
		}
		else
		{
			growTree(world, pos, rand);
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);
		if (!world.isRemote)
		{
			if (getLightValue(world, pos.up()) >= 9 && rand.nextInt(growthRate) == 0)
			{
				markOrGrowMarked(world, pos, rand);
			}
		}
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		return state.getValue(BlockSapling.STAGE) == 0;
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		if (rand.nextFloat() < 0.45D)
		{
			growTree(world, pos, rand);
		}
	}
}
