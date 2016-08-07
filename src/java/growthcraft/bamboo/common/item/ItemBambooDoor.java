package growthcraft.bamboo.common.item;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.item.GrcItemBase;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemBambooDoor extends GrcItemBase
{
	public ItemBambooDoor()
	{
		super();
		this.maxStackSize = 1;
		setUnlocalizedName("grc.bamboo_door");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	private static void placeDoorBlock(World world, BlockPos pos, int side, Block block)
	{
		byte b0 = 0;
		byte b1 = 0;

		if (side == 0)
		{
			b1 = 1;
		}

		if (side == 1)
		{
			b0 = -1;
		}

		if (side == 2)
		{
			b1 = -1;
		}

		if (side == 3)
		{
			b0 = 1;
		}

		final BlockPos posNW = new BlockPos(pos.getX() - b0, pos.getY(), pos.getZ() - b1);
		final BlockPos posNE = new BlockPos(pos.getX() + b0, pos.getY(), pos.getZ() + b1);
		final int i1 = (world.getBlockState(posNW).getBlock().isNormalCube(world, posNW) ? 1 : 0) + (world.getBlockState(posNW.up()).getBlock().isNormalCube(world, posNW.up()) ? 1 : 0);
		final int j1 = (world.getBlockState(posNE).getBlock().isNormalCube(world, posNE) ? 1 : 0) + (world.getBlockState(posNE.up()).getBlock().isNormalCube(world, posNE.up()) ? 1 : 0);
		final boolean flag = world.getBlockState(posNW).getBlock() == block || world.getBlockState(posNW.up()).getBlock() == block;
		final boolean flag1 = world.getBlockState(posNE).getBlock() == block || world.getBlockState(posNE.up()).getBlock() == block;
		boolean flag2 = false;

		if (flag && !flag1)
		{
			flag2 = true;
		}
		else if (j1 > i1)
		{
			flag2 = true;
		}

		GrowthCraftBamboo.getLogger().warn("(fixme) ItemBambooDoor#placeDoorBlock")
		world.setBlockState(pos, block.getDefaultState()); //block, side, 2);
		world.setBlockState(pos.up(), block.getDefaultState()); //block, 8 | (flag2 ? 1 : 0), 2);
		//world.notifyBlocksOfNeighborChange(i, j, k, block);
		//world.notifyBlocksOfNeighborChange(i, j + 1, k, block);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		if (side != 1)
		{
			return false;
		}
		else
		{
			++y;
			final Block block = GrowthCraftBamboo.blocks.bambooDoor.getBlock();

			if (player.canPlayerEdit(pos, side, stack) && player.canPlayerEdit(pos.up(), side, stack))
			{
				if (!block.canPlaceBlockAt(world, pos))
				{
					return false;
				}
				else
				{
					final int dir = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
					placeDoorBlock(world, pos, dir, block);
					--stack.stackSize;
					return true;
				}
			}
			else
			{
				return false;
			}
		}
	}
}
