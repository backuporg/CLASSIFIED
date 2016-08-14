/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.core.client.util;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.util.DomainResourceLocationFactory;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GrcModelRegistry
{
	private static GrcModelRegistry instance_;
	private final ILogger logger = new GrcLogger("grcmodel_registry");

	public static GrcModelRegistry instance()
	{
		if (instance_ == null)
		{
			instance_ = new GrcModelRegistry();
		}
		return instance_;
	}

	public void registerItem(Item item, int meta, ModelResourceLocation location)
	{
		GrowthCraftCore.getLogger().info("Registering Model item=%s meta=%d location=%s", item, meta, location);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, location);
	}

	public void register(BlockTypeDefinition<? extends Block> def, int meta, ModelResourceLocation location)
	{
		registerItem(def.getItem(), meta, location);
	}

	public void register(BlockTypeDefinition<? extends Block> def, int meta, DomainResourceLocationFactory drl)
	{
		if (def != null)
		{
			register(def, meta, drl.createModel(def.registeredName, "inventory"));
		}
		else
		{
			logger.error("null BlockTypeDefinition defintion passed in for registering!");
		}
	}
}
