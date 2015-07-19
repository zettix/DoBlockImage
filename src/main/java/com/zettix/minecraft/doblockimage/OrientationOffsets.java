package com.zettix.minecraft.doblockimage;

import java.util.HashMap;



public class OrientationOffsets {
	
	private HashMap<String, int[]> offsets = null;

	public OrientationOffsets() {};
	
	public int[] GetOffsets(int px, int py, int pz, int xres, int yres, String key) {
		// I should use a generator function for this but I don't know how.
		// One change:  move blocks one block away for vertical, two blocks up for +y, and no change for -y (ground)
		 offsets = new HashMap<String, int[]>();
	     offsets.put("+x", new int[] {  0, px + 1,         px + 1,      0, 0,   1, py, py + yres, 0, 1,    1, pz - xres / 2, pz + xres , 1, 0}); 
	     offsets.put("-x", new int[] {  0, px - 2,         px - 1,      0, 0,   1, py, py + yres, 0, 1,   -1, pz + xres / 2, pz , 1, 0}); 
	     offsets.put("+z", new int[] { -1, px + xres / 2,  px,          1, 0,   1, py, py + yres, 0, 1,    0, pz + 1, pz + 1, 0, 0}); 
	     offsets.put("-z", new int[] {  1, px - xres / 2 , px + xres,   1, 0,   1, py, py + yres, 0, 1,    0, pz - 2, pz - 2, 0, 0}); 
	     offsets.put("+y+x", new int[] {-1, px , px + yres,   0, 1,   0, py + 2, py, 0, 0,           -1, pz + xres / 2, pz, 1, 0}); 
	     offsets.put("+y-x", new int[] {1, px , px + yres,   0, 1,   0, py + 2, py, 0, 0,            1, pz - xres / 2, pz + xres, 1, 0}); 
	     offsets.put("-y+x", new int[] {1, px, px,   0, 1,   0, py - 1, py, 0, 0,                1, pz - xres / 2, pz + xres, 1, 0}); 
	     offsets.put("-y-x", new int[] {-1, px, px,   0, 1,   0, py - 1, py, 0, 0,               -1, pz + xres / 2, pz, 1, 0}); 
	     offsets.put("+y+z", new int[] {-1, px , px + xres / 2,   1, 0,   0, py + 2, py + 2, 0, 0,          -1, pz, pz, 0, 1}); 
	     offsets.put("+y-z", new int[] {1, px - xres / 2 , px + xres,   1, 0,   0, py + 2, py + 2, 0, 0,           1, pz, pz + yres, 0, 1}); 
	     offsets.put("-y+z", new int[] {-1, px + xres/ 2 , px,  1, 0,   0, py - 1, py, 0, 0,           1, pz, pz, 0, 1}); 
	     offsets.put("-y-z", new int[] {1, px - xres / 2, px,   1, 0,   0, py - 1, py, 0, 0,          -1, pz, pz, 0, 1});	
	    return offsets.get(key);
	}	
}
