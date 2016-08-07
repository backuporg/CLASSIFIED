package growthcraft.apples.common.world;

import java.util.Random;

import growthcraft.apples.GrowthCraftApples;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.World;

public class WorldGenAppleTree extends WorldGenTrees
{
	public WorldGenAppleTree(boolean blockNotify)
	{
		super(blockNotify, 4, Blocks.log.getDefaultState(), GrowthCraftApples.appleLeaves.getBlock().getDefaultState(), false);
	}
}
