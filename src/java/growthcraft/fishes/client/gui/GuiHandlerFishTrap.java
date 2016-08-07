package growthcraft.fishes.client.gui;

import growthcraft.fishes.common.inventory.ContainerFishTrap;
import growthcraft.fishes.common.tileentity.TileEntityFishTrap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandlerFishTrap implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

		if (te instanceof TileEntityFishTrap)
		{
			return new ContainerFishTrap(player.inventory, (TileEntityFishTrap)te);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

		if (te instanceof TileEntityFishTrap)
		{
			return new GuiFishTrap(player.inventory, (TileEntityFishTrap)te);
		}

		return null;
	}
}
