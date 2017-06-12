package me.lumpchen.afp.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import junit.framework.TestCase;
import me.lumpchen.afp.tool.AFPTool;

public class TestCase0 extends TestCase {

	private static Logger logger = Logger.getLogger(TestCase0.class.getName());
	
	File root = new File("C:/temp/afp/xpression/all");
	
	public void test_X80_2C() {
		assertTrue(compare("X80_2C.afp"));
	}
	
	public void test__provini_1() {
		assertTrue(compare("_provini (1).afp"));
	}
	
	public void test_provini() {
		assertTrue(compare("_provini.afp"));
	}
	
	private boolean compare(String afpName) {
		String s = this.root.getAbsolutePath() + "\\" + afpName.substring(0, afpName.length() - 4);
		File outputFolder = new File(s);
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}
		
		File afpFile = new File(this.root, afpName);
		try {
			logger.info("Start rendering " + afpFile.getAbsolutePath());
			AFPTool.render(afpFile, outputFolder, "jpg");
			logger.info("Complete rendering: " + afpFile.getAbsolutePath());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Render fail: " + afpFile.getAbsolutePath(), e);
			return false;
		}
		
		try {
			logger.info("Start comparing bitmaps: " + outputFolder.getAbsolutePath());
			assertTrue(compare(outputFolder, "jpg"));
			logger.info("Complete comparing bitmaps: " + outputFolder.getAbsolutePath());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Bitmap compare fail: " + afpFile.getAbsolutePath(), e);
		}
		
		return true;
	}
	
	private boolean compare(File outputFolder, final String imageFomat) {
		File baseFolder = new File(outputFolder, "baseline");
		if (!baseFolder.exists()) {
			logger.severe("Not find baseline folder: " + baseFolder.getAbsolutePath());
			return false;
		}
		
		File[] images = outputFolder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith("." + imageFomat)) {
					return true;
				}
				return false;
			}
			
		});
		
		for (File img : images) {
			String name = img.getName();
			File baseline = new File(baseFolder, name);
			if (!baseline.exists()) {
				logger.severe("Not find baseline image: " + baseline.getAbsolutePath());
				return false;
			}
			
			BufferedImage bimg1;
			BufferedImage bimg2;
			BufferedImage diff;
			try {
				bimg1 = ImageIO.read(new FileInputStream(baseline));
				bimg2 = ImageIO.read(new FileInputStream(img));
				diff = BitmapComparor.diffImages(bimg1, bimg2, "black");
				if (diff != null) {
					logger.log(Level.WARNING, "Different as baseline: " + baseline.getAbsolutePath());
					
					ImageIO.write(diff, "jpg", new File(img.getParentFile(), img.getName() + ".diff.jpg"));
					return false;
				} else {
					logger.log(Level.INFO, "Same as baseline: " + baseline.getAbsolutePath());
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, "bitmap generation fail: " + img.getAbsolutePath(), e);
				return false;
			}
		}
		return true;
	}
}