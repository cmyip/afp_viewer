package me.lumpchen.afp;

import java.awt.Color;

import me.lumpchen.afp.AFPConst.ColorSpace;

public class AFPColor {

	private ColorSpace cs;
	private int[] value;
	
	public AFPColor(ColorSpace cs, int[] value) {
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
		} else if (this.cs == ColorSpace.CMYK) {
			float[] components = new float[4];
			components[0] = (value[0] & 0xFF) / 255f;
			components[1] = (value[1] & 0xFF) / 255f;
			components[2] = (value[2] & 0xFF) / 255f;
			components[3] = (value[3] & 0xFF) / 255f;
			
			float[] rgb = CMYKColorSpace.getInstance().toRGB(components);
			Color c = new Color(rgb[0], rgb[1], rgb[2]);
			return c;
		}
		
		return null;
	}
	
	static class CMYKColorSpace extends java.awt.color.ColorSpace {

		private static final long serialVersionUID = 1L;
		private static CMYKColorSpace instance;

		protected CMYKColorSpace(int type, int numcomponents) {
	        super(type, numcomponents);
	    }

	    public static CMYKColorSpace getInstance() {
	        if (instance == null) {
	            instance = new CMYKColorSpace(TYPE_CMYK, 4);
	        }
	        return instance;
	    }

	    public float[] toRGB(float[] colorvalue) {
	        return new float [] {
	            (1 - colorvalue[0]) * (1 - colorvalue[3]),
	            (1 - colorvalue[1]) * (1 - colorvalue[3]),
	            (1 - colorvalue[2]) * (1 - colorvalue[3])};
	    }

	    public float[] fromRGB(float[] rgbvalue) {
	        throw new UnsupportedOperationException("NYI");
	    }

	    public float[] toCIEXYZ(float[] colorvalue) {
	        throw new UnsupportedOperationException("NYI");
	    }

	    public float[] fromCIEXYZ(float[] colorvalue) {
	        throw new UnsupportedOperationException("NYI");
	    }

	}
}
