package growthcraft.bees.common;

import growthcraft.core.common.GrcModuleBase;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy extends GrcModuleBase
{
	@SidedProxy(clientSide="growthcraft.bees.client.ClientProxy", serverSide="growthcraft.bees.common.CommonProxy")
	public static CommonProxy instance;
}
