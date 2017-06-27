package me.lumpchen.xafp.sf.triplet;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;

public class X4DTriplet extends Triplet {

	public static final int ID = 0x4D;
	
	public X4DTriplet() {
		super();
		this.identifier = ID;
	}
	
	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		int remain = this.length - 2;
		while (remain > 0) {
			in.readBytes(remain);
			remain = 0;
		}
		
		if (remain != 0) {
			throw new IOException("Triplet reading error." + remain);
		}
	}

}
