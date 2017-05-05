package me.lumpchen.afp.sf.triplet;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

public class X80Triplet extends Triplet {

	public static final int ID = 0x80;
	
	public X80Triplet() {
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

