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
		this.XoaBase = in.readCode();
		this.YoaBase = in.readCode();
		this.XoaUnits = in.readUBin(2);
		this.YoaUnits = in.readUBin(2);
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

