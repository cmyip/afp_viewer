package me.lumpchen.afp.render;

import java.awt.Color;

import me.lumpchen.afp.font.AFPFont;

public final class GraphicsState implements Cloneable {

	private Matrix currentTransformationMatrix = new Matrix();

	public Color backgroundColor;
	public Color color;
	
	public static class TextState {
		public float posX;
		public float posY;
		public AFPFont font;
		public float fontSize;
		public Color color;
		public float rotation;
		public float ruleWidth;
	}
	
	public TextState textState;

	public GraphicsState() {
		this.textState = new TextState();
	}

	public Matrix getCTM() {
		return this.currentTransformationMatrix;
	}

	public void setCTM(Matrix m) {
		this.currentTransformationMatrix = m;
	}

	public GraphicsState clone() {
		return this.clone();
	}
}
