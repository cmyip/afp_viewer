package me.lumpchen.afp.sf.triplet;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

public class X18Triplet extends Triplet {

	public static final int ID = 0x18;
	
	public X18Triplet() {
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
	}

}
