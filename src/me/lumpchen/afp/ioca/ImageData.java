package me.lumpchen.afp.ioca;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

public class ImageData {

	public static final int ID_0 = 0xFE;
	public static final int ID_1 = 0x92;
	
	private byte[] data;
	
	public ImageData() {
		
	}
	
	public byte[] getData() {
		return this.data;
	}
	
	public void read(AFPInputStream in) throws IOException {
		int length = in.readUBin(2);
		
		this.data = in.readBytes(length);
	}
}
