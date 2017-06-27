package me.lumpchen.xafp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.xafp.sf.StructureField;
import me.lumpchen.xafp.sf.triplet.Triplet;

public class FontControl extends AFPObject {

	private int PatTech;
	
	private int FntFlags;
	
	private int XUnitBase;
	private int YUnitBase;
	
	private int XftUnits;
	private int YftUnits;
	private int MaxBoxWd;
	private int MaxBoxHt;
	private int FNORGLen;
	private int FNIRGLen;
	
	private int PatAlign;
	private int RPatDCnt;
	private int FNPRGLen;
	private int FNMRGLen;
	
	private List<Triplet> triplets;

	private int ResXUBase;

	private int ResYUBase;

	private int XfrUnits;

	private int YfrUnits;

	private long OPatDCnt;

	private int FNNRGLen;

	private long FNNDCnt;

	private int FNNMapCnt;
	
	public FontControl(StructureField structField) throws IOException {
		super(structField);
		this.parseData(this.structField.getData());
	}
	
	public int getFNORGLen() {
		return this.FNORGLen;
	}
	
	public int getFNPRGLen() {
		return this.FNPRGLen;
	}
	
	public int getFNIRGLen() {
		return this.FNIRGLen;
	}
	
	public int getFNMRGLen() {
		return this.FNMRGLen;
	}
	
	public int getFNNMapCnt() {
		return this.FNNMapCnt;
	}
	
	public int getPatAlign() {
		return PatAlign;
	}

	public int getRPatDCnt() {
		return RPatDCnt;
	}
	
	public int getPatTech() {
		return PatTech;
	}

	private void parseData(byte[] data) throws IOException {
		AFPInputStream in = new AFPInputStream(data);
		try {
			in.readUBin(1);
			this.PatTech = in.readCode();
			
			in.readUBin(1);
			
			this.FntFlags = in.readCode();
			
			this.XUnitBase = in.readCode();
			this.YUnitBase = in.readCode();
			
			this.XftUnits = in.readUBin(2);
			this.YftUnits = in.readUBin(2);
			this.MaxBoxWd = in.readUBin(2);
			this.MaxBoxHt = in.readUBin(2);
			this.FNORGLen = in.readUBin(1);
			this.FNIRGLen = in.readUBin(1);
			
			this.PatAlign = in.readCode();
			this.RPatDCnt = in.readUBin(3);
			this.FNPRGLen = in.readUBin(1);
			this.FNMRGLen = in.readUBin(1);
			
			if (in.remain() > 0) {
				this.ResXUBase = in.readCode();
			}
			if (in.remain() > 0) {
				this.ResYUBase = in.readCode();
			}
			
			if (in.remain() > 0) {
				this.XfrUnits = in.readUBin(2);
			}
			
			if (in.remain() > 0) {
				this.YfrUnits = in.readUBin(2);
			}
			
			if (in.remain() > 0) {
				this.OPatDCnt = in.readUnsignedInt();
			}
			
			if (in.remain() > 0) {
				in.readBytes(3);
			}
			
			if (in.remain() > 0) {
				this.FNNRGLen = in.readUBin(1);
			}
			
			if (in.remain() > 0) {
				this.FNNDCnt = in.readUnsignedInt();
			}
			
			if (in.remain() > 0) {
				this.FNNMapCnt = in.readUBin(2);
			}
			
			while (in.remain() > 0) {
				Triplet triplet = Triplet.readTriple(in);
				if (this.triplets == null) {
					this.triplets = new ArrayList<Triplet>();
				}
				this.triplets.add(triplet);
			}
			
		} finally {
			in.close();
		}
	}
}
 