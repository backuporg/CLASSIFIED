package growthcraft.bamboo.common.item;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.item.GrcItemFoodBase;

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

public class ItemBambooShoot extends GrcItemFoodBase implements IPlantable
{
	private Block cropBlock;

	public ItemBambooShoot()
	{
		super(4, 0.6F, false);
		this.cropBlock = GrowthCraftBamboo.blocks.bambooShoot.getBlock();
		setUnlocalizedName("grc.bamboo_shoot_food");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		final Block block1 = world.getBlock(x, y, z);

        if (!block.isReplaceable(world, pos))
        {
            pos = pos.offset(dir);
        }

		if (stack.stackSize == 0)
		{
			return false;
		}
		else if (!player.canPlayerEdit(pos, dir, stack))
		{
			return false;
		}
		else
		{
			if (world.canBlockBePlaced(cropBlock, x, y, z, false, dir, (Entity)null, stack))
			{
				final int meta = cropBlock.onBlockPlaced(world, x, y, z, dir, par8, par9, par10, 0);

				if (world.setBlock(x, y, z, cropBlock, meta, 3))
				{
					if (world.getBlock(x, y, z) == cropBlock)
					{
						cropBlock.onBlockPlacedBy(world, pos, state, player, stack);
						cropBlock.onPostBlockPlaced(world, x, y, z, meta);
					}

					world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), cropBlock.stepSound.getPlaceSound(), (cropBlock.stepSound.getVolume() + 1.0F) / 2.0F, cropBlock.stepSound.getPitch() * 0.8F);
					--stack.stackSize;
				}
			}

			return true;
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return cropBlock.getDefaultState();
	}
}
