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
package growthcraft.hops;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.core.util.DomainResourceLocationFactory;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.MapGenHelper;
import growthcraft.hops.common.block.BlockHops;
import growthcraft.hops.common.CommonProxy;
import growthcraft.hops.common.item.ItemHops;
import growthcraft.hops.common.item.ItemHopSeeds;
import growthcraft.hops.common.village.ComponentVillageHopVineyard;
import growthcraft.hops.common.village.VillageHandlerHops;
import growthcraft.hops.init.GrcHopsFluids;

import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod(
	modid = GrowthCraftHops.MOD_ID,
	name = GrowthCraftHops.MOD_NAME,
	version = GrowthCraftHops.MOD_VERSION,
	acceptedMinecraftVersions = GrowthCraftCore.MOD_ACC_MINECRAFT,
	dependencies = "required-after:Growthcraft@@VERSION@;required-after:Growthcraft|Cellar@@VERSION@"
)
public class GrowthCraftHops
{
	public static final String MOD_ID = "Growthcraft|Hops";
	public static final String MOD_NAME = "Growthcraft Hops";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftHops instance;
	public static DomainResourceLocationFactory resources = new DomainResourceLocationFactory("grchops");
	public static BlockTypeDefinition<BlockHops> hopVine;

	public static ItemDefinition hops;
	public static ItemDefinition hopSeeds;
	public static GrcHopsFluids fluids = new GrcHopsFluids();

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcHopsConfig config = new GrcHopsConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcHopsConfig getConfig()
	{
		return instance.config;
	}

	public static ILogger getLogger()
	{
		return instance.logger;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/hops.conf");

		modules.add(fluids);
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.hops.integration.ThaumcraftModule());
		if (config.debugEnabled) modules.setLogger(logger);

		//====================
		// INIT
		//====================
		hopVine  = new BlockTypeDefinition<BlockHops>(new BlockHops());

		hops     = new ItemDefinition(new ItemHops());
		hopSeeds = new ItemDefinition(new ItemHopSeeds());

		modules.preInit();
		register();
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(hopVine.getBlock(), "grc.hop_vine");

		GameRegistry.registerItem(hops.getItem(), "grc.hops");
		GameRegistry.registerItem(hopSeeds.getItem(), "grc.hopSeeds");

		CoreRegistry.instance().vineDrops().addDropEntry(hops.asStack(2), config.hopsVineDropRarity);

		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(hops.asStack(), 1, 2, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(hops.asStack(), 1, 2, 10));

		MapGenHelper.registerStructureComponent(ComponentVillageHopVineyard.class, "grc.hopvineyard");

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("cropHops", hops.getItem());
		OreDictionary.registerOre("materialHops", hops.getItem());
		OreDictionary.registerOre("conesHops", hops.getItem());
		OreDictionary.registerOre("seedHops", hopSeeds.getItem());
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("listAllseed", hopSeeds.getItem());

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapelessRecipe(hopSeeds.asStack(), hops.getItem());

		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CommonProxy.instance.init();
		final VillageHandlerHops handler = new VillageHandlerHops();
		logger.warn("(fixme) GrowthCraftHops registerVillageTradeHandler");
		//VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
		modules.init();
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
