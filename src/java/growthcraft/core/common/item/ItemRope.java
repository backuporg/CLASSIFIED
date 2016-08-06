package growthcraft.core.common.item;

import growthcraft.api.core.item.ItemKey;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.registry.FenceRopeRegistry.FenceRopeEntry;
import growthcraft.core.registry.FenceRopeRegistry;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemRope extends GrcItemBase
{
	public ItemRope()
	{
		super();
		setUnlocalizedName("grc.rope");
		setCreativeTab(GrowthCraftCore.creativeTab);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		final Block block = world.getBlockState(pos).getBlock();
		GrowthCraftCore.getLogger().warn("(fixme) ItemRope#onItemUse metadata");
		final int blockMeta = 0;

		if (!block.isReplaceable(world, pos))
        {
            pos = pos.offset(dir);
        }

		final FenceRopeEntry entry = FenceRopeRegistry.instance().getEntry(block, blockMeta);
		if (entry != null)
		{
			if (!player.canPlayerEdit(pos, dir, stack))
			{
				return false;
			}
			else if (stack.stackSize == 0)
			{
				return false;
			}

			int targetMeta = entry.getFenceRopeBlockMetadata();
			if (targetMeta == ItemKey.WILDCARD_VALUE) targetMeta = blockMeta;

			world.setBlockState(pos, entry.getFenceRopeBlock().getDefaultState(), BlockFlags.UPDATE_AND_SYNC);
			--stack.stackSize;
			return true;
		}

		if (stack.stackSize == 0)
		{
			return false;
		}
		else if (!player.canPlayerEdit(pos, dir, stack))
		{
			return false;
		}
		else
		{
			final Block ropeBlock = GrowthCraftCore.blocks.ropeBlock.getBlock();
			if (world.canBlockBePlaced(ropeBlock, pos, false, dir, (Entity)null, stack))
			{
				final int meta = ropeBlock.onBlockPlaced(world, pos, dir, hitX, hitY, hitZ, 0);
				if (world.setBlockState(pos, ropeBlock.getDefaultState(), meta, 3))
				{
					if (world.getBlockState(pos).getBlock() == ropeBlock)
					{
						ropeBlock.onBlockPlacedBy(world, pos, state, player, stack);
						ropeBlock.onPostBlockPlaced(world, pos, meta);
					}
					world.playSoundEffect(
						(double)pos.getX() + 0.5D,
						(double)pos.getY() + 0.5D,
						(double)pos.getZ() + 0.5D,
						ropeBlock.stepSound.getPlaceSound(),
						(ropeBlock.stepSound.getVolume() + 1.0F) / 2.0F,
						ropeBlock.stepSound.getPitch() * 0.8F);
					--stack.stackSize;
				}
			}

			return true;
		}
	}
}
