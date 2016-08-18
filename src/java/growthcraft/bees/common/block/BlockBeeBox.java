package growthcraft.bees.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.core.integration.minecraft.EnumMinecraftWoodType;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBeeBox extends GrcBlockContainer
{
	private int flammability;
	private int fireSpreadSpeed;

	public BlockBeeBox(Material material)
	{
		super(material);
		setTickRandomly(true);
		setHardness(2.5F);
		setStepSound(soundTypeWood);
		setUnlocalizedName("grc.bee_box.minecraft");
		setCreativeTab(GrowthCraftBees.tab);
		setTileEntityType(TileEntityBeeBox.class);
	}

	public BlockBeeBox()
	{
		this(Material.wood);
	}

	public String getMetaname(int meta)
	{
		if (meta >= 0 && meta < EnumMinecraftWoodType.VALUES.length)
		{
			return EnumMinecraftWoodType.VALUES[meta].name;
		}
		return "" + meta;
	}

	public BlockBeeBox setFlammability(int flam)
	{
		this.flammability = flam;
		return this;
	}

	public BlockBeeBox setFireSpreadSpeed(int speed)
	{
		this.fireSpreadSpeed = speed;
		return this;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return flammability;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return fireSpreadSpeed;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item block, CreativeTabs tab, List<ItemStack> list)
	{
		for (EnumMinecraftWoodType woodType : EnumMinecraftWoodType.VALUES)
		{
			list.add(new ItemStack(block, 1, woodType.meta));
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);
		final TileEntityBeeBox te = getTileEntity(world, pos);
		if (te != null) te.updateBlockTick();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.randomDisplayTick(world, pos, state, rand);
		if (rand.nextInt(24) == 0)
		{
			final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(pos);
			if (te != null)
			{
				if (te.hasBees())
				{
					world.playSound(
						(double)pos.getX() + 0.5D,
						(double)pos.getY() + 0.5D,
						(double)pos.getZ() + 0.5D,
						"grcbees:buzz",
						1.0F + rand.nextFloat(),
						0.3F + rand.nextFloat() * 0.7F,
						false);
				}
			}
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (super.onBlockActivated(world, pos, state, player, facing, hitX, hitY, hitZ)) return true;
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(pos);
			if (te != null)
			{
				player.openGui(GrowthCraftBees.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
			return false;
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState block)
	{
		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(pos);
		if (te != null)
		{
			for (int index = 0; index < te.getSizeInventory(); ++index)
			{
				final ItemStack stack = te.getStackInSlot(index);
				spawnAsEntity(world, pos, stack);
			}
			GrowthCraftBees.getLogger().warn("(fixme) BlockBeeBox#breakBlock updateComparatorOutputLevel");
			//world.updateComparatorOutputLevel(pos, par5);
		}
		super.breakBlock(world, pos, block);
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return EnumFacing.UP == side;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public int damageDropped(IBlockState state)
	{
		GrowthCraftBees.getLogger().warn("(fixme) BlockBeeBox#damageDropped");
		return 0;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
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

	/************
	 * BOXES
	 ************/
	@Override
	public void setBlockBoundsForItemRender()
	{
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB axis, List<AxisAlignedBB> list, Entity entity)
	{
		final float f = 0.0625F;
		// LEGS
		setBlockBounds(3*f, 0.0F, 3*f, 5*f, 3*f, 5*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		setBlockBounds(11*f, 0.0F, 3*f, 13*f, 3*f, 5*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		setBlockBounds(3*f, 0.0F, 11*f, 5*f, 3*f, 13*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		setBlockBounds(11*f, 0.0F, 11*f, 13*f, 3*f, 13*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		// BODY
		setBlockBounds(1*f, 3*f, 1*f, 15*f, 10*f, 15*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		// ROOF
		setBlockBounds(0.0F, 10*f, 0.0F, 1.0F, 13*f, 1.0F);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		setBlockBounds(2*f, 13*f, 2*f, 14*f, 1.0F, 14*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		setBlockBoundsForItemRender();
	}

	/************
	 * COMPARATOR
	 ************/
	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos)
	{
		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(pos);
		return te.countHoney() * 15 / 27;
	}
}
