package growthcraft.core.common;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy extends GrcModuleBase
{
	@SidedProxy(clientSide="growthcraft.core.client.ClientProxy", serverSide="growthcraft.core.common.CommonProxy")
	public static CommonProxy instance;
}
