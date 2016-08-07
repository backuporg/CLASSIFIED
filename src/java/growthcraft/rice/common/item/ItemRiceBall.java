package growthcraft.rice.common.item;

import growthcraft.core.common.item.GrcItemFoodBase;
import growthcraft.core.GrowthCraftCore;

public class ItemRiceBall extends GrcItemFoodBase
{
	public ItemRiceBall()
	{
		super(5, 0.6F, false);
		this.setUnlocalizedName("grc.rice_ball");
		this.setCreativeTab(GrowthCraftCore.creativeTab);
	}
}
