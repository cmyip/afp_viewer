package me.lumpchen.afp.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;

import me.lumpchen.afp.font.AFPFont;

public interface AFPGraphics {
	
	public void transform(AffineTransform Tx);
	
	public AffineTransform getTransform();
	
	public void setTransform(AffineTransform Tx);
	
	public void drawString(String str, float x, float y);
	
	public void scale(double sx, double sy);
	
	public void setBackground(Color color);
	
	public void clearRect(int x, int y, int width, int height);
	
	public void translate(double tx, double ty);
	
	public void rotate(double theta);
	
	public void rotate(double theta, double x, double y);
	
	public void setColor(Color c);
	
	public void setFont(Font font); // for testing
	
	public void setAFPFont(AFPFont afpFont);
	
	public void setTranslateX(double tx);
	
	public void setTranslateY(double tx);
}
