package me.lumpchen.afp.sf.triplet;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

public class X4CTriplet extends Triplet {

	public static final int ID = 0x4C;
	
	/**
	 * X'02' Object Area Size 
	 * All others Reserved
	 * */
	private int SizeType;
	
	private int XoaSize;
	private int YoaSize;
	
	public X4CTriplet() {
		super();
		this.identifier = ID;
	}
	
	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		this.SizeType = in.readCode();
		this.XoaSize = in.readUBin(3);
		this.YoaSize = in.readUBin(3);
	}

	public int getSizeType() {
		return SizeType;
	}

	public int getXoaSize() {
		return XoaSize;
	}

	public int getYoaSize() {
		return YoaSize;
	}

}

