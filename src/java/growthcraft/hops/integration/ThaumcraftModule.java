/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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
package growthcraft.hops.integration;

//import growthcraft.cellar.integration.ThaumcraftBoozeHelper;
//import growthcraft.core.integration.thaumcraft.AspectsHelper;
import growthcraft.core.integration.ThaumcraftModuleBase;
import growthcraft.hops.GrowthCraftHops;

//import thaumcraft.api.ThaumcraftApi;
//import thaumcraft.api.aspects.AspectList;
//import thaumcraft.api.aspects.Aspect;

import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftHops.MOD_ID);
	}

	@Override
	@Optional.Method(modid="Thaumcraft")
	protected void integrate()
	{
		FMLInterModComms.sendMessage("Thaumcraft", "harvestClickableCrop", GrowthCraftHops.blocks.hopVine.asStack(1, 3));
		GrowthCraftHops.getLogger().warn("(fixme) GrowthCraftHops/ThaumcraftModule#integrate");
		/*ThaumcraftApi.registerObjectTag(GrowthCraftHops.hopSeeds.asStack(), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftHops.hops.asStack(), new AspectList().add(Aspect.CROP, 1));
		// hopAle
		AspectList[] common = new AspectList[]
		{
			new AspectList().add(Aspect.PLANT, 1),
			new AspectList().add(Aspect.PLANT, 1).add(Aspect.MOTION, 1),
			new AspectList().add(Aspect.PLANT, 1).add(Aspect.MOTION, 2),
			new AspectList().add(Aspect.PLANT, 1).add(Aspect.MOTION, 1),
			new AspectList(),
			new AspectList().add(Aspect.PLANT, 1).add(Aspect.MOTION, 2),
			new AspectList().add(Aspect.PLANT, 1).add(Aspect.MOTION, 3),
			new AspectList().add(Aspect.PLANT, 1).add(Aspect.MOTION, 1).add(Aspect.POISON, 1),
			new AspectList().add(Aspect.POISON, 2)
		};
		for (int i = 0; i < common.length; ++i)
		{
			final AspectList list = common[i];
			ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(GrowthCraftHops.fluids.hopAle.asStack(1, i), list.copy());
			ThaumcraftBoozeHelper.instance().registerAspectsForBucket(GrowthCraftHops.fluids.hopAleBuckets[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.PLANT, Aspect.MOTION));
			ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(GrowthCraftHops.fluids.hopAleFluids[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.PLANT, Aspect.MOTION));
		}
		// Lager
		common = new AspectList[]
		{
			new AspectList().add(Aspect.COLD, 1),
			new AspectList().add(Aspect.COLD, 1).add(Aspect.FLIGHT, 1).add(Aspect.MOTION, 1),
			new AspectList().add(Aspect.COLD, 1).add(Aspect.FLIGHT, 2).add(Aspect.MOTION, 2),
			new AspectList().add(Aspect.COLD, 1).add(Aspect.FLIGHT, 1).add(Aspect.MOTION, 1),
			new AspectList().add(Aspect.COLD, 1).add(Aspect.FLIGHT, 2).add(Aspect.MOTION, 2),
			new AspectList().add(Aspect.COLD, 1).add(Aspect.FLIGHT, 3).add(Aspect.MOTION, 3).add(Aspect.POISON, 1),
			new AspectList().add(Aspect.COLD, 1).add(Aspect.POISON, 2)
		};
		for (int i = 0; i < common.length; ++i)
		{
			final AspectList list = common[i];
			ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(GrowthCraftHops.fluids.lager.asStack(1, i), list.copy());
			ThaumcraftBoozeHelper.instance().registerAspectsForBucket(GrowthCraftHops.fluids.lagerBuckets[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.COLD, Aspect.FLIGHT, Aspect.MOTION));
			ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(GrowthCraftHops.fluids.lagerFluids[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.COLD, Aspect.FLIGHT, Aspect.MOTION));
		}*/
	}
}
