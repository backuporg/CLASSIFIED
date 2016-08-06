package growthcraft.bees.common.item;

import growthcraft.bees.common.block.BlockBeeBox;
import growthcraft.core.common.item.GrcItemBlockBase;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/*
 * This ItemBlock is used for the vanilla wood variations of the BeeBox
 */
public class ItemBlockBeeBox extends GrcItemBlockBase
{
	public ItemBlockBeeBox(Block block)
	{
		super(block);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		if (block instanceof BlockBeeBox)
		{
			return super.getUnlocalizedName(stack) + "." + ((BlockBeeBox)block).getMetaname(stack.getItemDamage());
		}
		return super.getUnlocalizedName(stack) + stack.getItemDamage();
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}
}
