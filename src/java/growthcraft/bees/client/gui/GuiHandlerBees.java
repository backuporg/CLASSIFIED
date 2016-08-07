package growthcraft.bees.client.gui;

import growthcraft.bees.common.inventory.ContainerBeeBox;
import growthcraft.bees.common.tileentity.TileEntityBeeBox;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandlerBees implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		if (te instanceof TileEntityBeeBox)
		{
			return new ContainerBeeBox(player.inventory, (TileEntityBeeBox)te);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		if (te instanceof TileEntityBeeBox)
		{
			return new GuiBeeBox(player.inventory, (TileEntityBeeBox)te);
		}
		return null;
	}
}
