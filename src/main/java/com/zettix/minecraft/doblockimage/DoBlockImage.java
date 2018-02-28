package com.zettix.minecraft.doblockimage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.DataBuffer;
import java.awt.Color;
import java.awt.Rectangle;

import org.bukkit.command.Command;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DoBlockImage extends JavaPlugin {
	private List<BlockClone> undoList;

	@Override
	public void onEnable() {
		// TODO: Probably nothing.
		undoList = null;
		getLogger().info("onEnable has been invoked");
		
	}
	
	@Override
	public void onDisable() {
		// TODO: Also probably nothing.
		getLogger().info("onDisable has been invoked");
	}

	// Module maximum dimension for any block image.
	int mod_max_dim = 100;
	static final String SpigotUrl = new String("http://www.spigotmc.org/resources/doblockimage.5454/");
	
	void doImage(String user_webaddr, Player p, int px, int py, int pz, World world,
			int usermax, String key, boolean erase) {
		boolean debug = false;
		undoList = new ArrayList<>();
		
		BlockPicker bp = new BlockPicker();
		OrientationOffsets offsob = new OrientationOffsets();
		try {
			URL url = new URL(user_webaddr);
			BufferedImage image = null;
			image = ImageIO.read(url.openStream());
			Raster raster = image.getData();
			Rectangle bounds = raster.getBounds();
			p.sendMessage("Image resolution: " + bounds.width + " " + bounds.height);
			getLogger().info("Image resolution: " + bounds.width + " " + bounds.height);
			getLogger().info("Image encoding: " + image.getColorModel().toString());

			if (DataBuffer.TYPE_BYTE != raster.getTransferType()) {
				p.sendMessage("Image is not based on bytes is not supported.");
				return;
			}
			boolean rgba = false;
			int image_depth = raster.getNumDataElements();
			if (image_depth == 4) {
				getLogger().info("Image is RGBA, alpha < 10 masked!");
				rgba = true;
		  	  } else {
				getLogger().info("Image is RGB");
			}
		    double scale_factor = 1.0;
		    if (usermax > 0) {
		    	if (usermax >= mod_max_dim) {
		    		usermax = mod_max_dim;
		    	}
		    } else {  // usermax not set.
		    	if (bounds.height > mod_max_dim) {
		    	    usermax = mod_max_dim;
		    	} else {
		    		usermax = bounds.height;
		    	}
		    }
		    if (bounds.height > usermax) {
		    	scale_factor = (double) usermax / (double) bounds.height;
		    }
		    if ((int)((double)bounds.width * scale_factor) > mod_max_dim) {
		    	//  k * scale_factor * bounds.width = mod_max_dim
		    	// new_scale * bounds.width = mod_max_dim
		    	scale_factor = (double) mod_max_dim / (double) bounds.width;
		    }
		    getLogger().info("Scale Factor: " + scale_factor);
		    // Player facing direction, NSEW, for image placement
			//int buf[] = new int[4];
			int r, g, b, a;
			int sx, sy;
			int scaled_h = (int)( (double) bounds.height * scale_factor);
			int scaled_w = (int)( (double) bounds.width * scale_factor);
			
			getLogger().info("Scale size:" + scaled_w + " x " + scaled_h);
			int [] offies = offsob.GetOffsets(px, py, pz, scaled_w, scaled_h, key);
			
		    // w = world
		    // yd, xd are image times scale factor.

		    
		    // xd and yd sample pixels from the source image (using scale_factor)
		    // scaled_h and scaled_w will be the dimension of the block representation as well.
		    // However, the player can face in one of 12 positions, so this is solved by
		    // iterating over the x/y of the image and moving in world x, y, z according to the
		    // offset table function, which specifies starting x, y, and x, and what to increment.\
		    // They are:
		    // x_d, x_s, x_e, xx, xy, y_d, y_s, y_e, yx, yy,  z_d, z_s, z_e, zx, zy
			int x_d, x_s, xx, xy, y_d, y_s, yx, yy,  z_d, z_s, zx, zy;
	        x_d = offies[0];
	        x_s = offies[1];
	        xx = offies[3];
	        xy = offies[4];
	        y_d = offies[5];
	        y_s = offies[6];
	        yx = offies[8];
	        yy = offies[9];
	        z_d = offies[10];
	        z_s = offies[11];
	        zx = offies[13];
	        zy = offies[14];
	        if (debug) {
	          getLogger().info("xd:" + x_d + " xs:" + x_s + " xx:" + xx + " xy:" + xy);
	          getLogger().info("yd:" + y_d + " ys:" + y_s + " yx:" + yx + " yy:" + yy);
	          getLogger().info("zd:" + z_d + " zs:" + z_s + " zx:" + zx + " zy:" + zy);
	        }
		    // _d: change, _s: start, _e: send. *x: change on image x, *y change on image y
			for (int yd = scaled_h - 1; yd >= 0; yd--) {
				for (int xd = 0; xd < scaled_w; xd++) {
					//raster.getDataElements(xd, yd, buf);
					//raster.getPixel(xd, yd, buf);
					sx = (int) ((double) xd / scale_factor);
					sy = (int) ((double) yd / scale_factor);
					// getLogger().info("Getting pixel at:" + sx + " by " + sy);
					Color c = new Color(image.getRGB(sx,  sy), rgba); // image space
					r = c.getRed();
					g = c.getGreen();
					b = c.getBlue();
					a = -1;

					// world space
				    org.bukkit.block.Block myblock = world.getBlockAt(x_s, y_s, z_s);
				    undoList.add(new BlockClone(x_s, y_s, z_s, myblock.getType()));
					// myblock.setType(Material.DIAMOND_BLOCK);
				    Material m = Material.AIR;
				    if (! erase) {
					    m = bp.Getblock(r,g,b);
				    }
					// getLogger().info("Block Type: " + m.toString());
					if (yd == 10 && debug) {
						getLogger().info("RGB: R:" + r + " G:" + g + " B: " + b + " A: " + a);
						getLogger().info("Block Type: " + m.toString());
						getLogger().info("Block Coords:" + x_s + " " + y_s + " " + z_s);
				    }
			        x_s += x_d * xx;
			        y_s += y_d * yx;
			        z_s += z_d * zx;
					a = -1;
					if (rgba) {    
					  a = c.getAlpha();
					  // getLogger().info("A: " + a);
					  if (a < 10) {
						continue;
			          }
				    }
				    myblock.setType(m);	
				}
				if (xx != 0) {
					x_s = offies[1];
				}
				if (yx != 0) {
			        y_s = offies[6];
				}
				if (zx != 0) {
			        z_s = offies[11];
				}
			    x_s += x_d * xy;
	            y_s += y_d * yy;
			    z_s += z_d * zy;
			}
		} catch (IOException e) {
			getLogger().info("Image error: " + e.toString());
			p.sendMessage("Image Error:" + e.toString());
		}
		
	}

	public void doUndo(Player p, World world) {
		if (undoList == null) {
			p.sendMessage("DoBlockImage: Undo buffer empty.");
			return;
		}
		for (BlockClone bc : undoList) {
			org.bukkit.block.Block myblock = world.getBlockAt(bc.x_s, bc.y_s, bc.z_s);
			myblock.setType(bc.material);
		}
		undoList = null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("doblockimage")) {  // player /doblockimage
			int urlindex = 0;
			int maxheight = -1;  // no user max, mod max is 100.
			boolean erase = false;
			boolean undo = false;
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
				return false;
			} else {
			  if (args.length == 3) {
				  // getLogger().info("argo:" + args[0] + " arg1:" + args[1] + " arg2:" + args[2] + ":");
				  if ("e".equals(args[0])) {
					  erase = true;
					  maxheight = Integer.parseInt(args[1]);
					  urlindex = 2;
				  } else {
					  sender.sendMessage("Usage error. doblockimage [e] [maxHeight] imageUrl");
				  }
			  } else if (args.length == 2) {
				  if ("e".equals(args[0])) {
				  	erase = true;
				  } else {
				  	maxheight = Integer.parseInt(args[0]);
				  }
		          urlindex = 1;
			  } else if (args.length != 1) {
				  sender.sendMessage("Bad input. Usage: doblockimage [e] [maxHeight] imageUrl");
				  return false;
			  } else {  // args.length == 1
				  if ("u".equals(args[0])) {
					  undo = true;
				  }
			  }
			  // OK We're going to do it.
			  Player player = (Player) sender;       
			  Location loc = player.getLocation();
			  int x1 = loc.getBlockX();
			  int y1 = loc.getBlockY();
			  int z1 = loc.getBlockZ();
			  World world = loc.getWorld();
			  float yaw = player.getLocation().getYaw();
			  float pitch = player.getLocation().getPitch();
			  // getLogger().info("Yaw: " + yaw + " Pitch:" + pitch);
			  // up -90, down 90, z+ 0, -x = 90= -z = 180, -180 = -z , -90 = x , 0 = +z
			  String key = new String();
			  if (yaw < -180.0) {
				  yaw += 360.0;
			  }
			  if (yaw > 180.0) {
				  yaw -= 360.0;
			  }
			  if (yaw < 45.0 && yaw > -45.0) {
				key = "+z";
			  } else if (yaw < 135.0 && yaw  > 44.0) {
				  key = "-x";
			  } else if (yaw > -135 && yaw < -44) {
				  key = "+x";
			  } else {
				  key = "-z";
			  }
			  if (pitch > 45.0) {
				  key = "-y" + key;
			  }
			  if (pitch < -45.0) {
				  key = "+y" + key;
			  }
			  // getLogger().info("Running doImage( " + args[urlindex] + ",player, " + x1 + "," + y1 + "," + z1 + ", " + " maxh: " + maxheight + " key:" + key
			  //		  + " Erase: " + erase);
				if (undo) {
			  		doUndo(player, world);
				} else {
					doImage(args[urlindex], player, x1, y1, z1, world, maxheight, key, erase);
				}
			  return true;
		     }
		}
		return false;
	}	
}