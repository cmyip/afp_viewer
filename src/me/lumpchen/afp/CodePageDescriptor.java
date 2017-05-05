package me.lumpchen.afp;

import java.io.IOException;

import me.lumpchen.afp.sf.StructureField;

public class CodePageDescriptor extends AFPObject {

	private byte[] CPDesc;
	private int GCGIDLen;
	private long NumCdPts;
	private int GCSGID;
	private int CPGID;
	
	private int EncScheme;
	
	public CodePageDescriptor(StructureField structField) throws IOException {
		super(structField);
		this.parseData(this.structField.getData());
	}

	private void parseData(byte[] data) throws IOException {
		AFPInputStream in = new AFPInputStream(data);
		try {
			this.CPDesc = in.readBytes(32);
			this.GCGIDLen = in.readUBin(2);
			this.NumCdPts = in.readUnsignedInt();
			this.GCSGID = in.readUBin(2);
			this.CPGID = in.readUBin(2);
			
			if (in.remain() > 0) {
				this.EncScheme = in.readUBin(2);	
			}
			
		} finally {
			in.close();
		}
	}
}
