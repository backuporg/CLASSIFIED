package growthcraft.apples.common.item;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAppleSeeds extends GrcItemBase implements IPlantable
{
	private Block cropBlock;

	public ItemAppleSeeds()
	{
		super();
		this.cropBlock = GrowthCraftApples.appleSapling.getBlock();
		setUnlocalizedName("grc.apple_seeds");
		setCreativeTab(GrowthCraftCore.creativeTab);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		final IBlockState state = world.getBlockState(pos);
		final Block block = state.getBlock();
		if (Blocks.snow_layer.isAssociatedBlock(block) && (world.getBlockMetadata(pos) & 7) < 1)
		{
			dir = EnumFacing.UP;
		}
		else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush)
		{
			pos = pos.offset(dir);
		}
		if (!player.canPlayerEdit(pos, dir, stack))
		{
			return false;
		}
		else if (stack.stackSize == 0)
		{
			return false;
		}
		else
		{
			if (world.canBlockBePlaced(cropBlock, pos, false, dir, (Entity)null, stack))
			{
				final int meta = cropBlock.onBlockPlaced(world, pos, dir, hitX, hitY, hitZ, 0);
				if (world.setBlock(pos, cropBlock, meta, 3))
				{
					if (world.getBlock(pos) == cropBlock)
					{
						cropBlock.onBlockPlacedBy(world, pos, state, player, stack);
						cropBlock.onPostBlockPlaced(world, pos, meta);
					}
					world.playSoundEffect(
						(double)pos.getX() + 0.5D,
						(double)pos.getY() + 0.5D,
						(double)pos.getZ() + 0.5D,
						cropBlock.stepSound.getPlaceSound(),
						(cropBlock.stepSound.getVolume() + 1.0F) / 2.0F,
						cropBlock.stepSound.getFrequency() * 0.8F);
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
