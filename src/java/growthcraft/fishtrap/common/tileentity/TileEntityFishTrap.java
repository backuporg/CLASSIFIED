package growthcraft.fishtrap.common.tileentity;

import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.GrcTileEntityInventoryBase;
import growthcraft.fishtrap.common.inventory.ContainerFishTrap;
import growthcraft.core.common.inventory.InventoryProcessor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFishTrap extends GrcTileEntityInventoryBase implements IInventory
{
	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 5);
	}

	public void addStack(ItemStack stack)
	{
		InventoryProcessor.instance().mergeWithSlots(this, stack);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerFishTrap(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return "grcfishtrap:fish_trap";
	}
}
