package growthcraft.bees.common.world;

import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.bees.common.block.BlockBeeHive;

import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;

public class WorldGenBeeHive extends WorldGenerator
{
	//constants
	private final int density = GrowthCraftBees.getConfig().beeWorldGenDensity;

	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		for (int loop = 0; loop < this.density; ++loop)
		{
			final BlockPos lPos = pos.add(
				random.nextInt(8) - random.nextInt(8),
				random.nextInt(4) - random.nextInt(4),
				random.nextInt(8) - random.nextInt(8));

			final BlockBeeHive beeHive = GrowthCraftBees.blocks.beeHive.getBlock();
			if (world.isAirBlock(lPos) && beeHive.canBlockStay(world, lPos))
			{
				//				System.out.println(x + " " + y + " " + z);
				world.setBlockState(lPos, beeHive.getDefaultState(), BlockFlags.UPDATE_AND_SYNC);
			}
		}
		return true;
	}
}
