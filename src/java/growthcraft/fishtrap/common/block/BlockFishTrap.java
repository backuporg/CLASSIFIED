package growthcraft.fishtrap.common.block;

import java.util.Random;

import growthcraft.api.fishtrap.FishTrapRegistry;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.BlockCheck;
import growthcraft.core.Utils;
import growthcraft.fishtrap.common.tileentity.TileEntityFishTrap;
import growthcraft.fishtrap.GrowthCraftFishTrap;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;

public class BlockFishTrap extends GrcBlockContainer
{
	private final float chance = GrowthCraftFishTrap.getConfig().fishTrapCatchRate;

	public BlockFishTrap()
	{
		super(Material.wood);
		setTickRandomly(true);
		setHardness(0.4F);
		setStepSound(soundTypeWood);
		setUnlocalizedName("grc.fish_trap");
		setTileEntityType(TileEntityFishTrap.class);
		setCreativeTab(GrowthCraftCore.creativeTab);
	}

	private boolean isWater(IBlockState state)
	{
		return BlockCheck.isWater(state);
	}

	private float getCatchRate(World world, BlockPos pos)
	{
		final int checkSize = 3;
		final int halfSize = (checkSize - 1) / 2;
		final BlockPos basePos = pos.add(-halfSize, -halfSize, -halfSize);
		float f = 1.0F;
		for (int loopy = 0; loopy <= checkSize; loopy++)
		{
			for (int loopx = 0; loopx <= checkSize; loopx++)
			{
				for (int loopz = 0; loopz <= checkSize; loopz++)
				{
					final IBlockState state = world.getBlockState(basePos.add(loopx, loopy, loopz));
					float f1 = 0.0F;
					//1.038461538461538;
					if (state != null && isWater(state))
					{
						//f1 = 1.04F;
						f1 = 3.0F;
						//f1 = 17.48F;
					}
					f1 /= 4.0F;
					f += f1;
				}
			}
		}
		return f;
	}

	private void doCatch(World world, BlockPos pos, Random random, TileEntityFishTrap te, boolean debugFlag)
	{
		float f = this.getCatchRate(world, pos);
		boolean flag;
		if (GrowthCraftFishTrap.getConfig().useBiomeDict)
		{
			final BiomeGenBase biome = world.getBiomeGenForCoords(pos);
			flag = BiomeDictionary.isBiomeOfType(biome, Type.WATER);
		}
		else
		{
			flag = Utils.isIDInList(world.getBiomeGenForCoords(pos).biomeID, GrowthCraftFishTrap.getConfig().biomesList);
		}
		if (flag)
		{
			f *= 1 + (75 / 100);
		}
		if (random.nextInt((int)(this.chance / f) + 1) == 0 || debugFlag)
		{
			final ItemStack item = pickCatch(world);
			if (item != null)
			{
				te.addStack(item);
			}
		}
	}

	private ItemStack pickCatch(World world)
	{
		float f1 = world.rand.nextFloat();
		final float f2 = 0.1F;
		final float f3 = 0.05F;
		if (f1 < f2)
		{
			return FishTrapRegistry.instance().getJunkList(world);
		}
		f1 -= f2;
		if (f1 < f3)
		{
			return FishTrapRegistry.instance().getTreasureList(world);
		}
		f1 -= f3;
		return FishTrapRegistry.instance().getFishList(world);
	}

	private boolean canCatch(World world, BlockPos pos)
	{
		return isWater(world.getBlockState(pos.north())) ||
			isWater(world.getBlockState(pos.south())) ||
			isWater(world.getBlockState(pos.west())) ||
			isWater(world.getBlockState(pos.east()));
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);
		final TileEntityFishTrap te = getTileEntity(world, pos);
		if (te != null)
		{
			if (canCatch(world, pos))
			{
				doCatch(world, pos, rand, te, false);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			player.openGui(GrowthCraftFishTrap.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos)
	{
		final TileEntityFishTrap te = getTileEntity(world, pos);
		if (te != null)
		{
			return Container.calcRedstoneFromInventory(te);
		}
		return 0;
	}
}
