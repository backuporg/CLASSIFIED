package growthcraft.bamboo.common.item;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.item.GrcItemFoodPlantBlock;

public class ItemBambooShoot extends GrcItemFoodPlantBlock
{
	public ItemBambooShoot()
	{
		super(GrowthCraftBamboo.blocks.bambooShoot.getBlock(), 4, 0.6F, false);
		setUnlocalizedName("grc.bamboo_shoot_food");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}
}
