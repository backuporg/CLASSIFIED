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
package growthcraft.bamboo;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.core.util.DomainResourceLocationFactory;
import growthcraft.bamboo.common.CommonProxy;
import growthcraft.bamboo.common.entity.EntityBambooRaft;
import growthcraft.bamboo.common.village.ComponentVillageBambooYard;
import growthcraft.bamboo.common.village.VillageHandlerBamboo;
import growthcraft.bamboo.common.world.BiomeGenBamboo;
import growthcraft.bamboo.common.world.WorldGeneratorBamboo;
import growthcraft.bamboo.creativetab.CreativeTabsGrowthcraftBamboo;
import growthcraft.bamboo.handler.BambooFuelHandler;
import growthcraft.bamboo.init.GrcBambooBlocks;
import growthcraft.bamboo.init.GrcBambooItems;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.MapGenHelper;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftBamboo.MOD_ID,
	name = GrowthCraftBamboo.MOD_NAME,
	version = GrowthCraftBamboo.MOD_VERSION,
	acceptedMinecraftVersions = GrowthCraftCore.MOD_ACC_MINECRAFT,
	dependencies = "required-after:Growthcraft"
)
public class GrowthCraftBamboo
{
	public static final String MOD_ID = "Growthcraft|Bamboo";
	public static final String MOD_NAME = "Growthcraft Bamboo";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftBamboo instance;
	public static CreativeTabs creativeTab;
	public static final DomainResourceLocationFactory resources = new DomainResourceLocationFactory("grcbamboo");
	public static final GrcBambooBlocks blocks = new GrcBambooBlocks();
	public static final GrcBambooItems items = new GrcBambooItems();
	public static BiomeGenBase bambooBiome;

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcBambooConfig config = new GrcBambooConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcBambooConfig getConfig()
	{
		return instance.config;
	}

	public static ILogger getLogger()
	{
		return instance.logger;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/bamboo.conf");
		modules.add(blocks);
		modules.add(items);
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.bamboo.integration.ThaumcraftModule());
		if (config.debugEnabled) modules.setLogger(logger);
		modules.add(CommonProxy.instance);
		modules.freeze();
		creativeTab = new CreativeTabsGrowthcraftBamboo("creative_tab_grcbamboo");
		modules.preInit();
		if (config.generateBambooBiome)
		{
			bambooBiome = (new BiomeGenBamboo(config.bambooBiomeID))
				.setColor(353825)
				.setBiomeName("BambooForest")
				.setFillerBlockMetadata(5159473)
				.setTemperatureRainfall(0.7F, 0.8F);
		}
		register();
	}

	private void register()
	{
		modules.register();

		if (config.generateBambooBiome)
		{
			//GameRegistry.addBiome(bambooBiome);
			BiomeManager.addSpawnBiome(bambooBiome);
			BiomeDictionary.registerBiomeType(bambooBiome, Type.FOREST);
		}

		GameRegistry.registerWorldGenerator(new WorldGeneratorBamboo(), 0);

		EntityRegistry.registerModEntity(EntityBambooRaft.class, "bamboo_raft", 1, this, 80, 3, true);

		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapedRecipe(blocks.bambooWall.asStack(6), "###", "###", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooStairs.asStack(4), "#  ", "## ", "###", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooSingleSlab.asStack(6), "###", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(items.bambooDoorItem.asStack(), "##", "##", "##", '#', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(items.bambooRaft.asStack(), "A A", "AAA", 'A', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooBlock.asStack(), "A", "A", 'A', blocks.bambooSingleSlab.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooBlock.asStack(), "AA", "AA", 'A', items.bamboo.getItem());
		GameRegistry.addShapedRecipe(blocks.bambooFence.asStack(3), "AAA", "AAA", 'A', items.bamboo.getItem());
		GameRegistry.addShapedRecipe(blocks.bambooFenceGate.asStack(), "ABA", "ABA", 'A', items.bamboo.getItem(), 'B', blocks.bambooBlock.getBlock());
		GameRegistry.addShapedRecipe(blocks.bambooScaffold.asStack(16), "BBB", " A ", "A A", 'A', items.bamboo.getItem(), 'B', blocks.bambooBlock.getBlock());
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.torch, 2), new Object[] {"A", "B", 'A', items.bambooCoal.getItem(), 'B', "stickWood"}));

		MapGenHelper.registerStructureComponent(ComponentVillageBambooYard.class, "grc.bamboo_yard");

		registerOres();

		//====================
		// SMELTING
		//====================
		GameRegistry.registerFuelHandler(new BambooFuelHandler());
		GameRegistry.addSmelting(items.bamboo.getItem(), items.bambooCoal.asStack(), 0.075f);
	}

	public void registerOres()
	{
		/*
		 * ORE DICTIONARY
		 */

		// General ore dictionary names
		OreDictionary.registerOre("stickWood", items.bamboo.getItem());
		OreDictionary.registerOre("woodStick", items.bamboo.getItem());
		OreDictionary.registerOre("plankWood", blocks.bambooBlock.getBlock());
		OreDictionary.registerOre("woodPlank", blocks.bambooBlock.getBlock());
		OreDictionary.registerOre("slabWood", blocks.bambooSingleSlab.getBlock());
		OreDictionary.registerOre("woodSlab", blocks.bambooSingleSlab.getBlock());
		OreDictionary.registerOre("stairWood", blocks.bambooStairs.getBlock());
		OreDictionary.registerOre("woodStair", blocks.bambooStairs.getBlock());
		OreDictionary.registerOre("leavesTree", blocks.bambooLeaves.getBlock());
		OreDictionary.registerOre("treeLeaves", blocks.bambooLeaves.getBlock());


		// Bamboo specific
		OreDictionary.registerOre("cropBamboo", items.bamboo.getItem());
		OreDictionary.registerOre("materialBamboo", items.bamboo.getItem());
		OreDictionary.registerOre("bamboo", items.bamboo.getItem());
		OreDictionary.registerOre("plankBamboo", blocks.bambooBlock.getBlock());
		OreDictionary.registerOre("slabBamboo", blocks.bambooSingleSlab.getBlock());
		OreDictionary.registerOre("stairBamboo", blocks.bambooStairs.getBlock());
		OreDictionary.registerOre("treeBambooLeaves", blocks.bambooLeaves.getBlock());

		OreDictionary.registerOre("foodBambooshoot", blocks.bambooShoot.getBlock());
		OreDictionary.registerOre("foodBambooshoot", items.bambooShootFood.getItem());

		/*
		 * For Pam's HarvestCraft
		 *   Uses the same OreDict. names as HarvestCraft
		 */
		OreDictionary.registerOre("cropBambooshoot", blocks.bambooShoot.getBlock());
		OreDictionary.registerOre("listAllveggie", blocks.bambooShoot.getBlock());
		OreDictionary.registerOre("cropBambooshoot", items.bambooShootFood.getItem());
		OreDictionary.registerOre("listAllveggie", items.bambooShootFood.getItem());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		final VillageHandlerBamboo handler = new VillageHandlerBamboo();
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
		modules.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
