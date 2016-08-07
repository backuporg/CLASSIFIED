package growthcraft.bamboo.common.item;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.item.GrcItemFoodPlantBlock;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemBambooShoot extends GrcItemFoodPlantBlock
{
	public ItemBambooShoot()
	{
		super(GrowthCraftBamboo.blocks.bambooShoot.getBlock(), 4, 0.6F, false);
		setUnlocalizedName("grc.bamboo_shoot_food");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}
}
