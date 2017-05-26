package me.lumpchen.afp.ioca;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

public class ImageContent {
	
	public static final int Begin = 0x91;
	public static final int End = 0x93;
	
	private int OBJTYPE;
	
	private ImageSize size;
	private ImageEncoding encoding;
	private int IDESize;
	
	public ImageContent() {
		
	}
	
	public void setImageSize(ImageSize size) {
		this.size = size;
	}
	
	public void setImageEncoding(ImageEncoding encoding) {
		this.encoding = encoding;
	}
	
	public void setIDESize(int size) {
		this.IDESize = size;
	}
	
	public void readBegin(AFPInputStream in) throws IOException {
		int length = in.readUBin(1);
		if (length > 0) {
			this.OBJTYPE = in.readCode(); // must be 0xFF
		}
		
	}
	
	private void readEnd(AFPInputStream in) throws IOException {
		int length = in.readUBin(1);
		if (length != 0) {
			// throw exception
		}
	}
}
