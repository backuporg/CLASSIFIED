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
package growthcraft.bees.init;

import growthcraft.bees.common.item.EnumBeesWax;
import growthcraft.bees.common.item.ItemBee;
import growthcraft.bees.common.item.ItemBeesWax;
import growthcraft.bees.common.item.ItemHoneyCombEmpty;
import growthcraft.bees.common.item.ItemHoneyCombFilled;
import growthcraft.bees.common.item.ItemHoneyJar;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleItems;

import net.minecraftforge.oredict.OreDictionary;

public class GrcBeesItems extends GrcModuleItems
{
	public ItemDefinition honeyCombEmpty;
	public ItemDefinition honeyCombFilled;
	public ItemDefinition honeyJar;
	public ItemDefinition bee;
	public ItemDefinition beesWax;

	@Override
	public void preInit()
	{
		this.honeyCombEmpty = newDefinition(new ItemHoneyCombEmpty());
		this.honeyCombFilled = newDefinition(new ItemHoneyCombFilled());
		this.honeyJar = newDefinition(new ItemHoneyJar());
		this.bee = newDefinition(new ItemBee());
		this.beesWax = newDefinition(new ItemBeesWax());
	}

	@Override
	public void register()
	{
		honeyCombEmpty.register("honey_comb_empty");
		honeyCombFilled.register("honey_comb_filled");
		honeyJar.register("honey_jar");
		bee.register("bee");
		beesWax.register("bees_wax");
	}

	@Override
	public void init()
	{
		OreDictionary.registerOre("materialWax", beesWax.getItem());
		OreDictionary.registerOre("materialPressedwax", beesWax.getItem());
		OreDictionary.registerOre("materialBeeswax", beesWax.getItem());
		OreDictionary.registerOre("materialBeeswaxBlack", EnumBeesWax.BLACK.asStack());
		OreDictionary.registerOre("materialBeeswaxRed", EnumBeesWax.RED.asStack());
		OreDictionary.registerOre("beeQueen", bee.getItem());
		OreDictionary.registerOre("materialWaxcomb", honeyCombEmpty.asStack());
		OreDictionary.registerOre("beeComb", honeyCombEmpty.asStack());
		OreDictionary.registerOre("materialHoneycomb", honeyCombFilled.asStack());
		OreDictionary.registerOre("beeComb", honeyCombFilled.asStack());
		OreDictionary.registerOre("honeyDrop", honeyJar.getItem());
		OreDictionary.registerOre("dropHoney", honeyJar.getItem());
		OreDictionary.registerOre("bucketHoney", honeyJar.getItem());
	}
}
