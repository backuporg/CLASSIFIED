/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.core.common.inventory;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

public class InventorySlice implements IInventory
{
	private IInventory parent;
	private int[] accesible;
	private int maxSize;

	public InventorySlice(@Nonnull IInventory par, @Nonnull int[] acc)
	{
		this.parent = par;
		this.accesible = acc;
		this.maxSize = accesible.length;
	}

	@Override
	public int getSizeInventory()
	{
		return maxSize;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return parent.getStackInSlot(accesible[index]);
	}

	@Override
	public ItemStack decrStackSize(int index, int amount)
	{
		return parent.decrStackSize(accesible[index], amount);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return parent.removeStackFromSlot(accesible[index]);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		parent.setInventorySlotContents(accesible[index], stack);
	}

	@Override
	public void clear()
	{
		for (int index : accesible)
		{
			parent.setInventorySlotContents(index, null);
		}
	}

	@Override
	public String getName()
	{
		return parent.getName();
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return parent.getDisplayName();
	}

	@Override
	public boolean hasCustomName()
	{
		return parent.hasCustomName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return parent.getInventoryStackLimit();
	}

	@Override
	public void markDirty()
	{
		parent.markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return parent.isUseableByPlayer(player);
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		parent.openInventory(player);
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		parent.closeInventory(player);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return parent.isItemValidForSlot(accesible[index], stack);
	}

	public ItemStack mergeStackBang(ItemStack stack)
	{
		if (stack == null) return null;
		InventoryProcessor.instance().mergeWithSlots(this, stack);
		return stack.stackSize <= 0 ? null : stack;
	}

	public ItemStack mergeStack(ItemStack stack)
	{
		if (stack != null)
		{
			final ItemStack result = stack.copy();
			mergeStackBang(result);
			return result.stackSize <= 0 ? null : result;
		}
		return null;
	}

	@Override
	public int getField(int id)
	{
		return parent.getField(id);
	}

	@Override
	public void setField(int id, int value)
	{
		parent.setField(id, value);
	}

	@Override
	public int getFieldCount()
	{
		return parent.getFieldCount();
	}
}
