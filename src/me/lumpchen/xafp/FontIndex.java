package me.lumpchen.xafp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.xafp.sf.StructureField;

public class FontIndex extends AFPObject {
	
	static class FNI {

		public byte[] GCGID;
		public int CharInc;
		public int AscendHt;
		public int DescendDp;
		public int FNMCnt;
		public int ASpace;
		public int BSpace;
		public int CSpace;
		public int BaseOset;
	}
	
	private ByteArrayOutputStream buffer;
	private List<FNI> repeatGroup;
	
	public FontIndex(StructureField structField) {
		super(structField);
		this.appendData(super.getStructureData());
	}
	
	public void appendData(byte[] data) {
		try {
			if (this.buffer == null) {
				this.buffer = new ByteArrayOutputStream();
				this.buffer.write(data);
			} else {
				this.buffer.write(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void parseData(int repeatGroupLen) throws IOException {
		byte[] data = this.buffer.toByteArray();
		if (data.length % repeatGroupLen > 0) {
			throw new AFPException("Invalid repeating group length (FNI): " + repeatGroupLen);
		}
		int repeat = data.length / repeatGroupLen;
		this.repeatGroup = new ArrayList<FNI>(repeat);
		AFPInputStream in = new AFPInputStream(data);
		try {
			for (int i = 0; i < repeat; i++) {
				FNI fni = new FNI();
				fni.GCGID = in.readBytes(8);
				fni.CharInc = in.readUBin(2);
				fni.AscendHt = in.readSBin(2);
				fni.DescendDp = in.readSBin(2);
				
				in.readUBin(2);
				
				fni.FNMCnt = in.readUBin(2);
				fni.ASpace = in.readSBin(2);
				fni.BSpace = in.readUBin(2);
				fni.CSpace = in.readSBin(2);
				
				in.readUBin(2);
				
				fni.BaseOset = in.readSBin(2);
				
				this.repeatGroup.add(fni);
			}
		} finally {
			in.close();
		}
	}
}
