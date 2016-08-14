package growthcraft.apples.common;

import growthcraft.core.common.GrcModuleBase;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy extends GrcModuleBase
{
	@SidedProxy(clientSide="growthcraft.apples.client.ClientProxy", serverSide="growthcraft.apples.common.CommonProxy")
	public static CommonProxy instance;
}
