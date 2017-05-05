package me.lumpchen.afp.sf.triplet;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

/**
 * Object Classification Triplet
 * */
public class X10Triplet extends Triplet {

	public static final int ID = 0x10;
	
	private int ObjClass;
	private int StrucFlgs;
	
	private byte[] RegObjId;
	private byte[] ObjTpName;
	private byte[] ObjLev;
	private byte[] CompName;
	
	
	public X10Triplet() {
		super();
		this.identifier = ID;
	}
	
	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		int read = 2;
		
		in.read(); // reserverd
		read += 1;
		
		this.ObjClass = in.readCode();
		read += 1;
		
		in.readBytes(2); // reserved
		read += 2;
		this.StrucFlgs = in.readSBin(2);
		read += 2;
		this.RegObjId = in.readBytes(23 - 8 + 1);
		read += (23 - 8 + 1);
		
		if (this.length - read > 0 && in.remain() > 0) {
			this.ObjTpName = in.readBytes(55 - 24 + 1);
			read += (55 - 24 + 1);
		}
		if (this.length - read > 0 && in.remain() > 0) {
			this.ObjLev = in.readBytes(63 - 56 + 1);
			read += (63 - 56 + 1);
		}
		if (this.length - read > 0 && in.remain() > 0) {
			this.CompName = in.readBytes(95 - 64 + 1);
			read += (95 - 64 + 1);
		}
		
	}

	public int getObjClass() {
		return ObjClass;
	}

	public int getStrucFlgs() {
		return StrucFlgs;
	}

	public byte[] getRegObjId() {
		return RegObjId;
	}

	public byte[] getObjTpName() {
		return ObjTpName;
	}

	public byte[] getObjLev() {
		return ObjLev;
	}

	public byte[] getCompName() {
		return CompName;
	}

}
