package growthcraft.bamboo.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBambooSlab extends BlockSlab
{
	private boolean isDouble;

	public BlockBambooSlab(boolean isDouble)
	{
		super(Material.wood);
		this.isDouble = isDouble;
		this.useNeighborBrightness = true;
		setStepSound(soundTypeWood);
		setResistance(5.0F);
		setHardness(2.0F);
		setUnlocalizedName("grc.bamboo_slab");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	@Override
	public boolean isDouble()
	{
		return this.isDouble;
	}

	@Override
	public String getUnlocalizedName(int meta)
	{
		return super.getUnlocalizedName();
	}

	@Override
	public Object getVariant(ItemStack stack)
	{
		GrowthCraftBamboo.getLogger().warn("(fixme) BlockBambooSlab#getVariant");
		return null;
	}

	@Override
	public IProperty<?> getVariantProperty()
	{
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		if (item != Item.getItemFromBlock(GrowthCraftBamboo.blocks.bambooDoubleSlab.getBlock()))
		{
			list.add(new ItemStack(item, 1, 0));
		}
	}

	@SideOnly(Side.CLIENT)
	private static boolean isBlockSingleSlab(Block block)
	{
		return block == GrowthCraftBamboo.blocks.bambooSingleSlab.getBlock();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World worldIn, BlockPos pos)
	{
		return Item.getItemFromBlock(GrowthCraftBamboo.blocks.bambooSingleSlab.getBlock());
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return Item.getItemFromBlock(GrowthCraftBamboo.blocks.bambooSingleSlab.getBlock());
	}

	protected ItemStack createStackedBlock(int par1)
	{
		return new ItemStack(GrowthCraftBamboo.blocks.bambooSingleSlab.getBlock(), 2, 0);
	}
}
