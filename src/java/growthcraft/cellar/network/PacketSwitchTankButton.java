package growthcraft.cellar.network;

import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;

public class PacketSwitchTankButton extends AbstractPacketButton
{
	public PacketSwitchTankButton() {}

	public PacketSwitchTankButton(BlockPos pos)
	{
		super(pos);
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{

	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		final World world = player.worldObj;
		final TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileEntityBrewKettle)
		{
			((TileEntityBrewKettle)te).switchTanks();
		}
	}
}
