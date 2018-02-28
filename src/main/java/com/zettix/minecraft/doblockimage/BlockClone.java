package com.zettix.minecraft.doblockimage;

public class BlockClone {
    public int x_s, y_s, z_s;
    public org.bukkit.Material material;
    public BlockClone(int x, int y, int z, org.bukkit.Material m) {
        x_s = x;
        y_s = y;
        z_s = z;
        material = m;
    }

}