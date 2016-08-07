package growthcraft.bamboo.common.world;

import java.util.Random;

import growthcraft.api.core.util.BiomeUtils;
import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGeneratorBamboo implements IWorldGenerator
{
	private final int rarity = GrowthCraftBamboo.getConfig().bambooWorldGenRarity;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.provider.getDimensionId() == 0)
		{
			generateSurface(world, random, chunkX, chunkZ);
		}
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		if (!world.getWorldInfo().getTerrainType().getWorldTypeName().startsWith("flat"))
		{
			final BlockPos pos = new BlockPos(
				chunkX * 16 + random.nextInt(16) + 8,
				random.nextInt(128),
				chunkZ * 16 + random.nextInt(16) + 8);
			final BiomeGenBase biome = world.getBiomeGenForCoords(pos);
			if (GrowthCraftBamboo.getConfig().useBiomeDict)
			{
				if (!BiomeUtils.testBiomeTypeTagsTable(biome, GrowthCraftBamboo.getConfig().bambooBiomesTypeList)) return;
			}
			else
			{
				final String biomeId = "" + biome.biomeID;
				if (!BiomeUtils.testBiomeIdTags(biomeId, GrowthCraftBamboo.getConfig().bambooBiomesIdList)) return;
			}

			if (random.nextInt(this.rarity) == 0)
			{
				new WorldGenBamboo(true).generateClumps(world, random, pos);
			}
		}
	}
}
