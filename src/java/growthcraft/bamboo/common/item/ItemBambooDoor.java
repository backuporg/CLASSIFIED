package growthcraft.bamboo.common.item;

import java.util.List;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.item.GrcItemBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBambooDoor extends ItemDoor
{
	public ItemBambooDoor()
	{
		super(GrowthCraftBamboo.blocks.bambooDoor.getBlock());
		this.maxStackSize = 1;
		setUnlocalizedName("grc.bamboo_door");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		super.addInformation(stack, player, list, bool);
		GrcItemBase.addDescription(this, stack, player, list, bool);
	}
}
