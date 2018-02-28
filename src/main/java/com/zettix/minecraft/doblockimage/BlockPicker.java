package com.zettix.minecraft.doblockimage;

import java.util.HashMap;
import org.bukkit.Material;

public class BlockPicker {
	
	public HashMap<Material, int[]> block_and_radius;
		
	public BlockPicker ( ) {
		// These are hard coded from a utility I wrote in python.
		// It basically averages the rgb values from one of the block textures.
		block_and_radius = new HashMap<Material, int[]>();
        block_and_radius.put(Material.BRICK, new int[] {146, 99,86});
        block_and_radius.put(Material.CACTUS, new int[] {12, 92,22});
        block_and_radius.put(Material.CLAY, new int[] {158, 164,176});
        block_and_radius.put(Material.COAL_BLOCK, new int[] {18, 18,18});
        block_and_radius.put(Material.COAL_ORE, new int[] {115, 115,115});
        block_and_radius.put(Material.COBBLESTONE, new int[] {122, 122,122});
        block_and_radius.put(Material.DIAMOND_BLOCK, new int[] {97, 219,213});
        block_and_radius.put(Material.DIAMOND_ORE, new int[] {129, 140,143});
        block_and_radius.put(Material.DIRT, new int[] {134, 96,67});
        block_and_radius.put(Material.EMERALD_BLOCK, new int[] {81, 217,117});
        block_and_radius.put(Material.EMERALD_ORE, new int[] {109, 128,116});
        block_and_radius.put(Material.FURNACE, new int[] {113, 113,113});
        block_and_radius.put(Material.GLASS, new int[] {60, 66,67});
        block_and_radius.put(Material.GOLD_BLOCK, new int[] {249, 236,78});
        block_and_radius.put(Material.GOLD_ORE, new int[] {143, 139,124});
        block_and_radius.put(Material.GRAVEL, new int[] {126, 124,122});
        block_and_radius.put(Material.HARD_CLAY, new int[] {150, 92,66});
        block_and_radius.put(Material.ICE, new int[] {125, 173,255});
        block_and_radius.put(Material.IRON_BLOCK, new int[] {219, 219,219});
        block_and_radius.put(Material.IRON_ORE, new int[] {135, 130,126});
        block_and_radius.put(Material.JACK_O_LANTERN, new int[] {185, 133,28});
        block_and_radius.put(Material.LAPIS_BLOCK, new int[] {38, 67,137});
        block_and_radius.put(Material.LAPIS_ORE, new int[] {102, 112,134});
        block_and_radius.put(Material.LEAVES, new int[] {81, 81,81});
        block_and_radius.put(Material.LOG, new int[] {102, 81,49});
        block_and_radius.put(Material.MELON_BLOCK, new int[] {141, 145,36});
        block_and_radius.put(Material.MOSSY_COBBLESTONE, new int[] {103, 121,103});
        block_and_radius.put(Material.MYCEL, new int[] {113, 88,73});
        block_and_radius.put(Material.NETHER_BRICK, new int[] {44, 22,26});
        block_and_radius.put(Material.NETHERRACK, new int[] {111, 54,52});
        block_and_radius.put(Material.OBSIDIAN, new int[] {20, 18,29});
        block_and_radius.put(Material.PACKED_ICE, new int[] {165, 194,245});
        block_and_radius.put(Material.PUMPKIN, new int[] {142, 76,12});
        block_and_radius.put(Material.QUARTZ_ORE, new int[] {125, 84,79});
        block_and_radius.put(Material.REDSTONE_BLOCK, new int[] {171, 27,9});
        block_and_radius.put(Material.REDSTONE_ORE, new int[] {132, 107,107});
        block_and_radius.put(Material.SAND, new int[] {219, 211,160});
        block_and_radius.put(Material.SANDSTONE, new int[] {219, 211,160});
        block_and_radius.put(Material.SMOOTH_BRICK, new int[] {122, 122,122});
        block_and_radius.put(Material.SOUL_SAND, new int[] {84, 64,51});
        block_and_radius.put(Material.SPONGE, new int[] {194, 195,84});
        block_and_radius.put(Material.STONE, new int[] {125, 125,125});
        block_and_radius.put(Material.TNT, new int[] {169, 92,71});
        block_and_radius.put(Material.WOOD, new int[] {156, 127,78});
        block_and_radius.put(Material.WOOL, new int[] {221, 221,221});
	}
	
	private int Radius(Material m, int r, int g, int b) {
		// Actually radius squared, I don't square root.  No need for comparisons.
		int [] a = (int[]) block_and_radius.get(m);
		int rr = a[0] - r;
		int gg = a[1] - g;
		int bb = a[2] - b;
		return rr * rr + gg * gg + bb * bb;
	}
		
	public Material Getblock(int r, int g, int b) {
		Material block = null;
		int dist = 0;
		int r2;
		for (Object m: block_and_radius.keySet()) {
			if (block == null) {
				block = (Material) m;
				dist = Radius((Material) m, r, g, b);
				continue;
			}
			r2 = Radius((Material) m, r, g, b);
			if (r2 < dist) {
				block = (Material) m;
				dist = r2;
			}
		}
		return block;
	}
}
