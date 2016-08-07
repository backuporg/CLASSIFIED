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
package growthcraft.grapes.init;

import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.grapes.common.block.BlockGrapeBlock;
import growthcraft.grapes.common.block.BlockGrapeLeaves;
import growthcraft.grapes.common.block.BlockGrapeVineBine;
import growthcraft.grapes.common.block.BlockGrapeVineTrunk;

public class GrcGrapesBlocks extends GrcModuleBase
{
	public BlockTypeDefinition<BlockGrapeVineBine> grapeVineBine;
	public BlockTypeDefinition<BlockGrapeVineTrunk> grapeVineTrunk;
	public BlockTypeDefinition<BlockGrapeLeaves> grapeLeaves;
	public BlockDefinition grapeBlock;

	@Override
	public void preInit()
	{
		this.grapeVineBine  = new BlockTypeDefinition<BlockGrapeVineBine>(new BlockGrapeVineBine());
		this.grapeVineTrunk  = new BlockTypeDefinition<BlockGrapeVineTrunk>(new BlockGrapeVineTrunk());
		this.grapeLeaves = new BlockTypeDefinition<BlockGrapeLeaves>(new BlockGrapeLeaves());
		this.grapeBlock  = new BlockDefinition(new BlockGrapeBlock());
	}

	@Override
	public void register()
	{
		grapeVineBine.register("grc.grape_vine_bine");
		grapeVineTrunk.register("grc.grape_vine_trunk");
		grapeLeaves.register("grc.grape_leaves");
		grapeBlock.register("grc.grape_block");
	}
}
