package growthcraft.bees.common.world;

import java.util.Random;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.Utils;

import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGeneratorBees implements IWorldGenerator
{
	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		if (!world.getWorldInfo().getTerrainType().getWorldTypeName().startsWith("flat"))
		{
			final BlockPos pos = new BlockPos(
				chunkX * 16 + random.nextInt(16) + 8,
				random.nextInt(128),
				chunkZ * 16 + random.nextInt(16) + 8);
			boolean flag = true;
			if (GrowthCraftBees.getConfig().useBiomeDict)
			{
				final BiomeGenBase biome = world.getBiomeGenForCoords(pos);
				flag = (BiomeDictionary.isBiomeOfType(biome, Type.FOREST) ||
						BiomeDictionary.isBiomeOfType(biome, Type.PLAINS))
						&& !BiomeDictionary.isBiomeOfType(biome, Type.SNOWY);
			}
			else
			{
				flag = Utils.isIDInList(world.getBiomeGenForCoords(pos).biomeID, GrowthCraftBees.getConfig().beeBiomesList);
			}
			if (flag)
			{
				new WorldGenBeeHive().generate(world, random, pos);
			}
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.provider.getDimensionId() == 0)
		{
			generateSurface(world, random, chunkX, chunkZ);
		}
	}
}
