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
package growthcraft.core.client.renderer.block.statemap;

import growthcraft.api.core.util.DomainResourceLocationFactory;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GrcDomainStateMapper extends StateMapperBase
{
	protected final DomainResourceLocationFactory reslocFactory;

	public GrcDomainStateMapper(DomainResourceLocationFactory p_reslocFactory)
	{
		this.reslocFactory = p_reslocFactory;
	}

	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		final ResourceLocation res = Block.blockRegistry.getNameForObject(state.getBlock());
		final ModelResourceLocation result = reslocFactory.createModel(res.getResourcePath(), getPropertyString(state.getProperties()));
		GrowthCraftCore.getLogger().info("getModelResourceLocation state=`%s` source='%s' result='%s'", state, res, result);
		return result;
	}
}
