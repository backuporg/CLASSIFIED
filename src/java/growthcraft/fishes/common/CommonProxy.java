package growthcraft.fishes.common;

import growthcraft.core.common.GrcModuleBase;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy extends GrcModuleBase
{
	@SidedProxy(clientSide="growthcraft.fishes.client.ClientProxy", serverSide="growthcraft.fishes.common.CommonProxy")
	public static CommonProxy instance;
}
