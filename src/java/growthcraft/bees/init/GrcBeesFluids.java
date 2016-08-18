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
package growthcraft.bees.init;

import growthcraft.api.bees.BeesFluidTag;
import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.GrcFluid;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.definition.ItemTypeDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.common.item.ItemFoodBottleFluid;
import growthcraft.core.util.FluidFactory;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;

public class GrcBeesFluids extends GrcModuleBase
{
	public FluidFactory.FluidDetails honey;
	public GrcFluid[] honeyMeadBooze;
	public ItemDefinition honeyMeadBottle;
	public ItemBucketBoozeDefinition[] honeyMeadBuckets;
	public BlockBoozeDefinition[] honeyMeadFluids;

	@Override
	public void preInit()
	{
		if (GrowthCraftBees.getConfig().honeyEnabled)
		{
			final Fluid honeyFluid = new GrcFluid("grc.honey", GrowthCraftBees.resources.create("honey_still"), GrowthCraftBees.resources.create("honey_flow"))
				.setColor(0xffac01)
				.setDensity(1420)
				.setViscosity(73600);
			this.honey = FluidFactory.instance().create(honeyFluid, FluidFactory.FEATURE_ALL_EDIBLE);
			honey.foodBottle = new ItemTypeDefinition<ItemFoodBottleFluid>(new ItemFoodBottleFluid(this.honey.getFluid(), 2, 0.2f, false));
			honey.refreshItemColor();
		}
		final FluidFactory.FluidBuilder builder = BoozeRegistryHelper.newBoozeBuilder();
		this.honeyMeadBooze = new GrcFluid[] {
			builder.create("grc.honey_mead_young"),
			builder.create("grc.honey_mead_fermented"),
			builder.create("grc.honey_mead_potent"),
			builder.create("grc.honey_mead_extended"),
			builder.create("grc.honey_mead_hyper_extended"),
			builder.create("grc.honey_mead_intoxicated"),
			builder.create("grc.honey_mead_poisoned")
		};
		this.honeyMeadFluids = new BlockBoozeDefinition[honeyMeadBooze.length];
		this.honeyMeadBuckets = new ItemBucketBoozeDefinition[honeyMeadBooze.length];
		BoozeRegistryHelper.registerBoozeFluids(honeyMeadBooze);
		for (GrcFluid booze : honeyMeadBooze)
		{
			booze.setColor(GrowthCraftBees.getConfig().honeyMeadColor).setDensity(1000).setViscosity(1200);
		}
		BoozeRegistryHelper.initializeBooze(honeyMeadBooze, honeyMeadFluids, honeyMeadBuckets);
		BoozeRegistryHelper.setBoozeFoodStats(honeyMeadBooze, 1, -0.45f);
		BoozeRegistryHelper.setBoozeFoodStats(honeyMeadBooze[0], 1, 0.45f);
		honeyMeadBottle = new ItemDefinition(new ItemBoozeBottle(honeyMeadBooze));

		if (honey != null)
		{
			honey.setCreativeTab(GrowthCraftBees.tab);
			//honey.block.getBlock().setBlockTextureName("grcbees:fluids/honey");
			honey.refreshItemColor();
		}
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(honeyMeadBottle.getItem(), "grc.honey_mead");
		BoozeRegistryHelper.registerBooze(honeyMeadBooze, honeyMeadFluids, honeyMeadBuckets, honeyMeadBottle);
		if (honey != null)
		{
			honey.registerObjects("grc", "honey");
			CoreRegistry.instance().fluidDictionary().addFluidTags(honey.getFluid(), BeesFluidTag.HONEY);
		}
	}

	@Override
	public void init()
	{
		if (honey != null)
		{
			OreDictionary.registerOre("bottleHoney", honey.asBottleItemStack());
			OreDictionary.registerOre("bucketHoney", honey.asBucketItemStack());
			OreDictionary.registerOre("honeyDrop", honey.asBucketItemStack());
			OreDictionary.registerOre("dropHoney", honey.asBucketItemStack());
		}
	}
}
