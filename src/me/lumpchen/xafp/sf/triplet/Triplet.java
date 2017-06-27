package me.lumpchen.xafp.sf.triplet;

import java.io.IOException;

import me.lumpchen.xafp.AFPConst;
import me.lumpchen.xafp.AFPInputStream;

public abstract class Triplet {
	
	int length;
	
	/**
	 * Identifies the triplet: 
	 * X'01' Coded Graphic Character Set Global Identifier
	 * 
	 * */
	int identifier;
	
	Triplet() {
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public int getIdentifier() {
		return this.identifier;
	}
	
	abstract protected void readContents(AFPInputStream in) throws IOException;
	
	public static Triplet readTriple(AFPInputStream in) throws IOException {
		int length = in.readUBin(1);
		if (length < 3 || length > 254) {
			throw new IOException("Invalid triplet length (3-254): " + length);
		}
		
		int identifier = in.readUBin(1);
		
		Triplet triplet;
		switch (identifier) {
		case X01Triplet.ID:
			triplet = new X01Triplet();
			break;
		case X02Triplet.ID:
			triplet = new X02Triplet();
			break;
		case X04Triplet.ID:
			triplet = new X04Triplet();
			break;
		case X10Triplet.ID:
			triplet = new X10Triplet();
			break;
		case X1FTriplet.ID:
			triplet = new X1FTriplet();
			break;
		case X21Triplet.ID:
			triplet = new X21Triplet();
			break;
		case X24Triplet.ID:
			triplet = new X24Triplet();
			break;
		case X4BTriplet.ID:
			triplet = new X4BTriplet();
			break;
		case X4CTriplet.ID:
			triplet = new X4CTriplet();
			break;
		case X62Triplet.ID:
			triplet = new X62Triplet();
			break;
		case X63Triplet.ID:
			triplet = new X63Triplet();
			break;
		case X43Triplet.ID:
			triplet = new X43Triplet();
			break;
		default:
			throw new IllegalArgumentException("unknown id: " + AFPConst.bytesToHex((byte) identifier));
		}
		
		triplet.setLength(length);
		triplet.readContents(in);
		
		return triplet;
	}
	
}
