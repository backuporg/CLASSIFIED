package growthcraft.bamboo.common.item;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.item.GrcPseudoItemBlock;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBamboo extends GrcPseudoItemBlock
{
	public ItemBamboo()
	{
		super();
		setUnlocalizedName("grc.bamboo");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	@Override
	protected Block getBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos)
	{
		return GrowthCraftBamboo.blocks.bambooStalk.getBlock();
	}
}
