package me.lumpchen.afp.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import me.lumpchen.afp.AFPColor;
import me.lumpchen.afp.font.AFPFont;

public class AFPGraphics2D implements AFPGraphics {
	
	private Graphics2D g2;
	
	private GraphicsState state;
	
	private double tx = 0;
	private double ty = 0;
	
	private Matrix textMatrix;
	private Matrix textLineMatrix;
	
	public AFPGraphics2D(Graphics2D g2) {
		this.g2 = g2;
		
		this.state = new GraphicsState();
		this.textMatrix = new Matrix();
		this.textLineMatrix = new Matrix();
	}
	
	@Override
	public void drawString(String str, float x, float y) {
		this.g2.drawString(str, x, y);
		char[] chars = str.toCharArray();
	}
	
	@Override
	public void drawString(byte[] text, float x, float y) {
		this.g2.setColor(this.state.color == null ? Color.black : this.state.color);
		
		this.textMatrix.concatenate(Matrix.getTranslateInstance(x, y));
		
		float fontSize = this.state.fontSize;
        Matrix parameters = new Matrix(fontSize, 0, 0, fontSize, 0, 0);
        
		for (byte b : text) {
			int unicode = this.state.font.getEncoding().getUnicode(b & 0xFF);
			String gcgid = this.state.font.getEncoding().getCharacterName(b & 0xFF);
			try {
				Matrix ctm = this.state.getCTM();
				Matrix textRenderingMatrix = parameters.multiply(this.textMatrix).multiply(ctm);
				
				AffineTransform at = textRenderingMatrix.createAffineTransform();

				Matrix fm = new Matrix(0.001d, 0, 0, 0.001d, 0, 0);
				at.concatenate(fm.createAffineTransform());
				
				GeneralPath glyph = this.state.font.getPath(gcgid);
				Matrix gm = new Matrix(1, 0, 0, -1, 0, 0);
				Shape gp = gm.createAffineTransform().createTransformedShape(glyph);
				
				Shape s = at.createTransformedShape(gp);
				
				this.g2.setColor(this.state.color);
				this.g2.fill(s);
				
				double advance = this.state.font.getWidth(gcgid) / 1000d;
				this.textMatrix.concatenate(Matrix.getTranslateInstance(advance * fontSize, ty));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println(b + "  " + unicode + "  " + gcgid);
		}
		
		this.textMatrix = new Matrix();
	}
	
	@Override
	public void scale(double sx, double sy) {
		this.state.getCTM().scale(sx, sy);
	}

	@Override
	public void setBackground(Color color) {
		this.state.backgroundColor = color;
		this.g2.setBackground(this.state.backgroundColor);
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		this.g2.clearRect(x, y, width, height);
	}

	@Override
	public void translate(double tx, double ty) {
		this.g2.translate(tx, ty);
	}

	@Override
	public void rotate(double theta) {
		this.g2.rotate(theta);
	}

	@Override
	public void rotate(double theta, double x, double y) {
		this.g2.rotate(theta, x, y);
	}

	@Override
	public void setTranslateX(double tx) {
//		this.g2.translate(-this.tx, -this.ty);
//		this.tx = tx;
//		this.g2.translate(this.tx, this.ty);
		
		this.textMatrix.concatenate(Matrix.getTranslateInstance(tx, 0));
	}

	@Override
	public void setTranslateY(double ty) {
//		this.g2.translate(-this.tx, -this.ty);
//		this.ty = ty;
//		this.g2.translate(this.tx, this.ty);
		
		this.textMatrix.concatenate(Matrix.getTranslateInstance(0, ty));
	}

	@Override
	public void setAFPFont(AFPFont afpFont, float fontSize) {
		this.state.font = afpFont;
		this.state.fontSize = fontSize;
	}

	@Override
	public void beginText() {
	}

	@Override
	public void endText() {
	}

	@Override
	public void setColor(AFPColor c) {
		Color awtColor = c.toJavaColor();
		this.state.color = awtColor;
	}

	@Override
	public void drawImage(BufferedImage img, int x, int y) {
		this.g2.drawImage(img, x, y, 144, 72, null);
	}
}
