package growthcraft.cellar.client;

import growthcraft.api.core.util.DomainResourceLocationFactory;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.common.CommonProxy;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.client.util.GrcModelRegistry;

public class ClientProxy extends CommonProxy
{
	private void registerBlockStates()
	{
		final GrcModelRegistry gmr = GrcModelRegistry.instance();
		final DomainResourceLocationFactory res = GrowthCraftCellar.resources;
		gmr.register(GrowthCraftCellar.blocks.brewKettle, 0, res.createModel("brew_kettle", "inventory"));
		gmr.register(GrowthCraftCellar.blocks.cultureJar, 0, res.createModel("culture_jar", "inventory"));
		gmr.register(GrowthCraftCellar.blocks.fermentBarrel, 0, res.createModel("ferment_barrel", "inventory"));
		gmr.register(GrowthCraftCellar.blocks.fruitPress, 0, res.createModel("fruit_press", "inventory"));
		gmr.register(GrowthCraftCellar.blocks.fruitPresser, 0, res.createModel("fruit_presser", "inventory"));
	}

	@Override
	public void init()
	{
		super.init();
		new GrcCellarResources();
		registerBlockStates();
	}
}
