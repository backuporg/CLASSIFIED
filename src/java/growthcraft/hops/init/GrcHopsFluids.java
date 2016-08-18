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
package growthcraft.hops.init;

import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.api.core.GrcFluid;
import growthcraft.api.core.item.OreItemStacks;
import growthcraft.api.core.util.TickUtils;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.FluidFactory;
import growthcraft.hops.GrowthCraftHops;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;

public class GrcHopsFluids extends GrcModuleBase
{
	public GrcFluid[] hopAleBooze;
	public GrcFluid[] lagerBooze;
	public BlockBoozeDefinition[] hopAleFluids;
	public BlockBoozeDefinition[] lagerFluids;
	public ItemDefinition hopAle;
	public ItemDefinition lager;
	public ItemBucketBoozeDefinition[] hopAleBuckets;
	public ItemBucketBoozeDefinition[] lagerBuckets;

	@Override
	public void preInit()
	{
		final FluidFactory.FluidBuilder builder = BoozeRegistryHelper.newBoozeBuilder();
		this.lagerBooze = new GrcFluid[] {
			builder.create("grc.lager_young"),
			builder.create("grc.lager_fermented"),
			builder.create("grc.lager_potent"),
			builder.create("grc.lager_extended"),
			builder.create("grc.lager_hyper_extended"),
			builder.create("grc.lager_intoxicated"),
			builder.create("grc.lager_poisoned")
		};
		this.lagerFluids = new BlockBoozeDefinition[lagerBooze.length];
		this.lagerBuckets = new ItemBucketBoozeDefinition[lagerBooze.length];
		BoozeRegistryHelper.registerBoozeFluids(lagerBooze);
		for (GrcFluid booze : lagerBooze)
		{
			booze.setColor(GrowthCraftHops.getConfig().lagerColor).setDensity(1080);
		}
		BoozeRegistryHelper.initializeBooze(lagerBooze, lagerFluids, lagerBuckets);
		BoozeRegistryHelper.setBoozeFoodStats(lagerBooze, 1, -0.6f);
		BoozeRegistryHelper.setBoozeFoodStats(lagerBooze[0], 1, 0.3f);
		this.lager = new ItemDefinition(new ItemBoozeBottle(lagerBooze));
		this.hopAleBooze = new GrcFluid[] {
			builder.create("grc.hop_ale_young"),
			builder.create("grc.hop_ale_fermented"),
			builder.create("grc.hop_ale_potent"),
			builder.create("grc.hop_ale_extended"),
			builder.create("grc.ale"),
			builder.create("grc.hop_ale_hyper_extended"),
			builder.create("grc.hop_ale_chilled"),
			builder.create("grc.hop_ale_intoxicated"),
			builder.create("grc.hop_ale_poisoned")
		};
		this.hopAleFluids = new BlockBoozeDefinition[hopAleBooze.length];
		this.hopAleBuckets = new ItemBucketBoozeDefinition[hopAleBooze.length];
		BoozeRegistryHelper.registerBoozeFluids(hopAleBooze);
		for (GrcFluid booze : hopAleBooze)
		{
			booze.setColor(GrowthCraftHops.getConfig().hopAleColor).setDensity(1080);
		}
		BoozeRegistryHelper.initializeBooze(hopAleBooze, hopAleFluids, hopAleBuckets);
		BoozeRegistryHelper.setBoozeFoodStats(hopAleBooze, 1, -0.6f);
		BoozeRegistryHelper.setBoozeFoodStats(hopAleBooze[0], 1, 0.3f);
		BoozeRegistryHelper.setBoozeFoodStats(hopAleBooze[4], 1, 0.3f);

		this.hopAle = new ItemDefinition(new ItemBoozeBottle(hopAleBooze));
	}

	private void registerLager()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[lagerBooze.length];
		for (int i = 0; i < fs.length; ++i)
		{
			fs[i] = new FluidStack(lagerBooze[i], 1);
		}

		GrowthCraftCellar.boozeBuilderFactory.create(lagerBooze[0])
			.tags(BoozeTag.YOUNG, BoozeTag.CHILLED)
			.brewsFrom(
				new FluidStack(hopAleBooze[4], 40),
				new OreItemStacks("yeastLager"),
				TickUtils.minutes(1),
				Residue.newDefault(0.0F));

		GrowthCraftCellar.boozeBuilderFactory.create(lagerBooze[1])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED)
			.fermentsFrom(fs[0], new OreItemStacks("yeastBrewers"), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.0419f), TickUtils.seconds(45))
				.addPotionEntry(Potion.moveSpeed, TickUtils.seconds(90), 0)
				.addPotionEntry(Potion.digSpeed, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(lagerBooze[2])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new OreItemStacks("dustGlowstone"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("dustGlowstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.0419f), TickUtils.seconds(45))
				.addPotionEntry(Potion.moveSpeed, TickUtils.seconds(90), 0)
				.addPotionEntry(Potion.digSpeed, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(lagerBooze[3])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new OreItemStacks("dustRedstone"), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("dustRedstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.0419f), TickUtils.seconds(45))
				.addPotionEntry(Potion.moveSpeed, TickUtils.seconds(90), 0)
				.addPotionEntry(Potion.digSpeed, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(lagerBooze[4])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED, BoozeTag.HYPER_EXTENDED)
			.fermentsFrom(fs[2], new OreItemStacks("yeastEthereal"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastEthereal"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.0419f), TickUtils.seconds(45))
				.addPotionEntry(Potion.moveSpeed, TickUtils.seconds(90), 0)
				.addPotionEntry(Potion.digSpeed, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(lagerBooze[5])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], new OreItemStacks("yeastOrigin"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastOrigin"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.091f), TickUtils.seconds(45))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.digSpeed.id, TickUtils.minutes(3), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.digSlowdown.id, TickUtils.minutes(3), 2))))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.moveSpeed.id, TickUtils.minutes(3), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.moveSlowdown.id, TickUtils.minutes(3), 2))));

		GrowthCraftCellar.boozeBuilderFactory.create(lagerBooze[6])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED, BoozeTag.POISONED)
			.fermentsFrom(fs[0], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[1], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[4], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[5], new OreItemStacks("yeastPoison"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.0419f), TickUtils.seconds(45))
				.createPotionEntry(Potion.poison, TickUtils.seconds(90), 0).toggleDescription(!GrowthCraftCore.getConfig().hidePoisonedBooze);
	}

	private void registerHopAle()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[hopAleBooze.length];
		for (int i = 0; i < fs.length; ++i)
		{
			fs[i] = new FluidStack(hopAleBooze[i], 1);
		}

		// Unhopped
		GrowthCraftCellar.boozeBuilderFactory.create(hopAleBooze[4])
			.tags(BoozeTag.YOUNG)
			.brewsFrom(
				new FluidStack(FluidRegistry.WATER, 40),
				new OreItemStacks("cropWheat"),
				TickUtils.minutes(1),
				Residue.newDefault(0.3F));

		GrowthCraftCellar.boozeBuilderFactory.create(hopAleBooze[0])
			.tags(BoozeTag.YOUNG, BoozeTag.HOPPED)
			.brewsFrom(
				new FluidStack(hopAleBooze[4], 40),
				new OreItemStacks("cropHops"),
				TickUtils.minutes(1),
				Residue.newDefault(0.0F));

		GrowthCraftCellar.boozeBuilderFactory.create(hopAleBooze[1])
			.tags(BoozeTag.FERMENTED)
			.fermentsFrom(fs[0], new OreItemStacks("yeastBrewers"), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.10f), TickUtils.seconds(45))
				.addPotionEntry(Potion.digSpeed, TickUtils.minutes(3), 0);

		// Glowstone
		GrowthCraftCellar.boozeBuilderFactory.create(hopAleBooze[2])
			.tags(BoozeTag.HOPPED, BoozeTag.FERMENTED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new OreItemStacks("dustGlowstone"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("dustGlowstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.12f), TickUtils.seconds(45))
				.addPotionEntry(Potion.digSpeed, TickUtils.minutes(3), 0);

		// Redstone
		GrowthCraftCellar.boozeBuilderFactory.create(hopAleBooze[3])
			.tags(BoozeTag.HOPPED, BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new OreItemStacks("dustRedstone"), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("dustRedstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.10f), TickUtils.seconds(45))
				.addPotionEntry(Potion.digSpeed, TickUtils.minutes(3), 0);

		// Ethereal Yeast
		GrowthCraftCellar.boozeBuilderFactory.create(hopAleBooze[5])
			.tags(BoozeTag.HOPPED, BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED)
			.fermentsFrom(fs[2], new OreItemStacks("yeastEthereal"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastEthereal"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.10f), TickUtils.seconds(45))
				.addPotionEntry(Potion.digSpeed, TickUtils.minutes(3), 0);

		// Lager Yeast
		GrowthCraftCellar.boozeBuilderFactory.create(hopAleBooze[6])
			.tags(BoozeTag.HOPPED, BoozeTag.FERMENTED, BoozeTag.CHILLED)
			.fermentsFrom(fs[5], new OreItemStacks("yeastLager"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.071f), TickUtils.seconds(45))
				.addPotionEntry(Potion.digSpeed, TickUtils.minutes(10), 2);

		// Intoxicated - Origin
		GrowthCraftCellar.boozeBuilderFactory.create(hopAleBooze[7])
			.tags(BoozeTag.HOPPED, BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], new OreItemStacks("yeastOrigin"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastOrigin"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.20f), TickUtils.seconds(45))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.digSpeed.id, TickUtils.minutes(3), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.digSlowdown.id, TickUtils.minutes(3), 2))));

		// Poisoned - Netherrash
		// Regardless of what you brewed it with, it will kill the hops in the
		// booze and poison it.
		GrowthCraftCellar.boozeBuilderFactory.create(hopAleBooze[8])
			.tags(BoozeTag.FERMENTED, BoozeTag.POISONED)
			.fermentsFrom(fs[1], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[5], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[6], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[7], new OreItemStacks("yeastPoison"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.10f), TickUtils.seconds(45))
				.createPotionEntry(Potion.poison, TickUtils.seconds(90), 0).toggleDescription(!GrowthCraftCore.getConfig().hidePoisonedBooze);
	}

	private void registerFermentations()
	{
		registerHopAle();
		registerLager();
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(hopAle.getItem(), "grc.hop_ale");
		GameRegistry.registerItem(lager.getItem(), "grc.lager");

		BoozeRegistryHelper.registerBooze(hopAleBooze, hopAleFluids, hopAleBuckets, hopAle);
		BoozeRegistryHelper.registerBooze(lagerBooze, lagerFluids, lagerBuckets, lager);
		registerFermentations();
	}
}
