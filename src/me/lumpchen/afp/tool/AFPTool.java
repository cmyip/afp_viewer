package me.lumpchen.afp.tool;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import me.lumpchen.afp.AFPFileReader;
import me.lumpchen.afp.PrintFile;
import me.lumpchen.afp.render.AFPRenderer;

public class AFPTool {

	private static Logger logger = Logger.getLogger(AFPTool.class.getName());
	
	public AFPTool() {
	}
	
	public static void render(File afpFile, File outputFolder, String imageFomat) throws IOException {
		AFPFileReader reader = new AFPFileReader();
		try {
			reader.read(afpFile);
			PrintFile pf = reader.getPrintFile();
			
			AFPRenderer render = new AFPRenderer(null, pf);
			int pageNo = 0;
			int docCount = pf.getDocuments().size();
			for (int i = 0; i < docCount; i++) {
				int pageCount = pf.getDocuments().get(i).getPageList().size();
				
				for (int j = 0; j < pageCount; j++) {
					Image image = render.getPageImage(i, j);
					File f = createImageFile(outputFolder, afpFile, i + 1, j + 1, imageFomat);
					ImageIO.write((BufferedImage) image, imageFomat, f);
					
					logger.info("Rendering page " + ++pageNo);
				}
			}
		} finally {
			reader.close();
		}
	}
	
	private static File createImageFile(File outputFolder, File afpFile, int docIndex, int pageIndex, String suffix) throws IOException {
		StringBuilder buf = new StringBuilder();
		String afpName = afpFile.getName();
		buf.append(afpName.substring(0, afpName.length() - 4));
		buf.append("-" + docIndex + "-" + pageIndex);
		buf.append(".").append(suffix);
		
		File f = new File(outputFolder.getAbsolutePath() + "\\" + buf.toString());
		if (f.exists()) {
			return f;
		} else {
			if (f.createNewFile()) {
				return f;
			}
		}
		
		return null;
	}
}
