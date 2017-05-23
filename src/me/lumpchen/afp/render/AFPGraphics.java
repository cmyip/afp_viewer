package me.lumpchen.afp.render;

import java.awt.Color;
import java.awt.image.BufferedImage;

import me.lumpchen.afp.AFPColor;
import me.lumpchen.afp.font.AFPFont;

public interface AFPGraphics {
	
	public void drawString(String str, float x, float y);
	
	public void drawString(byte[] text, float x, float y);
	
	public void scale(double sx, double sy);
	
	public void setBackground(Color color);
	
	public void clearRect(int x, int y, int width, int height);
	
	public void translate(double tx, double ty);
	
	public void rotate(double theta);
	
	public void rotate(double theta, double x, double y);
	
	public void setColor(AFPColor c);
	
	public void setAFPFont(AFPFont afpFont, float fontSize);
	
	public void setTranslateX(double tx);
	
	public void setTranslateY(double tx);
	
	public void beginText();
	
	public void endText();
	
    public void drawImage(BufferedImage img, float x, float y, float w, float h);

    public void setLineWidth(float w);
    
    public void drawLine(float x1, float y1, float x2, float y2);
}
