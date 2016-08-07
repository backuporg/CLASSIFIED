/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
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
package growthcraft.core.common.item;

import growthcraft.api.core.util.BlockFlags;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class GrcItemFoodPlantBlock extends GrcItemFoodBase implements IPlantable
{
	protected Block block;

	public GrcItemFoodPlantBlock(Block p_block, int amount, float saturation, boolean isWolfFood)
	{
		super(amount, saturation, isWolfFood);
		this.block = p_block;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return block.getDefaultState();
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		final IBlockState currentState = world.getBlockState(pos);
		final Block currentBlock = currentState.getBlock();
		if (!currentBlock.isReplaceable(world, pos))
		{
			pos = pos.offset(dir);
		}
		final IBlockState state = getPlant(world, pos);
		if (stack.stackSize == 0)
		{
			return false;
		}
		else if (!player.canPlayerEdit(pos, dir, stack))
		{
			return false;
		}
		else if (world.canBlockBePlaced(state.getBlock(), pos, false, dir, (Entity)null, stack))
		{
			final IBlockState _state = block.onBlockPlaced(world, pos, dir, hitX, hitY, hitZ, 0, player);
			if (world.setBlockState(pos, state, BlockFlags.UPDATE_AND_SYNC))
			{
				if (world.getBlockState(pos).getBlock() == block)
				{
					block.onBlockPlacedBy(world, pos, state, player, stack);
				}
				world.playSoundEffect(
					(double)pos.getX() + 0.5D,
					(double)pos.getY() + 0.5D,
					(double)pos.getZ() + 0.5D,
					block.stepSound.getPlaceSound(),
					(block.stepSound.getVolume() + 1.0F) / 2.0F,
					block.stepSound.getFrequency() * 0.8F);
				--stack.stackSize;
			}
			return true;
		}
		return false;
	}
}
