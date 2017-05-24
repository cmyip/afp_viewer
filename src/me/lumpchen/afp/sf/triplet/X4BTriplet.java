package me.lumpchen.afp.sf.triplet;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

public class X4BTriplet extends Triplet {

	public static final int ID = 0x4B;
	
	private int XoaBase;
	private int YoaBase;
	private int XoaUnits;
	private int YoaUnits;
	
	public X4BTriplet() {
		super();
		this.identifier = ID;
	}
	
	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		int remain = this.length - 2;
		this.XoaBase = in.readCode();
		remain -= 1;
		this.YoaBase = in.readCode();
		remain -= 1;
		this.XoaUnits = in.readUBin(2);
		remain -= 2;
		this.YoaUnits = in.readUBin(2);
		remain -= 2;
		
		if (remain != 0) {
			throw new IOException("Triplet reading error." + remain);
		}
	}

	public static int getId() {
		return ID;
	}

	public int getXoaBase() {
		return XoaBase;
	}

	public int getYoaBase() {
		return YoaBase;
	}

	public int getXoaUnits() {
		return XoaUnits;
	}

	public int getYoaUnits() {
		return YoaUnits;
	}

}

