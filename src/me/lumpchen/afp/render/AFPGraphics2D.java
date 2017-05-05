package me.lumpchen.afp.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import me.lumpchen.afp.font.AFPFont;

public class AFPGraphics2D implements AFPGraphics {
	
	private Graphics2D g2;
	
	private double tx = 0;
	private double ty = 0;
	private AFPFont afpFont;
	
	public AFPGraphics2D(Graphics2D g2) {
		this.g2 = g2;
	}
	
	@Override
	public void transform(AffineTransform Tx) {
		this.g2.transform(Tx);
	}
	
	public void setTransform(AffineTransform Tx) {
		this.g2.setTransform(Tx);
	}

	public void setFont(Font font) {
		this.g2.setFont(font);
	}
	
	@Override
	public AffineTransform getTransform() {
		return this.g2.getTransform();
	}

	@Override
	public void drawString(String str, float x, float y) {
		this.g2.setFont(new Font("TimesRoman", Font.PLAIN, (int) this.afpFont.fontSize));
		
		this.g2.drawString(str, x, y);
	}
	
	@Override
	public void scale(double sx, double sy) {
		this.g2.scale(sx, sy);
	}

	@Override
	public void setBackground(Color color) {
		this.g2.setBackground(color);
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
	public void setColor(Color c) {
		this.g2.setColor(c);
	}

	@Override
	public void setTranslateX(double tx) {
		this.g2.translate(-this.tx, -this.ty);
		this.tx = tx;
		this.g2.translate(this.tx, this.ty);
	}

	@Override
	public void setTranslateY(double ty) {
		this.g2.translate(-this.tx, -this.ty);
		this.ty = ty;
		this.g2.translate(this.tx, this.ty);
	}

	@Override
	public void setAFPFont(AFPFont afpFont) {
		this.afpFont = afpFont;
	}
}
