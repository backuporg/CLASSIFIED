package growthcraft.core.client;

import growthcraft.api.core.util.DomainResourceLocationFactory;
import growthcraft.core.client.util.GrcModelRegistry;
import growthcraft.core.common.CommonProxy;
import growthcraft.core.GrowthCraftCore;

public class ClientProxy extends CommonProxy
{
	private void registerBlockStates()
	{
		final GrcModelRegistry gmr = GrcModelRegistry.instance();
		final DomainResourceLocationFactory res = GrowthCraftCore.resources;
		gmr.register(GrowthCraftCore.blocks.ropeBlock, 0, res);
		gmr.register(GrowthCraftCore.blocks.saltBlock, 0, res);
		gmr.register(GrowthCraftCore.blocks.oakFenceRope, 0, res);
		gmr.register(GrowthCraftCore.blocks.spruceFenceRope, 0, res);
		gmr.register(GrowthCraftCore.blocks.birchFenceRope, 0, res);
		gmr.register(GrowthCraftCore.blocks.jungleFenceRope, 0, res);
		gmr.register(GrowthCraftCore.blocks.darkOakFenceRope, 0, res);
		gmr.register(GrowthCraftCore.blocks.acaciaFenceRope, 0, res);
		gmr.register(GrowthCraftCore.blocks.netherBrickFenceRope, 0, res);
	}

	@Override
	public void init()
	{
		super.init();
		registerBlockStates();
	}
}
