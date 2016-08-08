package growthcraft.cellar.common.tileentity;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.common.block.BlockFruitPresser;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityFruitPresser extends TileEntity implements ITickable
{
	public float trans;
	public float transPrev;

	private float transSpd = 0.21875F;
	private float transMin;
	private float transMax = 0.4375F;

	@Override
	public void update()
	{
		if (GrowthCraftCellar.blocks.fruitPresser.equals(worldObj.getBlockState(getPos()).getBlock()))
		{
			invalidate();
		}
		final IBlockState state = this.worldObj.getBlockState(getPos());
		final boolean pressState = state.getValue(BlockFruitPresser.PRESS_STATE);
		this.transPrev = this.trans;
		if (pressState && this.trans > this.transMin)
		{
			this.trans -= this.transSpd;
		}
		else if (!pressState && this.trans < this.transMax)
		{
			this.trans += this.transSpd;
		}
	}

	public float getTranslation()
	{
		return this.trans;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.trans = nbt.getFloat("trans");
		this.transPrev = nbt.getFloat("transprev");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setFloat("trans", trans);
		nbt.setFloat("transprev", transPrev);
	}

	@Override
	@SuppressWarnings({"rawtypes"})
	public Packet getDescriptionPacket()
	{
		final NBTTagCompound nbtTag = new NBTTagCompound();
		writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(getPos(), 128, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		readFromNBT(packet.getNbtCompound());
	}
}
