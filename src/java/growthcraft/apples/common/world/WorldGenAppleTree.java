package growthcraft.apples.common.world;

import growthcraft.apples.GrowthCraftApples;

import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class WorldGenAppleTree extends WorldGenTrees
{
	public WorldGenAppleTree(boolean blockNotify)
	{
		super(blockNotify, 4, Blocks.log.getDefaultState(), GrowthCraftApples.appleLeaves.getBlock().getDefaultState(), false);
	}
}
