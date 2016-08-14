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
package growthcraft.bees;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.api.bees.user.UserBeesConfig;
import growthcraft.api.bees.user.UserFlowerEntry;
import growthcraft.api.bees.user.UserFlowersConfig;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.core.util.DomainResourceLocationFactory;
import growthcraft.bees.client.gui.GuiHandlerBees;
import growthcraft.bees.common.block.BlockBeeBox;
import growthcraft.bees.common.block.BlockBeeHive;
import growthcraft.bees.common.CommonProxy;
import growthcraft.bees.common.item.ItemBlockBeeBox;
import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.bees.common.village.ComponentVillageApiarist;
//import growthcraft.bees.common.village.VillageHandlerBees;
import growthcraft.bees.common.village.VillageHandlerBeesApiarist;
import growthcraft.bees.common.world.WorldGeneratorBees;
import growthcraft.bees.creativetab.CreativeTabsGrowthcraftBees;
import growthcraft.bees.init.GrcBeesFluids;
import growthcraft.bees.init.GrcBeesItems;
import growthcraft.bees.init.GrcBeesRecipes;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.bop.BopPlatform;
import growthcraft.core.util.MapGenHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftBees.MOD_ID,
	name = GrowthCraftBees.MOD_NAME,
	version = GrowthCraftBees.MOD_VERSION,
	acceptedMinecraftVersions = GrowthCraftCore.MOD_ACC_MINECRAFT,
	dependencies = "required-after:Growthcraft@@VERSION@;required-after:Growthcraft|Cellar@@VERSION@;after:Forestry"
)
public class GrowthCraftBees
{
	public static final String MOD_ID = "Growthcraft|Bees";
	public static final String MOD_NAME = "Growthcraft Bees";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftBees instance;
	public static final DomainResourceLocationFactory resources = new DomainResourceLocationFactory("grcbees");
	public static CreativeTabs tab;
	public static BlockTypeDefinition<BlockBeeBox> beeBox;
	public static BlockTypeDefinition<BlockBeeBox> beeBoxBamboo;
	public static BlockTypeDefinition<BlockBeeBox> beeBoxBiomesOPlenty;
	public static BlockTypeDefinition<BlockBeeBox> beeBoxThaumcraft;
	public static BlockDefinition beeHive;
	public static final GrcBeesItems items = new GrcBeesItems();
	public static final GrcBeesFluids fluids = new GrcBeesFluids();

	public static VillagerProfession apiaristProfession;

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcBeesConfig config = new GrcBeesConfig();
	private ModuleContainer modules = new ModuleContainer();
	private UserBeesConfig userBeesConfig = new UserBeesConfig();
	private UserFlowersConfig userFlowersConfig = new UserFlowersConfig();
	private GrcBeesRecipes recipes = new GrcBeesRecipes();
	//private UserHoneyConfig userHoneyConfig = new UserHoneyConfig();

	public static UserBeesConfig getUserBeesConfig()
	{
		return instance.userBeesConfig;
	}

	/**
	 * Only use this logger for logging GrowthCraftBees related items
	 */
	public static ILogger getLogger()
	{
		return instance.logger;
	}

	public static GrcBeesConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/bees.conf");
		modules.add(items);
		modules.add(fluids);
		modules.add(recipes);
		userBeesConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/bees/bees.json");
		modules.add(userBeesConfig);
		userFlowersConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/bees/flowers.json");
		modules.add(userFlowersConfig);
		//userHoneyConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/bees/honey.json");
		//modules.add(userHoneyConfig);
		if (config.enableGrcBambooIntegration) modules.add(new growthcraft.bees.integration.GrcBambooModule());
		if (config.enableWailaIntegration) modules.add(new growthcraft.bees.integration.Waila());
		if (config.enableBoPIntegration) modules.add(new growthcraft.bees.integration.BoPModule());
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.bees.integration.ThaumcraftModule());
		if (config.debugEnabled)
		{
			BeesRegistry.instance().setLogger(logger);
			modules.setLogger(logger);
		}
		modules.add(CommonProxy.instance);
		modules.freeze();
		tab = new CreativeTabsGrowthcraftBees("creative_tab_grcbees");

		apiaristProfession = new VillagerProfession(resources.join("apiarist"), resources.join("textures/entity/apiarist.png"));
		initBlocksAndItems();
	}

	private void initBlocksAndItems()
	{
		beeBox  = new BlockTypeDefinition<BlockBeeBox>(new BlockBeeBox());
		beeBox.getBlock().setFlammability(20).setFireSpreadSpeed(5).setHarvestLevel("axe", 0);
		beeHive = new BlockDefinition(new BlockBeeHive());

		modules.preInit();
		register();
	}

	private void register()
	{
		// Bee Boxes
		GameRegistry.registerBlock(beeBox.getBlock(), ItemBlockBeeBox.class, "grc.beeBox");
		// Bee Hive(s)
		GameRegistry.registerBlock(beeHive.getBlock(), "grc.beeHive");
		// TileEntities
		GameRegistry.registerTileEntity(TileEntityBeeBox.class, "grc.tileentity.beeBox");
		GameRegistry.registerWorldGenerator(new WorldGeneratorBees(), 0);
		MapGenHelper.registerStructureComponent(ComponentVillageApiarist.class, "grc.apiarist");
		modules.register();
		registerRecipes();
		userBeesConfig.addDefault(items.bee.asStack()).setComment("Growthcraft's default bee");
		BeesRegistry.instance().addHoneyComb(items.honeyCombEmpty.asStack(), items.honeyCombFilled.asStack());
		userFlowersConfig.addDefault(Blocks.red_flower);
		userFlowersConfig.addDefault(Blocks.yellow_flower);
		if (BopPlatform.isLoaded())
		{
			userFlowersConfig.addDefault(
				new UserFlowerEntry("BiomesOPlenty", "flowers", OreDictionary.WILDCARD_VALUE)
					.setEntryType("forced"))
				.setComment("BiomesOPlenty flowers require a forced entry, in order for it to be placed by the bee box spawning.");
			userFlowersConfig.addDefault(
				new UserFlowerEntry("BiomesOPlenty", "flowers2", OreDictionary.WILDCARD_VALUE)
					.setEntryType("forced"))
				.setComment("BiomesOPlenty flowers require a forced entry, in order for it to be placed by the bee box spawning.");
		}
	}

	private void registerRecipes()
	{
		//====================
		// CRAFTING
		//====================
		final BlockDefinition planks = new BlockDefinition(Blocks.planks);
		for (int i = 0; i < 6; ++i)
		{
			GameRegistry.addRecipe(beeBox.asStack(1, i), new Object[] { " A ", "A A", "AAA", 'A', planks.asStack(1, i) });
		}

		final ItemStack honeyStack = items.honeyCombFilled.asStack();
		GameRegistry.addShapelessRecipe(items.honeyJar.asStack(),
			honeyStack, honeyStack, honeyStack, honeyStack, honeyStack, honeyStack, Items.flower_pot);
	}

	private void postRegisterRecipes()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(beeBox.asStack(), " A ", "A A", "AAA", 'A', "plankWood"));

		//GameRegistry.addRecipe(new ShapelessMultiRecipe(
		//		items.honeyJar.asStack(),
		//		new TaggedFluidStacks(1000, BeesFluidTag.HONEY.getName()),
		//		Items.flower_pot));
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerBees());
		final VillageHandlerBeesApiarist handler = new VillageHandlerBeesApiarist();
		VillagerRegistry.instance().register(apiaristProfession);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);
		logger.warn("(fixme) GrowthCraftBees registerVillageTradeHandler");
		//VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, new VillageHandlerBees());
		//VillagerRegistry.instance().registerVillageTradeHandler(config.villagerApiaristID, handler);
		modules.init();
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		userBeesConfig.loadUserConfig();
		userFlowersConfig.loadUserConfig();
		postRegisterRecipes();

		modules.postInit();
	}
}
