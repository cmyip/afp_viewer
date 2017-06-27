package me.lumpchen.xafp.sf.triplet;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;

public class X01Triplet extends Triplet {

	public static final int ID = 0x01;
	// GCSGID - X'0001'�CX'FFFE' Specifies the Graphic Character Set Global Identifier
	//          X'FFFF' Specifies the character set consisting of all characters in the code page
	private int gcsgid;
	
	// CPGID - X'0001'�CX'FFFE' Specifies the Code Page Global Identifier
	private int cpgid;
	
	public X01Triplet() {
		super();
		this.identifier = ID;
	}
	
	public int getGcsgid() {
		return gcsgid;
	}

	public int getCpgid() {
		return cpgid;
	}
	
	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		int remain = this.length - 2;
		while (remain > 0) {
			this.gcsgid = in.readUBin(2);
			remain -= 2;
			this.cpgid = in.readUBin(2);
			remain -= 2;
		}
	}

}
