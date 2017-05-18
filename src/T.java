import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;

import me.lumpchen.afp.render.Matrix;

public class T {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		TTFParser ttfParser = new TTFParser(true);
		TrueTypeFont ttfFont = ttfParser
				.parse(new FileInputStream("C:\\dev\\xPression\\Publish\\FontCache\\arial.ttf"));

		BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_BGR);
		Graphics2D g = image.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//        g.translate(0, 500);
//        g.scale(1, -1);
        
		float fontSize = 12;
		byte[] text = "abc".getBytes();
		for (byte b : text) {
			try {
				Matrix parameters = new Matrix(fontSize, 0, 0, fontSize, 0, 0);

				Matrix ctm = new Matrix(1, 0, 0, 1, 0, 0);
				Matrix textMatrix = new Matrix(1, 0, 0, 1, 72, 72);

				Matrix textRenderingMatrix = parameters.multiply(textMatrix).multiply(ctm);

				AffineTransform at = textRenderingMatrix.createAffineTransform();

				Matrix fm = new Matrix(0.001f, 0, 0, 0.001f, 0, 0);
				at.concatenate(fm.createAffineTransform());

				GeneralPath glyph = ttfFont.getPath("P");
				Matrix gm = new Matrix(1, 0, 0, -1, 0, 0);
				Shape gp = gm.createAffineTransform().createTransformedShape(glyph);
				
				Shape s = at.createTransformedShape(gp);

				g.setColor(Color.red);
				g.drawLine(0, 72, 300, 72);
				
				g.setFont(new Font("Arial", 0,  24));
				g.drawString("P", 88, 72);
				g.fill(s);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File temp = new File("c:/temp/afp/page.jpg");
		temp.createNewFile();
		ImageIO.write((BufferedImage) image, "png", temp);
	}
}
