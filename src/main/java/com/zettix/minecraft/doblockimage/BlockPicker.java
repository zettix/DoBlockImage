package com.zettix.minecraft.doblockimage;

import java.util.HashMap;
import org.bukkit.Material;

public class BlockPicker {
	
	public HashMap<Material, int[]> block_and_radius;
		
	public BlockPicker ( ) {
		// These are hard coded from a utility I wrote in python.
		// It basically averages the rgb values from one of the block textures.
		block_and_radius = new HashMap<Material, int[]>();
        block_and_radius.put(Material.DIRT, new int[] {134, 96,67});
        block_and_radius.put(Material.WOOL, new int[] {221, 221,221});
        block_and_radius.put(Material.WOOD, new int[] {156, 127,78});
        block_and_radius.put(Material.PUMPKIN, new int[] {197, 120,23});
        block_and_radius.put(Material.GRAVEL, new int[] {126, 124,122});
        block_and_radius.put(Material.MOSSY_COBBLESTONE, new int[] {103, 121,103});
        block_and_radius.put(Material.GOLD_BLOCK, new int[] {249, 236,78});
        block_and_radius.put(Material.SANDSTONE, new int[] {219, 211,160});
        block_and_radius.put(Material.LOG, new int[] {102, 81,49});
        block_and_radius.put(Material.TNT, new int[] {169, 92,71});
        block_and_radius.put(Material.ICE, new int[] {125, 173,255});
        block_and_radius.put(Material.COBBLESTONE, new int[] {122, 122,122});
        block_and_radius.put(Material.MELON_BLOCK, new int[] {141, 145,36});

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