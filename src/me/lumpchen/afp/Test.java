package me.lumpchen.afp;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.lumpchen.afp.render.AFPRenderer;

public class Test {

	public static void main(String[] args) {
//		String file = "C:/temp/afp/20170516882301871.afp";
		String file = args[0];
		
		AFPFileReader reader = new AFPFileReader();
		try {
			reader.read(new File(file));
			PrintFile pf = reader.getPrintFile();
			
			AFPRenderer render = new AFPRenderer(null, pf);
			Image image = render.getPageImage(0, 0);
			
//			File temp = new File("c:/temp/afp/20170516882301871.jpg");
			File temp = new File(args[1]);
	        
	        temp.createNewFile();
	        ImageIO.write((BufferedImage) image, "jpg",temp);
	        
	        reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
