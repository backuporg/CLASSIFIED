package growthcraft.bamboo.common.block;

import java.util.ArrayList;
import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBambooLeaves extends BlockLeaves implements IShearable
{
	private int[] adjacentTreeBlocks;

	public BlockBambooLeaves()
	{
		super(Material.leaves, false);
		setLightOpacity(1);
		setStepSound(soundTypeGrass);
		setHardness(0.2F);
		setTickRandomly(true);
		setUnlocalizedName("grc.bamboo_leaves");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
		setDefaultState(blockState.getBaseState().withProperty(BlockLeaves.DECAYABLE, false));
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(Blocks.leaves, 1, world.getBlockState(pos) & 3));
		return ret;
	}
}
