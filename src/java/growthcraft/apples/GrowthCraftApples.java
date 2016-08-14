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
package growthcraft.apples;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.core.util.DomainResourceLocationFactory;
import growthcraft.apples.common.CommonProxy;
import growthcraft.apples.common.village.ComponentVillageAppleFarm;
import growthcraft.apples.common.village.VillageHandlerApples;
import growthcraft.apples.handler.AppleFuelHandler;
import growthcraft.apples.init.GrcApplesBlocks;
import growthcraft.apples.init.GrcApplesFluids;
import growthcraft.apples.init.GrcApplesItems;
import growthcraft.apples.init.GrcApplesRecipes;
//import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.MapGenHelper;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

@Mod(
	modid = GrowthCraftApples.MOD_ID,
	name = GrowthCraftApples.MOD_NAME,
	version = GrowthCraftApples.MOD_VERSION,
	acceptedMinecraftVersions = GrowthCraftCore.MOD_ACC_MINECRAFT,
	dependencies = "required-after:Growthcraft@@VERSION@;required-after:Growthcraft|Cellar@@VERSION@"
)
public class GrowthCraftApples
{
	public static final String MOD_ID = "Growthcraft|Apples";
	public static final String MOD_NAME = "Growthcraft Apples";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftApples instance;
	public static CreativeTabs creativeTab;
	public static final DomainResourceLocationFactory resources = new DomainResourceLocationFactory("grcapples");
	public static final GrcApplesBlocks blocks = new GrcApplesBlocks();
	public static final GrcApplesItems items = new GrcApplesItems();
	public static final GrcApplesFluids fluids = new GrcApplesFluids();

	private final ILogger logger = new GrcLogger(MOD_ID);
	private final GrcApplesConfig config = new GrcApplesConfig();
	private final ModuleContainer modules = new ModuleContainer();
	private final GrcApplesRecipes recipes = new GrcApplesRecipes();

	public static GrcApplesConfig getConfig()
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
		creativeTab = GrowthCraftCore.creativeTab;
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/apples.conf");

		modules.add(blocks);
		modules.add(items);
		modules.add(fluids);
		modules.add(recipes);
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.apples.integration.ThaumcraftModule());
		if (config.debugEnabled) modules.setLogger(logger);
		modules.add(CommonProxy.instance);
		modules.preInit();
		register();
	}

	public void register()
	{
		MapGenHelper.registerStructureComponent(ComponentVillageAppleFarm.class, "grc.apple_farm");
		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(blocks.appleLeaves.getBlock(), 30, 60);
		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapelessRecipe(items.appleSeeds.asStack(), Items.apple);
		//====================
		// SMELTING
		//====================
		GameRegistry.registerFuelHandler(new AppleFuelHandler());
		// modules registration phase
		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		final VillageHandlerApples handler = new VillageHandlerApples();
		logger.warn("(fixme) GrowthCraftApples#registerVillageTradeHandler");
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
