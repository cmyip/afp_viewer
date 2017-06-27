package me.lumpchen.xafp.sf.triplet;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;

public class X43Triplet extends Triplet {

	public static final int ID = 0x43;
	
	// Specifies the associated Object Area Position structured field
	private int DesPosID;
	
	public X43Triplet() {
		super();
		this.identifier = ID;
	}
	
	public int getDesPosID() {
		return this.DesPosID;
	}
	
	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		int remain = this.length - 2;
		if (remain > 0) {
			this.DesPosID = (in.readCode() & 0x7F);
			remain -= 1;
		}
		
		if (remain != 0) {
			throw new IOException("Triplet reading error." + remain);
		}
	}

}
