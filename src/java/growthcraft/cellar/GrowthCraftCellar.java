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
package growthcraft.cellar;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import growthcraft.api.cellar.booze.BoozeEntry;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.booze.effect.EffectTipsy;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.heatsource.user.UserHeatSourceEntry;
import growthcraft.api.cellar.heatsource.user.UserHeatSourcesConfig;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.core.util.DomainResourceLocationFactory;
import growthcraft.cellar.common.booze.ModifierFunctionExtended;
import growthcraft.cellar.common.booze.ModifierFunctionHyperExtended;
import growthcraft.cellar.common.booze.ModifierFunctionPotent;
import growthcraft.cellar.common.CommonProxy;
import growthcraft.cellar.common.item.EnumYeast;
import growthcraft.cellar.common.item.ItemChievDummy;
import growthcraft.cellar.common.item.ItemWaterBag;
import growthcraft.cellar.common.item.ItemYeast;
import growthcraft.cellar.common.potion.PotionCellar;
import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;
import growthcraft.cellar.common.tileentity.TileEntityCultureJar;
import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.common.tileentity.TileEntityFruitPress;
import growthcraft.cellar.common.tileentity.TileEntityFruitPresser;
import growthcraft.cellar.common.village.ComponentVillageTavern;
import growthcraft.cellar.common.village.VillageHandlerCellar;
import growthcraft.cellar.creativetab.CreativeTabsCellar;
import growthcraft.cellar.eventhandler.EventHandlerCauldronUseItem;
import growthcraft.cellar.eventhandler.EventHandlerItemCraftedEventCellar;
import growthcraft.cellar.eventhandler.EventHandlerLivingUpdateEventCellar;
import growthcraft.cellar.handler.GuiHandlerCellar;
import growthcraft.cellar.init.GrcCellarBlocks;
import growthcraft.cellar.network.PacketPipeline;
import growthcraft.cellar.stats.CellarAchievement;
import growthcraft.cellar.stats.GrcCellarAchievements;
import growthcraft.cellar.util.CellarBoozeBuilderFactory;
import growthcraft.cellar.util.GrcCellarUserApis;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.MapGenHelper;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Mod(
	modid = GrowthCraftCellar.MOD_ID,
	name = GrowthCraftCellar.MOD_NAME,
	version = GrowthCraftCellar.MOD_VERSION,
	acceptedMinecraftVersions = GrowthCraftCore.MOD_ACC_MINECRAFT,
	dependencies = "required-after:Growthcraft@@VERSION@"
)
public class GrowthCraftCellar
{
	public static final String MOD_ID = "Growthcraft|Cellar";
	public static final String MOD_NAME = "Growthcraft Cellar";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftCellar instance;
	public static final DomainResourceLocationFactory resources = new DomainResourceLocationFactory("grccellar");
	public static CreativeTabs tab;
	public static final GrcCellarBlocks blocks = new GrcCellarBlocks();
	public static VillagerProfession brewerProfession;
	public static ItemDefinition yeast;
	public static ItemDefinition waterBag;

	public static Potion potionTipsy;

	// Achievments
	public static ItemDefinition chievItemDummy;
	public static GrcCellarAchievements achievements;

	// Network
	public static final PacketPipeline packetPipeline = new PacketPipeline();
	public static CellarBoozeBuilderFactory boozeBuilderFactory;

	// Events
	public static final EventBus CELLAR_BUS = new EventBus();

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcCellarConfig config = new GrcCellarConfig();
	private GrcCellarUserApis userApis = new GrcCellarUserApis();
	private ModuleContainer modules = new ModuleContainer();

	public static UserHeatSourcesConfig getUserHeatSources()
	{
		return instance.userApis.getUserHeatSources();
	}

	public static GrcCellarConfig getConfig()
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
		config.load(event.getModConfigurationDirectory(), "growthcraft/cellar.conf");

		if (config.debugEnabled)
		{
			logger.debug("Pre-Initializing %s", MOD_ID);
			CellarRegistry.instance().setLogger(logger);
		}

		modules.add(blocks);

		if (config.enableWailaIntegration) modules.add(new growthcraft.cellar.integration.Waila());
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.cellar.integration.ThaumcraftModule());
		// ALWAYS set the user modules as last, this ensures that other modules are given a chance to setup defaults and such.
		modules.add(userApis);
		modules.add(CommonProxy.instance);
		if (config.debugEnabled) modules.setLogger(logger);
		modules.freeze();

		userApis.getUserBrewingRecipes()
			.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/cellar/brewing.json");
		userApis.getUserCultureRecipes()
			.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/cellar/culturing.json");
		userApis.getUserFermentingRecipes()
			.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/cellar/fermenting.json");
		userApis.getUserHeatSources()
			.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/cellar/heatsources.json");
		userApis.getUserPressingRecipes()
			.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/cellar/pressing.json");
		userApis.getUserYeastEntries()
			.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/cellar/yeast.json");

		registerBoozeModifierFunctions();
		boozeBuilderFactory = new CellarBoozeBuilderFactory(userApis);

		//====================
		// INIT
		//====================
		tab = new CreativeTabsCellar("creative_tab_grccellar");

		yeast = new ItemDefinition(new ItemYeast());
		waterBag = new ItemDefinition(new ItemWaterBag());
		chievItemDummy = new ItemDefinition(new ItemChievDummy());

		brewerProfession = new VillagerProfession(resources.join("brewer"), resources.join("textures/entity/brewer.png"));
		modules.preInit();
		register();
	}

	private void addDefaultHeatSources()
	{
		userApis.getUserHeatSources().addDefault("minecraft", "fire", UserHeatSourceEntry.newWildcardHeat(1.0f))
			.setComment("Fire!");
		userApis.getUserHeatSources().addDefault("minecraft", "flowing_lava", UserHeatSourceEntry.newWildcardHeat(0.7f))
			.setComment("We need to register both states of lava, this when its flowing");
		userApis.getUserHeatSources().addDefault("minecraft", "lava", UserHeatSourceEntry.newWildcardHeat(0.7f))
			.setComment("And when its a still pool.");
	}

	private void registerBoozeModifierFunctions()
	{
		CellarRegistry.instance().booze().setModifierFunction(BoozeTag.POTENT, new ModifierFunctionPotent());
		CellarRegistry.instance().booze().setModifierFunction(BoozeTag.EXTENDED, new ModifierFunctionExtended());
		CellarRegistry.instance().booze().setModifierFunction(BoozeTag.HYPER_EXTENDED, new ModifierFunctionHyperExtended());
	}

	private void register()
	{
		addDefaultHeatSources();
		//====================
		// REGISTRIES
		//====================
		modules.register();

		GameRegistry.registerItem(yeast.getItem(), "grc.yeast");
		GameRegistry.registerItem(waterBag.getItem(), "grc.water_bag");
		GameRegistry.registerItem(chievItemDummy.getItem(), "grc.chiev_item_dummy");

		GameRegistry.registerTileEntity(TileEntityFruitPress.class, "grc.tileentity.fruit_press");
		GameRegistry.registerTileEntity(TileEntityFruitPresser.class, "grc.tileentity.fruit_presser");
		GameRegistry.registerTileEntity(TileEntityBrewKettle.class, "grc.tileentity.brew_kettle");
		GameRegistry.registerTileEntity(TileEntityFermentBarrel.class, "grc.tileentity.ferment_barrel");
		GameRegistry.registerTileEntity(TileEntityCultureJar.class, "grc.tileentity.culture_jar");

		MapGenHelper.registerStructureComponent(ComponentVillageTavern.class, "grc.tavern");

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(blocks.fruitPress.asStack(), "ABA", "CCC", "AAA", 'A', "plankWood", 'B', Blocks.piston,'C', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(blocks.brewKettle.asStack(), "A", 'A', Items.cauldron));
		GameRegistry.addRecipe(new ShapedOreRecipe(blocks.fermentBarrel.asStack(), "AAA", "BBB", "AAA", 'B', "plankWood", 'A', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(blocks.cultureJar.asStack(), "GAG", "G G", "GGG", 'A', "plankWood", 'G', "paneGlass"));

		GameRegistry.addRecipe(new ShapedOreRecipe(waterBag.asStack(1, 16), "AAA", "ABA", "AAA", 'A', Items.leather, 'B', "materialRope"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 0), "dyeWhite", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 1), "dyeOrange", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 2), "dyeMagenta", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 3), "dyeLightBlue", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 4), "dyeYellow", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 5), "dyeLime", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 6), "dyePink", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 7), "dyeGray", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 8), "dyeLightGray", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 9), "dyeCyan", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 10), "dyePurple", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 11), "dyeBlue", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 12), "dyeBrown", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 13), "dyeGreen", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 14), "dyeRed", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(waterBag.asStack(1, 15), "dyeBlack", waterBag.asStack(1, OreDictionary.WILDCARD_VALUE)));

		//====================
		// POTION
		//====================
		extendPotionsArray();
		potionTipsy = new PotionCellar(false, 0);
		EffectTipsy.potionTipsy = potionTipsy;
		EffectTipsy.achievement = CellarAchievement.GET_DRUNK;

		//====================
		// ACHIEVEMENTS
		//====================
		achievements = new GrcCellarAchievements();
	}

	private void extendPotionsArray()
	{
		final int newSize = 1024;
		for (Field f : Potion.class.getDeclaredFields())
		{
			f.setAccessible(true);
			try
			{
				if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a"))
				{
					final Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);
					final Potion[] potionTypes = (Potion[])f.get(null);
					if (potionTypes.length < newSize)
					{
						logger.warn("Resizing PotionTypes array from %d to %d", potionTypes.length, newSize);
						final Potion[] newPotionTypes = new Potion[newSize];
						System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
						f.set(null, newPotionTypes);
					}
				}
			}
			catch (Exception e)
			{
				System.err.println("GrowthCraft|Cellar has encountered a problem with the built-in potionTypes Array, please report this problem to the mod authors.");
				e.printStackTrace();
			}
		}
	}

	private void registerOres()
	{
		OreDictionary.registerOre("materialYeast", yeast.getItem());
		OreDictionary.registerOre("yeastBrewers", EnumYeast.BREWERS.asStack());
		OreDictionary.registerOre("yeastLager", EnumYeast.LAGER.asStack());
		OreDictionary.registerOre("yeastBayanus", EnumYeast.BAYANUS.asStack());
		OreDictionary.registerOre("yeastEthereal", EnumYeast.ETHEREAL.asStack());
		OreDictionary.registerOre("yeastOrigin", EnumYeast.ORIGIN.asStack());
	}

	private void registerYeast()
	{
		CellarRegistry.instance().yeast().addYeast(EnumYeast.BREWERS.asStack());
		CellarRegistry.instance().yeast().addYeast(EnumYeast.LAGER.asStack());
		CellarRegistry.instance().yeast().addYeast(EnumYeast.BAYANUS.asStack());
		CellarRegistry.instance().yeast().addYeast(EnumYeast.ETHEREAL.asStack());
		CellarRegistry.instance().yeast().addYeast(EnumYeast.ORIGIN.asStack());
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		registerOres();
		registerYeast();
		packetPipeline.initialise();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerCellar());
		VillagerRegistry.instance().register(brewerProfession);
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageHandlerCellar());
		modules.init();
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		userApis.loadConfigs();
		packetPipeline.postInitialise();
		MinecraftForge.EVENT_BUS.register(new EventHandlerItemCraftedEventCellar());
		MinecraftForge.EVENT_BUS.register(new EventHandlerLivingUpdateEventCellar());
		MinecraftForge.EVENT_BUS.register(new EventHandlerCauldronUseItem());

		modules.postInit();
		if (!config.boozeEffectsEnabled)
		{
			logger.debug("Stripping ALL booze effects except tipsy");
			for (BoozeEntry entry : CellarRegistry.instance().booze().getBoozeEntries())
			{
				entry.getEffect().clearEffects();
			}
		}
	}
}
