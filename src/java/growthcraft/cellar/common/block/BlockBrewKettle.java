package growthcraft.cellar.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.api.core.util.BBox;
import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.CellarGuiType;
import growthcraft.core.Utils;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBrewKettle extends BlockCellarContainer
{
	private final BBox kettleContentsBB = BBox.newCube(2, 4, 2, 12, 10, 12).scale(1f / 16f);
	private final boolean dropItemsInBrewKettle = GrowthCraftCellar.getConfig().dropItemsInBrewKettle;
	private final boolean setFireToFallenLivingEntities = GrowthCraftCellar.getConfig().setFireToFallenLivingEntities;
	private final int rainFillPerUnit = GrowthCraftCellar.getConfig().brewKettleRainFillPerUnit;

	public BlockBrewKettle()
	{
		super(Material.iron);
		setStepSound(soundTypeMetal);
		setTileEntityType(TileEntityBrewKettle.class);
		setHardness(2.0F);
		setUnlocalizedName("grc.brew_kettle");
		setCreativeTab(GrowthCraftCellar.tab);
		setGuiType(CellarGuiType.BREW_KETTLE);
	}

	@Override
	public void fillWithRain(World world, BlockPos pos)
	{
		if (GrowthCraftCellar.getConfig().brewKettleFillsWithRain)
		{
			final TileEntityBrewKettle te = getTileEntity(world, pos);
			if (te != null)
			{
				te.fill(EnumFacing.UP, new FluidStack(FluidRegistry.WATER, rainFillPerUnit), true);
			}
		}
		super.fillWithRain(world, pos);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity)
	{
		if (!world.isRemote)
		{
			final TileEntityBrewKettle te = getTileEntity(world, pos);
			if (te != null)
			{
				if (dropItemsInBrewKettle)
				{
					if (entity instanceof EntityItem)
					{
						final EntityItem item = (EntityItem)entity;
						if (te.tryMergeItemIntoMainSlot(item.getEntityItem()) != null)
						{
							world.playSoundEffect((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), "liquid.splash", 0.3f, 0.5f);
						}
					}
				}
				if (setFireToFallenLivingEntities)
				{
					if (entity instanceof EntityLivingBase)
					{
						if (te.getHeatMultiplier() >= 0.5f)
						{
							final float ex = (float)entity.posX - pos.getX();
							final float ey = (float)entity.posY - pos.getY();
							final float ez = (float)entity.posZ - pos.getZ();
							if (kettleContentsBB.contains(ex, ey, ez))
							{
								entity.setFire(1);
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected boolean playerDrainTank(World world, BlockPos pos, IFluidHandler fh, ItemStack is, EntityPlayer player)
	{
		final FluidStack fs = Utils.playerDrainTank(world, pos, fh, is, player);
		return fs != null && fs.amount > 0;
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

	@Override
	public void setBlockBoundsForItemRender()
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB axis, List<AxisAlignedBB> list, Entity entity)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		final float f = 0.125F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		this.setBlockBoundsForItemRender();
	}

	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos)
	{
		final TileEntityBrewKettle te = getTileEntity(world, pos);
		if (te != null)
		{
			return te.getFluidAmountScaled(15, 1);
		}
		return 0;
	}
}
