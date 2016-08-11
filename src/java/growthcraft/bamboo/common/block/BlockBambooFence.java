package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockBambooFence extends BlockFence
{
	public BlockBambooFence()
	{
		super(Material.wood, MapColor.woodColor);
		this.useNeighborBrightness = true;
		setStepSound(soundTypeWood);
		setResistance(5.0F);
		setHardness(2.0F);
		setCreativeTab(GrowthCraftBamboo.creativeTab);
		setUnlocalizedName("bamboo_fence");
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.UP;
	}

	@Override
	public boolean canConnectTo(IBlockAccess world, BlockPos pos)
	{
		final IBlockState state = world.getBlockState(pos);
		final Block block = state.getBlock();

		if (this == block ||
			(block instanceof BlockFence) ||
			(block instanceof BlockFenceGate) ||
			GrowthCraftBamboo.blocks.bambooWall.isSameAs(block) ||
			GrowthCraftBamboo.blocks.bambooStalk.isSameAs(block))
		{
			return true;
		}
		else
		{
			if (block != null && block.getMaterial().isOpaque())
			{
				return block.getMaterial() != Material.gourd;
			}
		}
		return false;
	}
}
