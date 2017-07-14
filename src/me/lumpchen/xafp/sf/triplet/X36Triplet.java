package me.lumpchen.xafp.sf.triplet;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;

public class X36Triplet extends Triplet {

	public static final int ID = 0x36;
	
	public X36Triplet() {
		super();
		this.identifier = ID;
		this.name = "Attribute Value";
	}
	
	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		while (remain > 0) {
			in.readBytes(remain);
			remain = 0;
		}
	}

}
