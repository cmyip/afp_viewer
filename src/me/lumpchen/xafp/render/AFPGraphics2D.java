package me.lumpchen.xafp.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.lumpchen.xafp.AFPColor;
import me.lumpchen.xafp.font.AFPFont;
import me.lumpchen.xafp.render.GraphicsState.TextState;

public class AFPGraphics2D implements AFPGraphics {
	
	private Graphics2D g2;
	private float width;
	private float height;
	
	private GraphicsState state;
	
	private Matrix textMatrix;
	private Matrix textLineMatrix;
	
	private Graphics2D savedGraphics;
	
	public AFPGraphics2D(Graphics2D g2, float width, float height) {
		this.g2 = g2;
		this.width = width;
		this.height = height;
		
		this.state = new GraphicsState();
		this.textMatrix = new Matrix();
		this.textLineMatrix = new Matrix();
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
	public void setColor(AFPColor c) {
	}
	
	@Override
	public void drawImage(BufferedImage img, float x, float y, float w, float h) {
		this.g2.drawImage(img, Math.round(x), Math.round(y), Math.round(w), Math.round(h), null);
	}

	@Override
	public void setLineWidth(float w) {
		this.state.textState.ruleWidth = w;
	}

	@Override
	public void beginText() {
		this.save();
		this.textMatrix = new Matrix();
		this.textLineMatrix = new Matrix();
	}

	@Override
	public void endText() {
		this.restore();
	}
	
	@Override
	public void setAFPFont(AFPFont afpFont, float fontSize) {
		this.state.textState.font = afpFont;
		this.state.textState.fontSize = fontSize;
	}
	
	@Override
	public void setTextRotation(float degree) {
		this.state.textState.rotation = degree;
	}

	@Override
	public void setTextColor(AFPColor c) {
		Color awtColor = c.toJavaColor();
		this.state.textState.color = awtColor;
	}

	@Override
	public void setTextPosX(float tx) {
		this.state.textState.posX = tx;
	}

	@Override
	public void setTextPosY(float ty) {
		this.state.textState.posY = ty;
	}
	
	private void setTextState(TextState tstate) {
		this.textLineMatrix = new Matrix();
		this.textLineMatrix.concatenate(Matrix.getTranslateInstance(tstate.posX, 0));
		
		this.textMatrix = new Matrix();
		this.textMatrix.concatenate(Matrix.getTranslateInstance(0, tstate.posY));
		
		this.g2.setColor(tstate.color == null ? Color.black : tstate.color);
		
		if (tstate.rotation == 0) {
			this.g2.translate(0, 0);
		}  else if (tstate.rotation == 180) {
			this.g2.translate(this.width, this.height);	
		} else if (tstate.rotation == 270) {
			this.g2.translate(0, this.height);	
		}
		this.g2.rotate(Math.toRadians(tstate.rotation));
	}
	
	@Override
	public void drawString(String str, float x, float y) {
	}
	
	@Override
	public void drawString(byte[] text, float x, float y) {
		this.save();
		this.setTextState(this.state.textState);
		
		float fontSize = this.state.textState.fontSize;
        Matrix parameters = new Matrix(fontSize, 0, 0, fontSize, 0, 0);
        
		for (byte b : text) {
			int unicode = this.state.textState.font.getEncoding().getUnicode(b & 0xFF);
			String gcgid = this.state.textState.font.getEncoding().getCharacterName(b & 0xFF);
			try {
				Matrix ctm = this.state.getCTM();
				Matrix textRenderingMatrix = parameters.multiply(this.textLineMatrix).multiply(this.textMatrix).multiply(ctm);
				
				AffineTransform at = textRenderingMatrix.createAffineTransform();

				Matrix fm = new Matrix(0.001d, 0, 0, 0.001d, 0, 0);
				at.concatenate(fm.createAffineTransform());
				
				GeneralPath glyph = this.state.textState.font.getPath(gcgid);
				if (glyph == null) {
					continue;
				}
				
				Matrix gm = new Matrix(1, 0, 0, -1, 0, 0);
				Shape gp = gm.createAffineTransform().createTransformedShape(glyph);
				
				Shape s = at.createTransformedShape(gp);
				this.g2.fill(s);
				
				double advance = this.state.textState.font.getWidth(gcgid) / 1000d;
				this.textLineMatrix.concatenate(Matrix.getTranslateInstance(advance * fontSize, 0));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.restore();
	}
	
	@Override
	public void drawRule(float x1, float y1, float x2, float y2, boolean horizon) {
		this.save();
		this.antialiasOff();
		this.setTextState(this.state.textState);
		
		Matrix ctm = this.state.getCTM();
		Matrix m;
		if (horizon) {
			m = new Matrix(1, 0, 0, 1, 0, this.state.textState.ruleWidth / 2);
		} else {
			m = new Matrix(1, 0, 0, 1, this.state.textState.ruleWidth / 2, 0);
		}
		
		Matrix textRenderingMatrix = m.multiply(this.textLineMatrix).multiply(this.textMatrix).multiply(ctm);
		AffineTransform at = textRenderingMatrix.createAffineTransform();
		
		Line2D.Float line = new Line2D.Float(x1, y1, x2, y2);
		Shape s = at.createTransformedShape(line);
		
		this.g2.setStroke(new BasicStroke(this.state.textState.ruleWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		this.g2.draw(s);
		
		this.antialiasOn();
		this.restore();
	}
	
	@Override
	public void save() {
		this.savedGraphics = this.g2;
		this.g2 = (Graphics2D) this.g2.create();
	}

	@Override
	public void restore() {
		this.g2.dispose();
		this.g2 = this.savedGraphics;
		this.savedGraphics = null;
	}
	
	@Override
    public void antialiasOn() {
    	this.g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    	this.g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
	@Override
    public void antialiasOff() {
    	this.g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    	this.g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
}
