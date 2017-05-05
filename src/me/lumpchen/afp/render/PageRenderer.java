package me.lumpchen.afp.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import me.lumpchen.afp.Page;

public class PageRenderer {

	private Page page;
	
	public PageRenderer(Page page) {
		this.page = page;
	}
	
	public Image render() {
		double pw = this.page.getPageWidth() * 72;
		double ph = this.page.getPageHeight() * 72;
		double hRes = this.page.getHorResolution();
		double vRes = this.page.getVerResolution();
		
		double hScale = hRes / 72;
		double vScale = vRes / 72;
		
		// process rotation
		
        int widthPx = (int) Math.round(pw * hScale);
        int heightPx = (int) Math.round(ph * vScale);
        int rotationAngle = 0;
        
        BufferedImage image;
        if (rotationAngle == 90 || rotationAngle == 270) {
            image = new BufferedImage(heightPx, widthPx, BufferedImage.TYPE_INT_BGR);
        } else {
            image = new BufferedImage(widthPx, heightPx, BufferedImage.TYPE_INT_BGR);
        }
        Graphics2D g = image.createGraphics();
        
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        AFPGraphics2D graphics = new AFPGraphics2D(g);
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, image.getWidth(), image.getHeight());
        graphics.scale(hScale, vScale);
        if (rotationAngle != 0) {
            double translateX = 0;
            double translateY = 0;
            switch (rotationAngle) {
                case 90:
                    translateX = ph;
                    break;
                case 270:
                    translateY = pw;
                    break;
                case 180:
                    translateX = pw;
                    translateY = ph;
                    break;
            }
            graphics.translate(translateX, translateY);
            graphics.rotate((float) Math.toRadians(rotationAngle));
        }
        
        this.page.render(graphics);
        
        return image;
	}
	
	
	
}
