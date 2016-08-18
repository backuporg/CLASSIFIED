package growthcraft.bamboo.common;

import growthcraft.core.common.GrcModuleBase;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy extends GrcModuleBase
{
	@SidedProxy(clientSide="growthcraft.bamboo.client.ClientProxy", serverSide="growthcraft.bamboo.common.CommonProxy")
	public static CommonProxy instance;
}
