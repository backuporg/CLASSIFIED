package growthcraft.cellar.common.item;

import growthcraft.core.common.item.GrcItemBase;

public class ItemChievDummy extends GrcItemBase
{
	public ItemChievDummy()
	{
		super();
		setCreativeTab(null);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setMaxDamage(0);
		setUnlocalizedName("grc.chiev_item_dummy");
	}
}
