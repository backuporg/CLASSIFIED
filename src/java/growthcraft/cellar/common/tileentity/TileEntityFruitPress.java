package growthcraft.cellar.common.tileentity;

import growthcraft.cellar.common.fluids.CellarTank;
import growthcraft.cellar.common.inventory.ContainerFruitPress;
import growthcraft.cellar.common.tileentity.device.FruitPress;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.ITileProgressiveDevice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityFruitPress extends TileEntityCellarDevice implements ITickable, ITileProgressiveDevice
{
	public static class FruitPressDataID
	{
		public static final int TIME = 0;
		public static final int TIME_MAX = 1;

		private FruitPressDataID() {}
	}

	private static final int[] allSlotIds = new int[] {0, 1};
	private static final int[] residueSlotIds = new int[] {0};
	private FruitPress fruitPress = new FruitPress(this, 0, 0, 1);

	@Override
	protected FluidTank[] createTanks()
	{
		final int maxCap = GrowthCraftCellar.getConfig().fruitPressMaxCap;
		return new FluidTank[] { new CellarTank(maxCap, this) };
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 2);
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.fruit_press";
	}

	@Override
	public float getDeviceProgress()
	{
		return fruitPress.getProgress();
	}

	@Override
	public int getDeviceProgressScaled(int scale)
	{
		return fruitPress.getProgressScaled(scale);
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			fruitPress.update();
		}
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		// 0 = raw item
		// 1 = residue
		return allSlotIds;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing side)
	{
		// allow the insertion of a raw item from ANY side
		if (index == 0) return true;
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing side)
	{
		// if this is the raw item slow
		if (index == 0)
		{
			// only allow extraction from the top
			if (side == EnumFacing.UP) return true;
		}
		// else this is the residue slot
		else
		{
			// extract from sides, or bottom
			if (side != EnumFacing.UP) return true;
		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if (nbt.getInteger("fruit_press_version") > 0)
		{
			fruitPress.readFromNBT(nbt, "fruit_press");
		}
		else
		{
			fruitPress.readFromNBT(nbt);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		fruitPress.writeToNBT(nbt, "fruit_press");
		nbt.setInteger("fruit_press_version", 2);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerFruitPress(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return "grccellar:fruit_press";
	}

	/**
	 * @param id - data id
	 * @param v - value
	 */
	@Override
	public void receiveGUINetworkData(int id, int v)
	{
		super.receiveGUINetworkData(id, v);
		switch (id)
		{
			case FruitPressDataID.TIME:
				fruitPress.setTime(v);
				break;
			case FruitPressDataID.TIME_MAX:
				fruitPress.setTimeMax(v);
				break;
			default:
				// should warn about invalid Data ID
				break;
		}
	}

	@Override
	public void sendGUINetworkData(Container container, ICrafting iCrafting)
	{
		super.sendGUINetworkData(container, iCrafting);
		iCrafting.sendProgressBarUpdate(container, FruitPressDataID.TIME, fruitPress.getTime());
		iCrafting.sendProgressBarUpdate(container, FruitPressDataID.TIME_MAX, fruitPress.getTimeMax());
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid)
	{
		return false;
	}

	@Override
	protected int doFill(EnumFacing from, FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	protected FluidStack doDrain(EnumFacing from, int maxDrain, boolean doDrain)
	{
		return drainFluidTank(0, maxDrain, doDrain);
	}

	@Override
	protected FluidStack doDrain(EnumFacing from, FluidStack stack, boolean doDrain)
	{
		if (stack == null || !stack.isFluidEqual(getFluidStack(0)))
		{
			return null;
		}
		return doDrain(from, stack.amount, doDrain);
	}
}
