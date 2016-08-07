package growthcraft.apples;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.apples.common.block.BlockApple;
import growthcraft.apples.common.block.BlockAppleLeaves;
import growthcraft.apples.common.block.BlockAppleSapling;
import growthcraft.apples.common.CommonProxy;
import growthcraft.apples.common.item.ItemAppleSeeds;
import growthcraft.apples.common.village.ComponentVillageAppleFarm;
import growthcraft.apples.common.village.VillageHandlerApples;
import growthcraft.apples.handler.AppleFuelHandler;
import growthcraft.apples.init.GrcApplesFluids;
import growthcraft.apples.init.GrcApplesRecipes;
//import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.ItemDefinition;
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
import net.minecraftforge.oredict.OreDictionary;

@Mod(
	modid = GrowthCraftApples.MOD_ID,
	name = GrowthCraftApples.MOD_NAME,
	version = GrowthCraftApples.MOD_VERSION,
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

	public static BlockDefinition appleSapling;
	public static BlockDefinition appleLeaves;
	public static BlockTypeDefinition<BlockApple> appleBlock;
	public static ItemDefinition appleSeeds;
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

		modules.add(fluids);
		modules.add(recipes);
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.apples.integration.ThaumcraftModule());
		if (config.debugEnabled) modules.setLogger(logger);

		appleSapling = new BlockDefinition(new BlockAppleSapling());
		appleLeaves = new BlockDefinition(new BlockAppleLeaves());
		appleBlock = new BlockTypeDefinition<BlockApple>(new BlockApple());

		appleSeeds = new ItemDefinition(new ItemAppleSeeds());

		modules.preInit();
		register();
	}

	public void register()
	{
		appleSapling.register("grc.appleSapling");
		appleLeaves.register("grc.appleLeaves");
		appleBlock.register("grc.appleBlock");
		appleSeeds.register("grc.appleSeeds");

		MapGenHelper.registerStructureComponent(ComponentVillageAppleFarm.class, "grc.applefarm");

		//====================
		// ADDITIONAL PROPS.
		//====================
		Blocks.fire.setFireInfo(appleLeaves.getBlock(), 30, 60);

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("saplingTree", appleSapling.getItem());
		OreDictionary.registerOre("treeSapling", appleSapling.getItem());
		OreDictionary.registerOre("seedApple", appleSeeds.getItem());
		OreDictionary.registerOre("treeLeaves", appleLeaves.asStack(1, OreDictionary.WILDCARD_VALUE));
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("listAllseed", appleSeeds.getItem());
		// Common
		OreDictionary.registerOre("foodApple", Items.apple);
		OreDictionary.registerOre("foodFruit", Items.apple);
		//====================
		// CRAFTING
		//====================
		GameRegistry.addShapelessRecipe(appleSeeds.asStack(), Items.apple);

		//====================
		// SMELTING
		//====================
		GameRegistry.registerFuelHandler(new AppleFuelHandler());

		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CommonProxy.instance.init();
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
