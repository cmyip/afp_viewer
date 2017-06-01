package me.lumpchen.afp.ioca;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

public class ExternalAlgorithmSpecification {
	
	public static final int ID = 0x9F;
	
	private int ALGTYPE;
	private byte[] ALGSPEC;
	
	public ExternalAlgorithmSpecification() {
		
	}
	
	public void read(AFPInputStream in) throws IOException {
		int length = in.readUBin(1);
		
		this.ALGTYPE = in.readCode();
		length--;
		
		in.readByte();
		length--;
		
		this.ALGSPEC = in.readBytes(length);
	}
}