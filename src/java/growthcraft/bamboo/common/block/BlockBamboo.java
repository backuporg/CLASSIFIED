package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.block.GrcBlockBase;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockBamboo extends GrcBlockBase
{
	public BlockBamboo()
	{
		super(Material.wood);
		setStepSound(soundTypeWood);
		setResistance(5.0F);
		setHardness(2.0F);
		setCreativeTab(GrowthCraftBamboo.creativeTab);
		setBlockName("grc.bambooBlock");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.blockIcon = reg.registerIcon("grcbamboo:block");
	}
}
