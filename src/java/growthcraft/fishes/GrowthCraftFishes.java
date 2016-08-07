package growthcraft.fishes;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.fishtrap.FishTrapEntry;
import growthcraft.api.fishtrap.user.UserFishTrapConfig;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.fishes.client.gui.GuiHandlerFishTrap;
import growthcraft.fishes.common.block.BlockFishTrap;
import growthcraft.fishes.common.CommonProxy;
import growthcraft.fishes.common.tileentity.TileEntityFishTrap;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftFishes.MOD_ID,
	name = GrowthCraftFishes.MOD_NAME,
	version = GrowthCraftFishes.MOD_VERSION,
	dependencies = "required-after:Growthcraft@@VERSION@"
)
public class GrowthCraftFishes
{
	public static final String MOD_ID = "Growthcraft|Fishes";
	public static final String MOD_NAME = "Growthcraft Fishes";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftFishes instance;

	public static BlockDefinition fishTrap;

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcFishtrapConfig config = new GrcFishtrapConfig();
	private ModuleContainer modules = new ModuleContainer();
	private UserFishTrapConfig userFishTrapConfig = new UserFishTrapConfig();

	public static GrcFishtrapConfig getConfig()
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
		config.load(event.getModConfigurationDirectory(), "growthcraft/fishtrap.conf");

		userFishTrapConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/fishtrap/entries.json");
		modules.add(userFishTrapConfig);

		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.fishes.integration.ThaumcraftModule());

		if (config.debugEnabled) modules.setLogger(logger);

		//====================
		// INIT
		//====================
		fishTrap = new BlockDefinition(new BlockFishTrap());

		modules.preInit();
		register();
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(fishTrap.getBlock(), "grc.fish_trap");

		GameRegistry.registerTileEntity(TileEntityFishTrap.class, "grc.tileentity.fish_trap");

		// Will use same chances as Fishing Rods
		//JUNK
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.leather_boots), 10).setDamage(0.9F));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.leather), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.bone), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.potionitem), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.string), 5));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.fishing_rod), 2).setDamage(0.9F));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.bowl), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.stick), 5));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.dye, 10), 1));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Blocks.tripwire_hook), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.rotten_flesh), 10));
		//TREASURE
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Blocks.waterlily), 1));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.name_tag), 1));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.saddle), 1));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.bow), 1).setDamage(0.25F).setEnchantable());
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.fishing_rod), 1).setDamage(0.25F).setEnchantable());
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.book), 1).setEnchantable());
		//FISH
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 60));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.getMetadata()), 25));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), 2));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()), 13));

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(fishTrap.asStack(1), "ACA", "CBC", "ACA", 'A', "plankWood", 'B', Items.lead, 'C', Items.string));

		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CommonProxy.instance.init();
		userFishTrapConfig.loadUserConfig();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerFishTrap());
		modules.init();
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
