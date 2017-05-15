package me.lumpchen.afp.render;

import java.awt.Color;

import me.lumpchen.afp.font.AFPFont;

public final class GraphicsState implements Cloneable {

	private Matrix currentTransformationMatrix = new Matrix();

	public Color backgroundColor;
	public Color color;
	
	public AFPFont font;
	public float fontSize;

	public GraphicsState() {

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
