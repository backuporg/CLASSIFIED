package growthcraft.bees.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.block.GrcBlockBase;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBeeHive extends GrcBlockBase
{
	public BlockBeeHive()
	{
		super(Material.plants);
		this.setHardness(0.6F);
		this.setStepSound(soundTypeGrass);
		this.setUnlocalizedName("grc.bee_hive");
		this.setCreativeTab(GrowthCraftBees.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.randomDisplayTick(world, pos, state, random);
		if (random.nextInt(24) == 0)
		{
			world.playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F),
				"grcbees:buzz", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
		}
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);
		this.setDefaultDirection(world, x, y, z);
	}

	private void setDefaultDirection(World world, BlockPos pos)
	{
		if (!world.isRemote)
		{
			final Block zneg = world.getBlock(x, y, z - 1);
			final Block zpos = world.getBlock(x, y, z + 1);
			final Block xneg = world.getBlock(x - 1, y, z);
			final Block xpos = world.getBlock(x + 1, y, z);
			byte b0 = 3;

			if (zneg.func_149730_j() && !zpos.func_149730_j())
			{
				b0 = 3;
			}

			if (zpos.func_149730_j() && !zneg.func_149730_j())
			{
				b0 = 2;
			}

			if (xneg.func_149730_j() && !xpos.func_149730_j())
			{
				b0 = 5;
			}

			if (xpos.func_149730_j() && !xneg.func_149730_j())
			{
				b0 = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, b0, BlockFlags.SYNC);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
	{
		super.onBlockPlacedBy(world, pos, state, entity, stack);
		final int face = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (face == 0)
		{
			world.setBlockMetadataWithNotify(pos, 2, BlockFlags.SYNC);
		}

		if (face == 1)
		{
			world.setBlockMetadataWithNotify(pos, 5, BlockFlags.SYNC);
		}

		if (face == 2)
		{
			world.setBlockMetadataWithNotify(pos, 3, BlockFlags.SYNC);
		}

		if (face == 3)
		{
			world.setBlockMetadataWithNotify(pos, 4, BlockFlags.SYNC);
		}
	}

	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block)
	{
		super.onNeighborBlockChange(world, pos, state, block);
		if (!this.canBlockStay(world, pos))
		{
			fellBlockAsItem(world, pos, state);
		}
	}

	public boolean canBlockStay(World world, BlockPos pos)
	{
		if (world.isAirBlock(pos.up()))
		{
			return false;
		}
		return world.getBlockState(pos.up()).getBlock().isLeaves(world, pos.up());
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return GrowthCraftBees.items.bee.getItem();
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return rand.nextInt(8);
	}

	@Override
	public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune)
	{
		super.dropBlockAsItemWithChance(world, pos, state, chance, fortune);
		if (!world.isRemote)
		{
			final int max = world.rand.nextInt(8);
			if (max > 0)
			{
				for (int i = 0; i < max; i++)
				{
					if (world.rand.nextInt(2) == 0)
					{
						spawnAsEntity(world, pos, GrowthCraftBees.items.honeyCombEmpty.asStack());
					}
					else
					{
						spawnAsEntity(world, pos, GrowthCraftBees.items.honeyCombFilled.asStack());
					}
				}
			}
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		return true;
	}

	@Override
	public void setBlockBoundsForItemRender()
	{
		final float f = 0.0625F;
		this.setBlockBounds(2*f, 0.0F, 2*f, 14*f, 1.0F, 14*f);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB axis, List<AxisAlignedBB> list, Entity entity)
	{
		final float f = 0.0625F;
		this.setBlockBounds(4*f, 0.0F, 4*f, 12*f, 14*f, 12*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		this.setBlockBounds(3*f, 1*f, 3*f, 13*f, 13*f, 13*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		this.setBlockBounds(2*f, 4*f, 2*f, 14*f, 10*f, 14*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		this.setBlockBounds(7*f, 14*f, 7*f, 9*f, 1.0F, 9*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		this.setBlockBoundsForItemRender();
	}
}
