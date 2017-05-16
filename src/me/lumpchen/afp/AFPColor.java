package me.lumpchen.afp;

import java.awt.Color;

import me.lumpchen.afp.AFPConst.ColorSpace;

public class AFPColor {

	private ColorSpace cs;
	private byte[] value;
	
	public AFPColor(ColorSpace cs, byte[] value) {
		if (cs.getComponentLength() != value.length) {
			throw new java.lang.IllegalArgumentException("Color component length not match value length.");
		}
		this.cs = cs;
		this.value = value;
	}
	
	public Color toJavaColor() {
		if (this.cs == ColorSpace.RGB) {
			int r = value[0] & 0xFF;
			int g = value[1] & 0xFF;
			int b = value[2] & 0xFF;
			Color c = new Color(r, g, b);
			return c;
		}
		
		return null;
	}
}
