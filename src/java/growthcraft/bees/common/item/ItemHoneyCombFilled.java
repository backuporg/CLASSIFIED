package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.item.GrcItemBase;

public class ItemHoneyCombFilled extends GrcItemBase
{
	public ItemHoneyCombFilled()
	{
		super();
		setUnlocalizedName("grc.honey_comb.filled");
		setCreativeTab(GrowthCraftBees.tab);
		setContainerItem(GrowthCraftBees.items.honeyCombEmpty.getItem());
	}
}
