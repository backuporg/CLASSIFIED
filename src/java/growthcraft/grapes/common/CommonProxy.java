package growthcraft.grapes.common;

import growthcraft.core.common.GrcModuleBase;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy extends GrcModuleBase
{
	@SidedProxy(clientSide="growthcraft.grapes.client.ClientProxy", serverSide="growthcraft.grapes.common.CommonProxy")
	public static CommonProxy instance;
}
