package growthcraft.bamboo.client;

import growthcraft.api.core.util.DomainResourceLocationFactory;
import growthcraft.core.client.util.GrcModelRegistry;
import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.common.CommonProxy;

public class ClientProxy extends CommonProxy
{
	private void registerBlockStates()
	{
		final GrcModelRegistry gmr = GrcModelRegistry.instance();
		final DomainResourceLocationFactory res = GrowthCraftBamboo.resources;
		gmr.register(GrowthCraftBamboo.blocks.bambooBlock, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooShoot, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooStalk, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooLeaves, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooFence, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooFenceRope, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooWall, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooStairs, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooSingleSlab, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooDoubleSlab, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooDoor, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooFenceGate, 0, res);
		gmr.register(GrowthCraftBamboo.blocks.bambooScaffold, 0, res);
	}

	@Override
	public void init()
	{
		super.init();
		registerBlockStates();
	}
}
