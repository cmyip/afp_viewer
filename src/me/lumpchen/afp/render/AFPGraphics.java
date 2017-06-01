package me.lumpchen.afp.render;

import java.awt.Color;
import java.awt.image.BufferedImage;

import me.lumpchen.afp.AFPColor;
import me.lumpchen.afp.font.AFPFont;
import me.lumpchen.afp.render.GraphicsState.TextState;

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
	
    public void drawImage(BufferedImage img, float x, float y, float w, float h);

    public void setLineWidth(float w);
    
    public void drawRule(float x1, float y1, float x2, float y2, boolean horizon);
    
	public void beginText();
	public void endText();
	public void setAFPFont(AFPFont afpFont, float fontSize);
	public void setTextPosX(float tx);
	public void setTextPosY(float tx);
	public void setTextRotation(float degree);
	public void setTextColor(AFPColor c);
	
    public void save();
    public void restore();
}
