package growthcraft.fishes.common;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.fishes.client.ClientProxy", serverSide="growthcraft.fishes.common.CommonProxy")
	public static CommonProxy instance;

	public void init() {}
}
