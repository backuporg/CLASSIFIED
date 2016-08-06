package growthcraft.bamboo.common.item;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.item.GrcItemBase;

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

public class ItemBamboo extends GrcItemBase
{
	public ItemBamboo()
	{
		super();
		setUnlocalizedName("grc.bamboo");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		final Block block1 = world.getBlock(pos);

        if (!block.isReplaceable(world, pos))
        {
            pos = pos.offset(dir);
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
			final Block block = GrowthCraftBamboo.blocks.bambooStalk.getBlock();
			if (world.canBlockBePlaced(block, pos, false, dir, (Entity)null, stack))
			{
				if (world.setBlock(pos, block, 1, 3))
				{
					if (world.getBlock(pos) == block)
					{
						block.onBlockPlacedBy(world, pos, player, stack);
						block.onPostBlockPlaced(world, pos, 1);
					}

					world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
					--stack.stackSize;
				}
			}

			return true;
		}
	}
}
