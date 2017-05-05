package me.lumpchen.afp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.afp.sf.StructureField;
import me.lumpchen.afp.sf.triplet.Triplet;

public class IncludeObject extends AFPObject {

	private byte[] ObjName;
	private int ObjType;
	private int XoaOset;
	private int YoaOset;
	private int XoaOrent;
	private int YoaOrent;
	private int XocaOset;
	private int YocaOset;
	private int RefCSys;
	private List<Triplet> Triplets;

	public IncludeObject(StructureField structField) throws IOException {
		super(structField);
		this.Triplets = new ArrayList<Triplet>();
		this.parseData(this.structField.getData());
	}

	private void parseData(byte[] data) throws IOException {
		AFPInputStream in = new AFPInputStream(data);
		try {
			this.ObjName = in.readBytes(8);
			in.read(); // reserverd
			
			this.ObjType = in.readCode();
			
			this.XoaOset = in.readSBin(3);
			this.YoaOset = in.readSBin(3);
			
			this.XoaOrent = in.readCode(2);
			this.YoaOrent = in.readCode(2);
			
			this.XocaOset = in.readSBin(3);
			this.YocaOset = in.readSBin(3);
			
			this.RefCSys = in.readCode();
			
			while (in.remain() > 0) {
				// read triplets
				Triplet triplet = Triplet.readTriple(in);
				this.Triplets.add(triplet);
			}
		} finally {
			in.close();
		}
	}

	public byte[] getObjName() {
		return ObjName;
	}

	public int getObjType() {
		return ObjType;
	}

	public int getXoaOset() {
		return XoaOset;
	}

	public int getYoaOset() {
		return YoaOset;
	}

	public int getXoaOrent() {
		return XoaOrent;
	}

	public int getYoaOrent() {
		return YoaOrent;
	}

	public int getXocaOset() {
		return XocaOset;
	}

	public int getYocaOset() {
		return YocaOset;
	}

	public int getRefCSys() {
		return RefCSys;
	}

	public List<Triplet> getTriplets() {
		return Triplets;
	}
}












