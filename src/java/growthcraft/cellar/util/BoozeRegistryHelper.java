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
package growthcraft.cellar.util;

import java.util.List;
import java.util.ArrayList;

import growthcraft.api.cellar.booze.BoozeEffect;
import growthcraft.api.cellar.booze.BoozeEntry;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.booze.IBoozeRegistry;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.GrcFluid;
import growthcraft.api.core.fluids.IFluidDictionary;
import growthcraft.cellar.common.block.BlockFluidBooze;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBlockFluidBooze;
import growthcraft.cellar.common.item.ItemBucketBooze;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.eventhandler.EventHandlerBucketFill;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.FluidFactory;

import net.minecraft.init.Items;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BoozeRegistryHelper
{
	private BoozeRegistryHelper() {}

	public static FluidFactory.FluidBuilder newBoozeBuilder()
	{
		return FluidFactory.instance().newBuilder(GrowthCraftCellar.resources.create("booze_still"), GrowthCraftCellar.resources.create("booze_flow"));
	}

	public static void registerBoozeFluids(GrcFluid[] boozes)
	{
		for (GrcFluid booze : boozes)
		{
			FluidRegistry.registerFluid(booze);
			CellarRegistry.instance().booze().registerBooze(booze);
		}
	}

	public static void initializeBooze(Fluid[] boozes, BlockBoozeDefinition[] fluidBlocks, ItemBucketBoozeDefinition[] buckets)
	{
		for (int i = 0; i < boozes.length; ++i)
		{
			final BlockFluidBooze boozeBlock = new BlockFluidBooze(boozes[i]);
			fluidBlocks[i] = new BlockBoozeDefinition(boozeBlock);
			buckets[i] = new ItemBucketBoozeDefinition(new ItemBucketBooze(boozeBlock, boozes[i]));
		}
	}

	public static void setBoozeFoodStats(Fluid booze, int heal, float saturation)
	{
		final BoozeEntry entry = CellarRegistry.instance().booze().getBoozeEntry(booze);
		if (entry != null)
		{
			entry.setFoodStats(heal, saturation);
		}
	}

	public static void setBoozeFoodStats(Fluid[] boozes, int heal, float saturation)
	{
		for (Fluid booze : boozes)
		{
			setBoozeFoodStats(booze, heal, saturation);
		}
	}

	public static void registerBooze(Fluid[] boozes, BlockBoozeDefinition[] fluidBlocks, ItemBucketBoozeDefinition[] buckets, ItemDefinition bottle)
	{
		for (int i = 0; i < boozes.length; ++i)
		{
			final Fluid booze = boozes[i];
			final String basename = booze.getUnlocalizedName();
			buckets[i].register(basename + "_bucket");
			fluidBlocks[i].register(basename + "_fluid_block", ItemBlockFluidBooze.class);

			EventHandlerBucketFill.instance().register(fluidBlocks[i].getBlock(), buckets[i].getItem());

			final FluidStack boozeStack = new FluidStack(boozes[i], FluidContainerRegistry.BUCKET_VOLUME);
			FluidContainerRegistry.registerFluidContainer(boozeStack, buckets[i].asStack(), FluidContainerRegistry.EMPTY_BUCKET);

			final FluidStack fluidStack = new FluidStack(boozes[i], GrowthCraftCore.getConfig().bottleCapacity);
			FluidContainerRegistry.registerFluidContainer(fluidStack, bottle.asStack(1, i), GrowthCraftCore.EMPTY_BOTTLE);

			GameRegistry.addShapelessRecipe(bottle.asStack(3, i), buckets[i].getItem(), Items.glass_bottle, Items.glass_bottle, Items.glass_bottle);
		}
	}

	public static List<BoozeEffect> getBoozeEffects(Fluid[] boozes)
	{
		final IBoozeRegistry reg = CellarRegistry.instance().booze();
		final IFluidDictionary dict = CoreRegistry.instance().fluidDictionary();
		final List<BoozeEffect> effects = new ArrayList<BoozeEffect>();
		for (int i = 0; i < boozes.length; ++i)
		{
			if (dict.hasFluidTags(boozes[i], BoozeTag.FERMENTED))
			{
				effects.add(reg.getEffect(boozes[i]));
			}
		}
		return effects;
	}
}
