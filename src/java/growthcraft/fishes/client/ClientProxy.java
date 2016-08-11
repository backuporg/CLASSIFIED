package growthcraft.fishes.client;

import growthcraft.core.client.util.GrcModelRegistry;
import growthcraft.fishes.common.CommonProxy;
import growthcraft.fishes.GrowthCraftFishes;

public class ClientProxy extends CommonProxy
{
	private void registerBlockStates()
	{
		GrcModelRegistry.instance().register(GrowthCraftFishes.fishTrap, 0, GrowthCraftFishes.resources);
	}

	@Override
	public void init()
	{
		super.init();
		registerBlockStates();
	}
}
