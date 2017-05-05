package me.lumpchen.afp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.lumpchen.afp.sf.StructureField;

public class FontNameMap extends AFPObject {

	/**
	 * FNM Repeating Group Length: 
	 *  X'00' No Raster Data  
	 *  X'08' Raster Data
	 * */
	private ByteArrayOutputStream buffer;
	private int IBMFormat;
	private int TechnologyFormat;
	
	private Map<byte[], Long> second;
	private List<byte[]> third;
	
	public FontNameMap(StructureField structField) {
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
		AFPInputStream in = new AFPInputStream(this.buffer.toByteArray());
		
		try {
			this.IBMFormat = in.readCode();
			this.TechnologyFormat = in.readCode();
			
			this.second = new HashMap<byte[], Long>(repeatGroupLen);
			if (in.remain() > 0) {
				for (int i = 0; i < repeatGroupLen; i++) {
					byte[] GCGID = in.readBytes(8);
					long TSOffset = in.readUnsignedInt();
					this.second.put(GCGID, TSOffset);
					
					System.err.println(AFPConst.ebcdic2Ascii(GCGID));
				}
			}
			if (in.remain() > 0) {
				for (int i = 0; i < repeatGroupLen; i++) {
					int tsiLen = in.readUBin(1);
					byte[] data = in.readBytes(tsiLen - 1);
					
					System.err.println(AFPConst.bytesToHex(data));
				}
			}
			
		} finally {
			in.close();
		}
	}
}
