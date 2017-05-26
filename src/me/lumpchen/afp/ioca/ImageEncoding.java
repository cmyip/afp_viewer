package me.lumpchen.afp.ioca;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

public class ImageEncoding {

	public static final int ID = 0x95;
	
	private int COMPRID;
	private int RECID;
	private int BITORDR;
	
	public ImageEncoding() {
		
	}
	
	public void read(AFPInputStream in) throws IOException {
		int length = in.readUBin(1); // 2 or 3
		
		this.COMPRID = in.readCode();
		length--;
		this.RECID = in.readUBin(1);
		length--;
		
		if (length > 0) {
			this.BITORDR = in.readUBin(1);
		}
	}
}
