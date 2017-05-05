package me.lumpchen.afp;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.lumpchen.afp.render.PageRenderer;

public class Test {

	public static void main(String[] args) {
		String file = "C:/temp/afp/XPR-91009.afp";
		
		AFPFileReader reader = new AFPFileReader();
		try {
			reader.read(new File(file));
			PrintFile pf = reader.getPrintFile();
			
			Page page = pf.getDocuments().get(0).getPageList().get(0);
			PageRenderer render = new PageRenderer(page);
			Image image = render.render();
			
	        File temp = new File("c:/temp/afp/page.jpg");
	        temp.createNewFile();
	        ImageIO.write((BufferedImage) image, "png",temp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.err.println(Integer.toHexString(-32768));
	}
}
