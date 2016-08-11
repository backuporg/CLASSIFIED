package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.block.GrcBlockBase;

import net.minecraft.block.material.Material;

public class BlockBamboo extends GrcBlockBase
{
	public BlockBamboo()
	{
		super(Material.wood);
		setStepSound(soundTypeWood);
		setResistance(5.0F);
		setHardness(2.0F);
		setUnlocalizedName("bamboo_block");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}
}
