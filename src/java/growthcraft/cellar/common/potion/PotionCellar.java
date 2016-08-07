package growthcraft.cellar.common.potion;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionCellar extends Potion
{
	private static final ResourceLocation resource = new ResourceLocation("grccellar" , "textures/guis/potion_tipsy.png");

	public PotionCellar(boolean badEffect, int potionColor)
	{
		super(resource, badEffect, potionColor);
		setIconIndex(0, 0);
		setPotionName("grc.potion.tipsy");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(resource);
		return true;
	}
}
