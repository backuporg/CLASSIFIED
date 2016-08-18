package growthcraft.hops.common;

import growthcraft.core.common.GrcModuleBase;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy extends GrcModuleBase
{
	@SidedProxy(clientSide="growthcraft.hops.client.ClientProxy", serverSide="growthcraft.hops.common.CommonProxy")
	public static CommonProxy instance;
}
