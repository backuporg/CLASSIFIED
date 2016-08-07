package growthcraft.apples.common.item;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.core.common.item.GrcPseudoItemBlock;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemAppleSeeds extends GrcPseudoItemBlock implements IPlantable
{
	public ItemAppleSeeds()
	{
		super();
		setUnlocalizedName("grc.apple_seeds");
		setCreativeTab(GrowthCraftCore.creativeTab);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return GrowthCraftApples.appleSapling.getBlock().getDefaultState();
	}

	@Override
	protected Block getBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos)
	{
		return getPlant(world, pos).getBlock();
	}
}
