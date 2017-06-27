package me.lumpchen.xafp.sf.triplet;

import java.io.IOException;

import me.lumpchen.xafp.AFPConst;
import me.lumpchen.xafp.AFPInputStream;

public class X21Triplet extends Triplet {

	public static final int ID = 0x21;
	
	public static final byte GOCA_OBJECT          = (byte)0x03;
	public static final byte BCOCA_OBJECT         = (byte)0x05;
	public static final byte IOCA_OBJECT          = (byte)0x06;
	public static final byte CHARACTER_SET_OBJECT = (byte)0x40;
	public static final byte CODE_PAGE_OBJECT     = (byte)0x41;
	public static final byte CODED_FONT_OBJECT    = (byte)0x42;
	public static final byte OBJECT_CONTAINER     = (byte)0x92;
	public static final byte DOCUMENT_OBJECT      = (byte)0xA8;
	public static final byte PAGE_SEGMENT_OBJECT  = (byte)0xFB;
	public static final byte OVERLAY_OBJECT       = (byte)0xFC;
	public static final byte PAGEDEF_OBJECT       = (byte)0xFD;
	public static final byte FORMDEF_OBJECT       = (byte)0xFE;
	
	/**
	 * Specifies the OCA: 
	 * X'02' Presentation Text 
	 * X'03' Graphics 
	 * X'05' Retired value 
	 * X'06' Image
	 * */
	private int objType;

	private int ArchVrsn;
	
	private int DCAFnSet;
	
	private int OCAFnSet;
	
	public X21Triplet() {
		super();
		this.identifier = ID;
	}
	
	@Override
	protected void readContents(AFPInputStream in) throws IOException {
		if (length < 8 || length > 254) {
			throw new IOException("Invalid X21 Triplet length (8�C254): " + length);
		}
		
		this.objType = in.readCode();
		this.ArchVrsn = in.readCode();
		this.DCAFnSet = in.readCode(2);
		this.OCAFnSet = in.readCode(2);
		
		while (in.remain() > 0) {
			in.readBytes(in.remain());
		}
	}
	
	
	public int getObjType() {
		return objType;
	}

	public void setObjType(int objType) {
		this.objType = objType;
	}


}

