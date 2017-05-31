package me.lumpchen.afp;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.lumpchen.afp.render.AFPRenderer;

public class Test {

	public static void main(String[] args) {
		String file = args[0];
		int docIndex = Integer.parseInt(args[2]);
		int pageIndex = Integer.parseInt(args[3]);
		
		AFPFileReader reader = new AFPFileReader();
		try {
			reader.read(new File(file));
			PrintFile pf = reader.getPrintFile();
			
			AFPRenderer render = new AFPRenderer(null, pf);
			Image image = render.getPageImage(docIndex, pageIndex);
			File temp = new File(args[1]);
	        
	        temp.createNewFile();
	        ImageIO.write((BufferedImage) image, "jpg",temp);
	        
	        reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
