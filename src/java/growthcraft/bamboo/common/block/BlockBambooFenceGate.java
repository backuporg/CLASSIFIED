package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;

public class BlockBambooFenceGate extends BlockFenceGate
{
	public BlockBambooFenceGate()
	{
		super(BlockPlanks.EnumType.OAK);
		setStepSound(soundTypeWood);
		setHardness(2.0F);
		setResistance(5.0F);
		setUnlocalizedName("bamboo_fence_gate");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}
}
