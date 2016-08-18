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
package growthcraft.core.init;

import growthcraft.core.common.block.BlockFenceRope;
import growthcraft.core.common.block.BlockRope;
import growthcraft.core.common.block.BlockSaltBlock;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.GrcModuleBlocks;
import growthcraft.core.common.item.ItemBlockFenceRope;
import growthcraft.core.registry.FenceRopeRegistry;

import net.minecraft.init.Blocks;
import net.minecraftforge.oredict.OreDictionary;

public class GrcCoreBlocks extends GrcModuleBlocks
{
	public BlockDefinition ropeBlock;
	public BlockDefinition saltBlock;
	public BlockDefinition oakFenceRope;
	public BlockDefinition spruceFenceRope;
	public BlockDefinition birchFenceRope;
	public BlockDefinition jungleFenceRope;
	public BlockDefinition darkOakFenceRope;
	public BlockDefinition acaciaFenceRope;
	public BlockDefinition netherBrickFenceRope;

	@Override
	public void preInit()
	{
		this.saltBlock = newDefinition(new BlockSaltBlock());
		this.ropeBlock = newDefinition(new BlockRope());
		this.oakFenceRope = newDefinition(new BlockFenceRope(Blocks.oak_fence, "oak_fence_rope"));
		this.spruceFenceRope = newDefinition(new BlockFenceRope(Blocks.spruce_fence, "spruce_fence_rope"));
		this.birchFenceRope = newDefinition(new BlockFenceRope(Blocks.birch_fence, "birch_fence_rope"));
		this.jungleFenceRope = newDefinition(new BlockFenceRope(Blocks.jungle_fence, "jungle_fence_rope"));
		this.darkOakFenceRope = newDefinition(new BlockFenceRope(Blocks.dark_oak_fence, "dark_oak_fence_rope"));
		this.acaciaFenceRope = newDefinition(new BlockFenceRope(Blocks.acacia_fence, "acacia_fence_rope"));
		this.netherBrickFenceRope = newDefinition(new BlockFenceRope(Blocks.nether_brick_fence, "nether_brick_fence_rope"));

		FenceRopeRegistry.instance().addEntry(Blocks.oak_fence, oakFenceRope.getBlock());
		FenceRopeRegistry.instance().addEntry(Blocks.spruce_fence, spruceFenceRope.getBlock());
		FenceRopeRegistry.instance().addEntry(Blocks.birch_fence, birchFenceRope.getBlock());
		FenceRopeRegistry.instance().addEntry(Blocks.jungle_fence, jungleFenceRope.getBlock());
		FenceRopeRegistry.instance().addEntry(Blocks.dark_oak_fence, darkOakFenceRope.getBlock());
		FenceRopeRegistry.instance().addEntry(Blocks.acacia_fence, acaciaFenceRope.getBlock());
		FenceRopeRegistry.instance().addEntry(Blocks.nether_brick_fence, netherBrickFenceRope.getBlock());
	}

	@Override
	public void register()
	{
		oakFenceRope.register("oak_fence_rope", ItemBlockFenceRope.class);
		spruceFenceRope.register("spruce_fence_rope", ItemBlockFenceRope.class);
		birchFenceRope.register("birch_fence_rope", ItemBlockFenceRope.class);
		jungleFenceRope.register("jungle_fence_rope", ItemBlockFenceRope.class);
		darkOakFenceRope.register("dark_oak_fence_rope", ItemBlockFenceRope.class);
		acaciaFenceRope.register("acacia_fence_rope", ItemBlockFenceRope.class);
		ropeBlock.register("rope_block");
		saltBlock.register("salt_block");
		netherBrickFenceRope.register("nether_brick_fence_rope", ItemBlockFenceRope.class);

		Blocks.fire.setFireInfo(oakFenceRope.getBlock(), 5, 20);
		Blocks.fire.setFireInfo(spruceFenceRope.getBlock(), 5, 20);
		Blocks.fire.setFireInfo(birchFenceRope.getBlock(), 5, 20);
		Blocks.fire.setFireInfo(jungleFenceRope.getBlock(), 5, 20);
		Blocks.fire.setFireInfo(darkOakFenceRope.getBlock(), 5, 20);
		Blocks.fire.setFireInfo(acaciaFenceRope.getBlock(), 5, 20);
	}

	@Override
	public void init()
	{
		OreDictionary.registerOre("blockSalt", saltBlock.getItem());

		//if (GrowthCraftCore.getConfig().enableEtfuturumIntegration) initEtfuturum();
		//if (GrowthCraftCore.getConfig().enableWoodstuffIntegration) initWoodstuff();
		//if (GrowthCraftCore.getConfig().enableNaturaIntegration) initNatura();
	}
}
