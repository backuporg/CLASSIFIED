package growthcraft.rice.common;

import growthcraft.core.common.GrcModuleBase;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy extends GrcModuleBase
{
	@SidedProxy(clientSide="growthcraft.rice.client.ClientProxy", serverSide="growthcraft.rice.common.CommonProxy")
	public static CommonProxy instance;
}
