package growthcraft.bees.common.village;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.util.SchemaToVillage.BlockEntry;
import growthcraft.core.util.SchemaToVillage.IBlockEntries;
import growthcraft.core.util.SchemaToVillage.MultiBlockEntries;
import growthcraft.core.util.SchemaToVillage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.World;

public class ComponentVillageApiarist extends StructureVillagePieces.Village implements SchemaToVillage.IVillage
{
	// Design by Ar97x, modified by IceDragon (I made the tree levaves a 3x3x3 cube, makes it look neat)
	private static final String[][] apiaristExteriorSchema = {
		{
			"    486  ",
			"xcccccccx",
			"cpppppppc",
			"cpppppppc",
			"cpppppppc",
			"cpppppppc",
			"xcccccccx",
			"ddddddd  ",
			"ddddddd  ",
			"ddddddd  ",
			"ddddddd  ",
			"ddddddd  ",
			"         ",
			"         "
		},
		{
			"         ",
			"xpppp ppx",
			"p       p",
			"B       B",
			"B       B",
			"p    8  p",
			"xpp ppppx",
			"f,,,,,f  ",
			"f,,,,,f  ",
			"f,+,+,f  ",
			"f,,,,,f  ",
			"ffff ff  ",
			"         ",
			"         "
		},
		{
			"         ",
			"xpggp ppx",
			"p       p",
			"Y       Y",
			"Y       Y",
			"p       p",
			"xpp pgppx",
			"         ",
			"         ",
			"t     t  ",
			"    H    ",
			"   t     ",
			"         ",
			"         "
		},
		{
		// the torches for this level are placed manually
			"888888888",
			"xpppppppx",
			"p       p",
			"B       B",
			"B       B",
			"p       p",
			"xpppppppx",
			"222222222",
			"         ",
			"         ",
			"         ",
			"         ",
			"         ",
			"         "
		},
		{
			"         ",
			"888888888",
			"ppppppppp",
			"Yp     pY",
			"Yp     pY",
			"ppppppppp",
			"222222222",
			"         ",
			"         ",
			"         ",
			"         ",
			"         ",
			"         ",
			"         "
		},
		{
			"         ",
			"         ",
			"788888889",
			"4BYBYBYB6",
			"4BYBYBYB6",
			"122222223",
			"         ",
			"         ",
			"         ",
			"         ",
			"         ",
			"         ",
			"         ",
			"         "
		}
	};

	private static final String[][] apiaristInteriorSchema = {
		{
			" 6    4",
			" 6    K",
			" 6    K",
			"p6    4"
		},
		{
			"      l",
			"      K",
			"      K",
			"      l"
		},
		{
			" 8    l",
			" _    K",
			" _    K",
			" 2    l"
		},
		{
			"       ",
			" 2   4 ",
			" 8   4 ",
			"       "
		}
	};

	// That tree that appears behind the apiary, its a birch tree
	// Since `x` is already used for Oak in the original schema, I've extracted
	// here.
	private static final String[][] apiaristBackyardTreeSchema = {
		{
			"   ",
			" x ",
			"   ",
		},
		{
		// bee hive appears on this level
			"   ",
			" x ",
			"   ",
		},
		{
			"lll",
			"lxl",
			"lll",
		},
		{
			"lll",
			"lxl",
			"lll",
		},
		{
			" l ",
			"lxl",
			" l ",
		},
		{
			"   ",
			" l ",
			"   ",
		}
	};

	private static final WeightedRandomChestContent[] apiaristChestContents = new WeightedRandomChestContent[] {
		new WeightedRandomChestContent(GrowthCraftBees.items.bee.asStack(), 1, 2, 3),
		new WeightedRandomChestContent(GrowthCraftBees.items.honeyCombEmpty.asStack(), 1, 3, 5),
		new WeightedRandomChestContent(GrowthCraftBees.items.honeyCombFilled.asStack(), 1, 3, 5),
		new WeightedRandomChestContent(GrowthCraftBees.items.honeyJar.asStack(), 1, 1, 10),
		new WeightedRandomChestContent(GrowthCraftBees.blocks.beeBox.asStack(), 1, 2, 5)
	};

	// DO NOT REMOVE
	public ComponentVillageApiarist() {}

	public ComponentVillageApiarist(Start startPiece, EnumFacing facing, Random random, StructureBoundingBox boundingBox, int type)
	{
		super(startPiece, type);
		this.coordBaseMode = facing;
		this.boundingBox = boundingBox;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static ComponentVillageApiarist buildComponent(Start startPiece, List list, Random random, int x, int y, int z, EnumFacing facing, int type)
	{
		final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 8, 14, facing);
		if (canVillageGoDeeper(structureboundingbox)) {
			if (StructureComponent.findIntersecting(list, structureboundingbox) == null) {
				return new ComponentVillageApiarist(startPiece, facing, random, structureboundingbox, type);
			}
		}
		return null;
	}

	@Override
	public void placeBlockAtCurrentPositionPub(World world, BlockPos pos, IBlockState state, StructureBoundingBox box)
	{
		setBlockState(world, state, pos.getX(), pos.getY(), pos.getZ(), box);
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox box)
	{
		GrowthCraftBees.getLogger().warn("(fixme) ComponentVillageApiarist#addComponentParts");
		if (this.field_143015_k < 0)
		{
			this.field_143015_k = this.getAverageGroundLevel(world, box);
			if (this.field_143015_k < 0)
			{
				return true;
			}
			this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7, 0);
		}
		// clear entire bounding box
		this.fillWithBlocks(world, box, 0, 0, 0, 9, 8, 14, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
		final HashMap<Character, IBlockEntries> map = new HashMap<Character, IBlockEntries>();
		// Plop down the tree first
		//map.put('x', new BlockEntry(Blocks.log, 2));
		map.put('x', new BlockEntry(Blocks.log.getDefaultState()));
		//map.put('l', new BlockEntry(Blocks.leaves, 2));
		map.put('l', new BlockEntry(Blocks.leaves.getDefaultState()));
		SchemaToVillage.drawSchema(this, world, random, box, apiaristBackyardTreeSchema, map, 3, 1, 10);
		map.clear();
		map.put('c', new BlockEntry(Blocks.cobblestone.getDefaultState()));
		map.put('d', new BlockEntry(Blocks.grass.getDefaultState()));
		map.put('p', new BlockEntry(Blocks.planks.getDefaultState()));
		map.put('x', new BlockEntry(Blocks.log.getDefaultState()));
		map.put('l', new BlockEntry(Blocks.leaves.getDefaultState()));
		map.put('g', new BlockEntry(Blocks.glass_pane.getDefaultState()));
		map.put('f', new BlockEntry(Blocks.oak_fence.getDefaultState()));
		// high slab
		//map.put('-', new BlockEntry(Blocks.wooden_slab, 8));
		map.put('-', new BlockEntry(Blocks.wooden_slab.getDefaultState()));
		// low slab
		//map.put('_', new BlockEntry(Blocks.wooden_slab, 0));
		map.put('_', new BlockEntry(Blocks.wooden_slab.getDefaultState()));
		map.put(',', new MultiBlockEntries(new BlockEntry[]{
			//new BlockEntry(Blocks.red_flower, 4),
			new BlockEntry(Blocks.red_flower.getDefaultState()),
			//new BlockEntry(Blocks.red_flower, 5),
			new BlockEntry(Blocks.red_flower.getDefaultState()),
			//new BlockEntry(Blocks.red_flower, 6),
			new BlockEntry(Blocks.red_flower.getDefaultState()),
			//new BlockEntry(Blocks.red_flower, 7)
			new BlockEntry(Blocks.red_flower.getDefaultState())
		}));
		//map.put('1', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 2)));
		map.put('1', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//map.put('2', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 2)));
		map.put('2', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//map.put('3', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 2)));
		map.put('3', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//map.put('4', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 0)));
		map.put('4', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//map.put('6', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 1)));
		map.put('6', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//map.put('7', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 3)));
		map.put('7', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//map.put('8', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 3)));
		map.put('8', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//map.put('9', new BlockEntry(Blocks.oak_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 3)));
		map.put('9', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		map.put('t', new BlockEntry(Blocks.torch.getDefaultState()));
		//map.put('Y', new BlockEntry(Blocks.planks, 2));
		map.put('Y', new BlockEntry(Blocks.planks.getDefaultState()));
		//map.put('B', new BlockEntry(Blocks.planks, 1));
		map.put('B', new BlockEntry(Blocks.planks.getDefaultState()));
		//map.put('H', new BlockEntry(GrowthCraftBees.beeHive.getBlock(), this.getMetadataWithOffset(GrowthCraftBees.beeHive.getBlock(), 3)));
		map.put('H', new BlockEntry(GrowthCraftBees.blocks.beeHive.getBlock().getDefaultState()));
		//map.put('+', new BlockEntry(GrowthCraftBees.beeBox.getBlock(), this.getMetadataWithOffset(GrowthCraftBees.beeBox.getBlock(), random.nextInt(6))));
		map.put('+', new BlockEntry(GrowthCraftBees.blocks.beeBox.getBlock().getDefaultState()));
		SchemaToVillage.drawSchema(this, world, random, box, apiaristExteriorSchema, map, 0, 0, 0);
		/**
		 * Interior design
		 * Get ready to recycle for interior design
		 */
		map.clear();
		map.put('p', new BlockEntry(Blocks.planks.getDefaultState()));
		// inverted stairs for decorating interior
		//, this.getMetadataWithOffset(Blocks.oak_stairs, 2) | 4));
		map.put('1', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//, this.getMetadataWithOffset(Blocks.oak_stairs, 2) | 4));
		map.put('2', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//, this.getMetadataWithOffset(Blocks.oak_stairs, 2) | 4));
		map.put('3', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//, this.getMetadataWithOffset(Blocks.oak_stairs, 0) | 4));
		map.put('4', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//, this.getMetadataWithOffset(Blocks.oak_stairs, 1) | 4));
		map.put('6', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//, this.getMetadataWithOffset(Blocks.oak_stairs, 3) | 4));
		map.put('7', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//, this.getMetadataWithOffset(Blocks.oak_stairs, 3) | 4));
		map.put('8', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//, this.getMetadataWithOffset(Blocks.oak_stairs, 3) | 4));
		map.put('9', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		//map.put('_', new BlockEntry(Blocks.wooden_slab, 0));
		map.put('_', new BlockEntry(Blocks.wooden_slab.getDefaultState()));
		//map.put('K', new BlockEntry(Blocks.bookshelf, 0));
		map.put('K', new BlockEntry(Blocks.bookshelf.getDefaultState()));
		// metadata here is (1(spruce leaves) | 4(no decay))
		//map.put('l', new BlockEntry(Blocks.leaves, 1 | 4));
		map.put('l', new BlockEntry(Blocks.leaves.getDefaultState()));
		SchemaToVillage.drawSchema(this, world, random, box, apiaristInteriorSchema, map, 1, 1, 2);
		// Place torches
		this.setBlockState(world, Blocks.torch.getDefaultState(), 5, 3, 2, box);
		this.setBlockState(world, Blocks.torch.getDefaultState(), 4, 3, 5, box);
		/*
			TODO:
				place signs beside stairs to form a chair -
				If you happen to be a sign rotation expert, PLEASE DO FIX THIS.
		 */
		//setBlockState(world, Blocks.wall_sign, 2, 4, 1, 5, box);
		//setBlockState(world, Blocks.wall_sign, 2, 6, 1, 5, box);
		// Drop in the front door
		//placeDoorAtCurrentPosition(world, box, random, 5, 1, 1, getMetadataWithOffset(Blocks.wooden_door, 1));
		// Drop in the back door
		//placeDoorAtCurrentPosition(world, box, random, 3, 1, 6, getMetadataWithOffset(Blocks.wooden_door, 1));
		// Slap that nicely placed flower pot on the counter
		//setBlockState(world, Blocks.flower_pot, 3, 2, 2, 2, box);
		setBlockState(world, Blocks.flower_pot.getDefaultState(), 2, 2, 2, box);
		// Shove a chest behind the counter, filled with goodies
		GrowthCraftBees.getLogger().warn("(fixme) ComponentVillageApiarist generateStructureChestContents");
		//generateStructureChestContents(world, box, random, 1, 2, 5, apiaristChestContents, 3 + random.nextInt(6));
		// Fix some structural madness, like buildings spawning in the air and
		// floating on a thin layer of dirt/cobblestone
		for (int row = 0; row < 14; ++row)
		{
			for (int col = 0; col < 9; ++col)
			{
				clearCurrentPositionBlocksUpwards(world, col, 8, row, box);
				replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), col, -1, row, box);
			}
		}
		// Trap the villager behind the counter, so he shall forever sells us
		// apiary items... Poor guy.
		spawnVillagers(world, box, 1, 2, 3, 1);
		return true;
	}
}
