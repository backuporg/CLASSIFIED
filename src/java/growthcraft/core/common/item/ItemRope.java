package growthcraft.core.common.item;

import growthcraft.api.core.item.ItemKey;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.registry.FenceRopeRegistry.FenceRopeEntry;
import growthcraft.core.registry.FenceRopeRegistry;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemRope extends GrcPseudoItemBlock
{
	public ItemRope()
	{
		super();
		setUnlocalizedName("grc.rope");
		setCreativeTab(GrowthCraftCore.creativeTab);
	}

	@Override
	protected boolean prePlaceBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		final Block currentBlock = world.getBlockState(pos).getBlock();
		GrowthCraftCore.getLogger().warn("(fixme) ItemRope#onItemUse metadata");
		final int blockMeta = 0;
		final FenceRopeEntry entry = FenceRopeRegistry.instance().getEntry(currentBlock, blockMeta);
		if (entry != null)
		{
			if (stack.stackSize == 0)
			{
				return false;
			}
			else if (!player.canPlayerEdit(pos, dir, stack))
			{
				return false;
			}
			int targetMeta = entry.getFenceRopeBlockMetadata();
			if (targetMeta == ItemKey.WILDCARD_VALUE) targetMeta = blockMeta;
			world.setBlockState(pos, entry.getFenceRopeBlock().getDefaultState(), BlockFlags.UPDATE_AND_SYNC);
			--stack.stackSize;
			return true;
		}
		return false;
	}

	@Override
	protected Block getBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos)
	{
		return GrowthCraftCore.blocks.ropeBlock.getBlock();
	}
}
