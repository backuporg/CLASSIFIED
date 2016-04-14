package growthcraft.core.eventhandler;

import java.util.HashMap;
import java.util.Map;

import growthcraft.api.core.util.BlockFlags;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerInteractEventPaddy
{
	public static Map<Block, Block> paddyBlocks = new HashMap<Block, Block>();

	@SubscribeEvent
	public void PlayerInteract(PlayerInteractEvent event)
	{
		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			if (event.face != 1) return;

			final EntityPlayer player = event.entityPlayer;
			final ItemStack itemstack = player.getCurrentEquippedItem();
			if (itemstack != null && itemstack.getItem() instanceof ItemSpade)
			{
				final World world = player.worldObj;
				final IBlockState targetBlock = world.getBlockState(event.pos);
				final Block paddyBlock = paddyBlocks.get(targetBlock);
				if (paddyBlock != null)
				{
					world.setBlockState(event.pos, paddyBlock.getDefaultState(), BlockFlags.UPDATE_AND_SYNC);
					world.playSoundEffect(
						(double)event.pos.getX() + 0.5D,
						(double)event.pos.getY() + 0.5D,
						(double)event.pos.getZ() + 0.5D,
						paddyBlock.stepSound.getPlaceSound(),
						(paddyBlock.stepSound.getVolume() + 1.0F) / 2.0F,
						paddyBlock.stepSound.getPitch() * 0.8F);
					itemstack.damageItem(1, player);
				}
			}
		}
	}
}
