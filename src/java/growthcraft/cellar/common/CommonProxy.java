package growthcraft.cellar.common;

import growthcraft.core.common.GrcModuleBase;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy extends GrcModuleBase
{
	@SidedProxy(clientSide="growthcraft.cellar.client.ClientProxy", serverSide="growthcraft.cellar.common.CommonProxy")
	public static CommonProxy instance;
}
