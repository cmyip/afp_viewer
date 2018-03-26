package me.lumpchen.xafp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.xafp.sf.StructureField;

public class PagePosition extends AFPObject {
	
	public static final int Constant = 0x01;
	private List<Position> group;

	public PagePosition(StructureField structField) throws IOException {
		super(structField);
		this.parseData(this.structField.getData());
	}
	
	private void parseData(byte[] data) throws IOException {
		AFPInputStream in = new AFPInputStream(data);
		
		int constant = in.readCode();
		if (Constant != constant) {
			throw new IOException("Reserved constant; must be X'01'");
		}
		this.group = new ArrayList<Position>();
		if (in.remain() > 0) {
			int RGLength = in.readUBin(1);
			byte[] repeatBytes = in.readBytes(RGLength - 2);
			AFPInputStream repeatStream = new AFPInputStream(repeatBytes);
			
		}
	}
	
	public static class Position {
		public int XmOset;
		public int YmOset;
		public int PGorient;
		public int SHside;
		
		public int PgFlgs;
		public int PMCid;
	}
}
