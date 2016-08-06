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

		if (Blocks.snow_layer == block && (blockMeta & 7) < 1)
		{
			dir = EnumFacing.UP;
		}
		else
		{
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
			else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush)
			{
				pos.offset(dir, 1);
			}
		}

		if (!player.canPlayerEdit(pos, dir, stack))
		{
			return false;
		}
		else if (stack.stackSize == 0)
		{
			return false;
		}
		else
		{
			final Block block2 = GrowthCraftCore.blocks.ropeBlock.getBlock();
			if (world.canPlaceEntityOnSide(block2, pos, false, dir, (Entity)null, stack))
			{
				final int meta = block2.onBlockPlaced(world, pos, dir, hitX, hitY, hitZ, 0);
				if (world.setBlockState(pos, block2.getDefaultState(), meta, 3))
				{
					if (world.getBlockState(pos).getBlock() == block2)
					{
						block2.onBlockPlacedBy(world, pos, player, stack);
						block2.onPostBlockPlaced(world, pos, meta);
					}
					world.playSoundEffect(
						(double)pos.getX() + 0.5D,
						(double)pos.getY() + 0.5D,
						(double)pos.getZ() + 0.5D,
						block2.stepSound.getPlaceSound(),
						(block2.stepSound.getVolume() + 1.0F) / 2.0F,
						block2.stepSound.getPitch() * 0.8F);
					--stack.stackSize;
				}
			}

			return true;
		}
	}
}
