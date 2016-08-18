package growthcraft.hops.common.item;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.BlockCheck;
import growthcraft.hops.GrowthCraftHops;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemHopSeeds extends GrcItemBase implements IPlantable
{
	public ItemHopSeeds()
	{
		super();
		this.setUnlocalizedName("grc.hop_seeds");
		this.setCreativeTab(GrowthCraftCore.creativeTab);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		if (stack.stackSize <= 0)
		{
			return false;
		}
		else if (dir != EnumFacing.UP)
		{
			return false;
		}
		else if (player.canPlayerEdit(pos, dir, stack) && player.canPlayerEdit(pos.up(), dir, stack))
		{
			if (BlockCheck.canSustainPlant(world, pos, EnumFacing.UP, GrowthCraftHops.blocks.hopVine.getBlock()) && BlockCheck.isRope(world, pos.up()))
			{
				world.setBlockState(pos.up(), GrowthCraftHops.blocks.hopVine.getBlock().getDefaultState(), BlockFlags.UPDATE_AND_SYNC);
				--stack.stackSize;
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return GrowthCraftHops.blocks.hopVine.getBlock().getDefaultState();
	}
}
