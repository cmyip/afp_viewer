package me.lumpchen.xafp.ioca;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;

public class IDEStructure {

	public static final int ID = 0x9B;
	
	private int FLAGS;
	private int FORMAT;
	private int SIZE1, SIZE2, SIZE3, SIZE4;
	
	public IDEStructure() {
		
	}
	
	public void read(AFPInputStream in) throws IOException {
		int length = in.readUBin(1); // 2 or 3
		this.FLAGS = in.readByte();
		length -= 1;
		this.FORMAT = in.readCode();
		length -= 1;
		in.readBytes(3);
		length -= 3;
		
		this.SIZE1 = in.readUBin(1);
		length -= 1;
		if (length > 0) {
			this.SIZE2 = in.readUBin(1);
			length -= 1;
		}
		
		if (length > 0) {
			this.SIZE3 = in.readUBin(1);
			length -= 1;
		}
		
		if (length > 0) {
			this.SIZE4 = in.readUBin(1);
			length -= 1;
		}
	}
}
