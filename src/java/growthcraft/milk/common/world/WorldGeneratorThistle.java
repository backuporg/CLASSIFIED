package growthcraft.milk.common.world;

import java.util.Random;

import growthcraft.api.core.util.BiomeUtils;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.milk.GrowthCraftMilk;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * Created by Firedingo on 25/02/2016.
 */
public class WorldGeneratorThistle implements IWorldGenerator
{
	private WorldGenerator thistle;

	private boolean canPlaceOnBlock(World world, BlockPos pos, IBlockState state)
	{
		final Block block = state.getBlock();
		return Blocks.dirt.equals(block) ||
			Blocks.grass.equals(block);
	}

	private boolean canReplaceBlock(World world, BlockPos pos, IBlockState state)
	{
		final Block block = state.getBlock();
		return block.isAir(world, pos) ||
			block.isLeaves(world, pos) ||
			Blocks.vine == block;
	}

	private void genRandThistle(WorldGenerator generator, World world, Random rand, int chunk_x, int chunk_z, int maxToSpawn, int minHeight, int maxHeight)
	{
		final int genChance = GrowthCraftMilk.getConfig().thistleGenChance;
		for (int i = 0; i < maxToSpawn; ++i)
		{
			if (genChance > 0)
			{
				if (rand.nextInt(genChance) != 0) continue;
			}
			BlockPos pos = new BlockPos(chunk_x * 16 + rand.nextInt(16), maxHeight, chunk_z * 16 + rand.nextInt(16));
			for (; pos.getY() > minHeight; pos = pos.down())
			{
				// If you can't replace the block now, it means you probably
				// hit the floor
				if (!canReplaceBlock(world, pos, world.getBlockState(pos)))
				{
					// move back up and break loop
					pos = pos.up();
					break;
				}
			}
			// If we've exceeded the minHeight, bail this operation immediately
			if (pos.getY() <= minHeight)
			{
				continue;
			}
			final IBlockState state = world.getBlockState(pos.down());
			if (canPlaceOnBlock(world, pos.down(), state))
			{
				world.setBlockState(pos, GrowthCraftMilk.blocks.thistle.getBlock().getDefaultState());
			}
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.provider.getDimensionId() == 0)
		{
			final BlockPos pos = new BlockPos(chunkX, 0, chunkZ);
			final BiomeGenBase biome = world.getBiomeGenForCoords(pos);
			if (GrowthCraftMilk.getConfig().thistleUseBiomeDict)
			{
				if (!BiomeUtils.testBiomeTypeTagsTable(biome, GrowthCraftMilk.getConfig().thistleBiomesTypeList)) return;
			}
			else
			{
				final String biomeId = "" + biome.biomeID;
				if (!BiomeUtils.testBiomeIdTags(biomeId, GrowthCraftMilk.getConfig().thistleBiomesIdList)) return;
			}
			genRandThistle(thistle, world, random, chunkX, chunkZ, GrowthCraftMilk.getConfig().thistleGenAmount, 64, 255);
		}
	}
}
