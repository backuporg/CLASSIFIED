package growthcraft.bamboo.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.core.util.BlockCheck;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBambooStalk extends GrcBlockBase implements IGrowable
{
	public static final PropertyBool DRIED = PropertyBool.create("dried");
	private final int growth = GrowthCraftBamboo.getConfig().bambooStalkGrowthRate;

	public BlockBambooStalk()
	{
		super(Material.wood);
		setStepSound(soundTypeWood);
		setHardness(2.0F);
		setTickRandomly(true);
		setUnlocalizedName("bamboo_stalk");
		setCreativeTab(null);
		setDefaultState(blockState.getBaseState().withProperty(DRIED, false));
	}

	@Override
	@SuppressWarnings({"rawtypes"})
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {DRIED});
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(DRIED, meta > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(DRIED) ? 8 : 0;
	}

	public boolean isDried(IBlockState state)
	{
		return state.getValue(DRIED);
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public int tickRate(World world)
	{
		return 5;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);
		if (state.getValue(DRIED)) return;
		if (!isBambooOnGround(world, pos)) return;
		if (rand.nextInt(this.growth) != 0) return;

		final BlockBambooShoot bambooShoot = GrowthCraftBamboo.blocks.bambooShoot.getBlock();

		final byte b = 9;
		int amount = 10;
		final BlockPos.MutableBlockPos basePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
		final BlockPos.MutableBlockPos lPos = new BlockPos.MutableBlockPos(0, 0, 0);

		for (int x1 = pos.getX() - b; x1 <= pos.getX() + b; ++x1)
		{
			for (int z1 = pos.getZ() - b; z1 <= pos.getZ() + b; ++z1)
			{
				for (int y1 = pos.getY() - 1; y1 <= pos.getY() + 1; ++y1)
				{
					lPos.set(x1, y1, z1);
					final boolean flag1 = world.getBlockState(lPos).getBlock() == this && isBambooOnGround(world, lPos);
					final boolean flag2 = world.getBlockState(lPos).getBlock() == bambooShoot;
					if (flag1 || flag2)
					{
						--amount;
						if (amount <= 0)
						{
							return;
						}
					}
				}
			}
		}

		lPos.set(
			pos.getX() + rand.nextInt(3) - 1,
			pos.getY() + rand.nextInt(2) - rand.nextInt(2),
			pos.getZ() + rand.nextInt(3) - 1
		);

		for (int loop = 0; loop < 4; ++loop)
		{
			if (world.isAirBlock(lPos) && bambooShoot.canBlockStay(world, lPos))
			{
				basePos.set(lPos.getX(), lPos.getY(), lPos.getZ());
			}

			lPos.set(
				basePos.getX() + rand.nextInt(3) - 1,
				basePos.getY() + rand.nextInt(2) - rand.nextInt(2),
				basePos.getZ() + rand.nextInt(3) - 1);
		}

		if (world.isAirBlock(lPos) && bambooShoot.canBlockStay(world, lPos))
		{
			world.setBlockState(lPos, bambooShoot.getDefaultState(), BlockFlags.UPDATE_AND_SYNC);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block)
	{
		boolean flag = false;

		if (world.getBlockState(pos.down()).getBlock() != this)
		{
			if (!isBambooOnGround(world, pos))
			{
				flag = true;
			}
		}

		if (flag && !isDried(state))
		{
			fellBlockAsItem(world, pos, state);
		}

		super.onNeighborBlockChange(world, pos, state, block);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if (isDried(state)) return;
		final int offset = 4;
		final int wideOffset = offset + 1;
		if (world.isAreaLoaded(pos.add(-wideOffset, -wideOffset, -wideOffset), pos.add(wideOffset, wideOffset, wideOffset)))
		{
			for (int x1 = -offset; x1 <= offset; ++x1)
			{
				for (int y1 = -offset; y1 <= offset; ++y1)
				{
					for (int z1 = -offset; z1 <= offset; ++z1)
					{
						final BlockPos lPos = pos.add(x1, y1, z1);
						final IBlockState leafState = world.getBlockState(lPos);
						if (leafState != null && leafState.getBlock() != null)
						{
							leafState.getBlock().beginLeavesDecay(world, lPos);
						}
					}
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftBamboo.items.bamboo.getItem();
	}

	@Override
	public boolean canSustainLeaves(IBlockAccess world, BlockPos pos)
	{
		return !isDried(world.getBlockState(pos));
	}

	@Override
	public boolean isWood(IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	public boolean isBambooOnGround(World world, BlockPos pos)
	{
		if (!BlockCheck.canSustainPlant(world, pos.down(), EnumFacing.UP, GrowthCraftBamboo.blocks.bambooShoot.getBlock())) return false;
		return this == world.getBlockState(pos).getBlock();
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	private boolean canFence(IBlockAccess world, BlockPos pos)
	{
		final IBlockState state = world.getBlockState(pos);
		final Block block = state.getBlock();
		return block instanceof BlockFence || block instanceof BlockFenceGate;
	}

	private boolean canWall(IBlockAccess world, BlockPos pos)
	{
		return GrowthCraftBamboo.blocks.bambooWall.getBlock().isAssociatedBlock(world.getBlockState(pos).getBlock());
	}

	private boolean canDoor(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock() instanceof BlockDoor;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return GrowthCraftBamboo.items.bamboo.getItem();
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		GrowthCraftBamboo.getLogger().warn("(fixme) BlockBambooStalk#grow");
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
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		final double d0 = 0.5D;
		final double d1 = 1.0D;
		return ColorizerGrass.getGrassColor(d0, d1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
	{
		//return par1 == 0 ? 0xFFFFFF : ColorizerFoliage.getFoliageColorBasic();
		return 0xFFFFFF;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, BlockPos pos, int renderPass)
	{
		if (!isDried(world.getBlockState(pos)))
		{
			int r = 0;
			int g = 0;
			int b = 0;
			for (int l1 = -1; l1 <= 1; ++l1)
			{
				for (int i2 = -1; i2 <= 1; ++i2)
				{
					final BlockPos p = pos.add(i2, 0, l1);
					final int color = world.getBiomeGenForCoords(p).getFoliageColorAtPos(p);
					r += (color & 16711680) >> 16;
					g += (color & 65280) >> 8;
					b += color & 255;
				}
			}
			return (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
		}
		else
		{
			return 0xFFFFFF;
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		float x1 = 0.25F;
		float x2 = 0.75F;
		float z1 = 0.25F;
		float z2 = 0.75F;
		if (!isDried(world.getBlockState(pos)))
		{
			final BlockPos north = pos.north();
			final BlockPos south = pos.south();
			final BlockPos east = pos.east();
			final BlockPos west = pos.west();
			if (this.canFence(world, north) || this.canWall(world, north) || this.canDoor(world, north))
			{
				z1 = 0.0F;
			}

			if (this.canFence(world, south) || this.canWall(world, south) || this.canDoor(world, south))
			{
				z2 = 1.0F;
			}

			if (this.canFence(world, west) || this.canWall(world, west) || this.canDoor(world, west))
			{
				x1 = 0.0F;
			}

			if (this.canFence(world, east) || this.canWall(world, east) || this.canDoor(world, east))
			{
				x2 = 1.0F;
			}
		}
		this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB axis, List<AxisAlignedBB> list, Entity entity)
	{
		final float x1 = 0.25F;
		final float x2 = 0.75F;
		final float z1 = 0.25F;
		final float z2 = 0.75F;
		this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		if (isDried(state))
		{
			if (this.canFence(world, pos.north()))
			{
				renderFence(world, axis, list, entity, pos, state, EnumFacing.NORTH);
			}
			else if (this.canWall(world, pos.north()))
			{
				renderWall(world, axis, list, entity, pos, state, EnumFacing.NORTH);
			}
			else if (this.canDoor(world, pos.north()))
			{
				renderDoor(world, axis, list, entity, pos, state, EnumFacing.NORTH);
			}

			if (this.canFence(world, pos.south()))
			{
				renderFence(world, axis, list, entity, pos, state, EnumFacing.SOUTH);
			}
			else if (this.canWall(world, pos.south()))
			{
				renderWall(world, axis, list, entity, pos, state, EnumFacing.SOUTH);
			}
			else if (this.canDoor(world, pos.south()))
			{
				renderDoor(world, axis, list, entity, pos, state, EnumFacing.SOUTH);
			}

			if (this.canFence(world, pos.west()))
			{
				renderFence(world, axis, list, entity, pos, state, EnumFacing.WEST);
			}
			else if (this.canWall(world, pos.west()))
			{
				renderWall(world, axis, list, entity, pos, state, EnumFacing.WEST);
			}
			else if (this.canDoor(world, pos.west()))
			{
				renderDoor(world, axis, list, entity, pos, state, EnumFacing.WEST);
			}

			if (this.canFence(world, pos.east()))
			{
				renderFence(world, axis, list, entity, pos, state, EnumFacing.EAST);
			}
			else if (this.canWall(world, pos.east()))
			{
				renderWall(world, axis, list, entity, pos, state, EnumFacing.EAST);
			}
			else if (this.canDoor(world, pos.east()))
			{
				renderDoor(world, axis, list, entity, pos, state, EnumFacing.EAST);
			}
		}
		this.setBlockBoundsBasedOnState(world, pos);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void renderFence(World world, AxisAlignedBB axis, List list, Entity entity, BlockPos pos, IBlockState state, EnumFacing m)
	{
		float x1 = pos.getX();
		float x2 = pos.getX() + 1.0F;
		float z1 = pos.getZ();
		float z2 = pos.getZ() + 1.0F;
		float y1 = 0.75F;
		float y2 = 0.9375F;

		if (m == EnumFacing.NORTH)
		{
			x1 = 0.4375F;
			x2 = 0.5625F;
			z1 = 0.0F;
			z2 = 0.25F;
		}
		else if (m == EnumFacing.SOUTH)
		{
			x1 = 0.4375F;
			x2 = 0.5625F;
			z1 = 0.75F;
			z2 = 1.0F;
		}
		else if (m == EnumFacing.WEST)
		{
			z1 = 0.4375F;
			z2 = 0.5625F;
			x1 = 0.0F;
			x2 = 0.25F;
		}
		else if (m == EnumFacing.EAST)
		{
			z1 = 0.4375F;
			z2 = 0.5625F;
			x1 = 0.75F;
			x2 = 1.0F;
		}

		this.setBlockBounds(x1, y1, z1, x2, y2, z2);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);

		y1 = 0.375F;
		y2 = 0.5625F;

		if (m == EnumFacing.NORTH)
		{
			x1 = 0.4375F;
			x2 = 0.5625F;
			z1 = 0.0F;
			z2 = 0.25F;
		}
		else if (m == EnumFacing.SOUTH)
		{
			x1 = 0.4375F;
			x2 = 0.5625F;
			z1 = 0.75F;
			z2 = 1.0F;
		}
		else if (m == EnumFacing.WEST)
		{
			z1 = 0.4375F;
			z2 = 0.5625F;
			x1 = 0.0F;
			x2 = 0.25F;
		}
		else if (m == EnumFacing.EAST)
		{
			z1 = 0.4375F;
			z2 = 0.5625F;
			x1 = 0.75F;
			x2 = 1.0F;
		}

		this.setBlockBounds(x1, y1, z1, x2, y2, z2);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void renderWall(World world, AxisAlignedBB axis, List list, Entity entity, BlockPos pos, IBlockState state, EnumFacing facing)
	{
		float x1 = pos.getX();
		float x2 = pos.getX() + 1.0F;
		float z1 = pos.getZ();
		float z2 = pos.getZ() + 1.0F;
		final double y1 = 0.0F;
		final double y2 = 1.0F;
		if (facing == EnumFacing.NORTH)
		{
			x1 = 0.375F;
			x2 = 0.625F;
			z1 = 0.0F;
			z2 = 0.25F;
		}
		else if (facing == EnumFacing.SOUTH)
		{
			x1 = 0.375F;
			x2 = 0.625F;
			z1 = 0.75F;
			z2 = 1.0F;
		}
		else if (facing == EnumFacing.WEST)
		{
			z1 = 0.375F;
			z2 = 0.625F;
			x1 = 0.0F;
			x2 = 0.25F;
		}
		else if (facing == EnumFacing.EAST)
		{
			z1 = 0.375F;
			z2 = 0.625F;
			x1 = 0.75F;
			x2 = 1.0F;
		}
		this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void renderDoor(World world, AxisAlignedBB axis, List list, Entity entity, BlockPos pos, IBlockState state, EnumFacing m)
	{
		GrowthCraftBamboo.getLogger().warn("(fixme) BlockBambooStalk#renderDoor");
		/*float x1 = pos.getX();
		float x2 = pos.getX() + 1.0F;
		float z1 = pos.getZ();
		float z2 = pos.getZ() + 1.0F;
		final float y1 = 0.0F;
		final float y2 = 1.0F;
		int tm0;
		int tm;
		if (m == EnumFacing.NORTH)
		{
			tm0 = world.getBlockMetadata(x, y, z - 1);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockMetadata(x, y - 1, z - 1);
			}
			tm = tm0 & 3;
			if (tm == 0)
			{
				x1 = 0.0F;
				x2 = 0.375F;
				z1 = 0.0F;
				z2 = 0.25F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}

			if (tm == 2)
			{
				x1 = 0.625F;
				x2 = 1.0F;
				z1 = 0.0F;
				z2 = 0.25F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}
		}
		else if (m == EnumFacing.SOUTH)
		{
			tm0 = world.getBlockMetadata(x, y, z + 1);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockMetadata(x, y - 1, z + 1);
			}

			tm = tm0 & 3;
			if (tm == 0)
			{
				x1 = 0.0F;
				x2 = 0.375F;
				z1 = 0.75F;
				z2 = 1.0F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}

			if (tm == 2)
			{
				x1 = 0.625F;
				x2 = 1.0F;
				z1 = 0.75F;
				z2 = 1.0F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}
		}
		else if (m == EnumFacing.WEST)
		{
			tm0 = world.getBlockMetadata(x - 1, y, z);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockMetadata(x - 1, y - 1, z);
			}
			tm = tm0 & 3;
			if (tm == 1)
			{

				x1 = 0.0F;
				x2 = 0.25F;
				z1 = 0.0F;
				z2 = 0.375F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}

			if (tm == 3)
			{

				x1 = 0.0F;
				x2 = 0.25F;
				z1 = 0.625F;
				z2 = 1.0F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}
		}
		else if (m == EnumFacing.EAST)
		{
			tm0 = world.getBlockMetadata(x + 1, y, z);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockMetadata(x + 1, y - 1, z);
			}
			tm = tm0 & 3;
			if (tm == 1)
			{
				x1 = 0.75F;
				x2 = 1.0F;
				z1 = 0.0F;
				z2 = 0.375F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}

			if (tm == 3)
			{
				x1 = 0.75F;
				x2 = 1.0F;
				z1 = 0.625F;
				z2 = 1.0F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}
		}*/
	}
}
