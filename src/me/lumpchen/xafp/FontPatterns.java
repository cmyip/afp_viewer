package me.lumpchen.xafp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import me.lumpchen.xafp.sf.StructureField;

public class FontPatterns extends AFPObject {
	/**
	 * X'05': Laser Matrix N-bit Wide 
	 * X'1E': CID Keyed font (Type 0) 
	 * X'1F': PFB (Type 1)
	 * */
	public enum PatTech {
		Laser_Matrix_N_bit_Wide(0x05), CID_Keyed_font_Type0(0x1E), PFB_Type1(0x1F);
		
		private int id;
		private PatTech(int id) {
			this.id = id;
		}
		
		public final static PatTech instance(int id) {
			switch (id) {
			case 0x05:
				return Laser_Matrix_N_bit_Wide;
			case 0x1E:
				return CID_Keyed_font_Type0;
			case 0x01F:
				return PFB_Type1;
			default:
				throw new IllegalArgumentException("Invalid font pattern id: " + id);
			}
		}
		
		public int getID() {
			return this.id;
		}
	};
	
	private ByteArrayOutputStream buffer;
	private byte[] fontData;

	private PatTech patTech;
	private int dataAlignment;
	private int rasterDataCount;
	
	private byte[] techSpecObjectIdentifier;
	private long OFLLen;
	private long Checksum;
	private int TIDLen;
	private int ODescLen;
	private byte[] ObjDesc;
	
	public FontPatterns(StructureField structField) {
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
	
	public byte[] getData() {
		return this.buffer.toByteArray();
	}
	
	public PatTech getPatTech() {
		return patTech;
	}
	
	public byte[] getFontData() {
		return this.fontData;
	}
	
	public void parseData(int patternTechnologyIdentifier, int dataAlignment, int rasterDataCount) throws IOException {
		byte[] data = this.buffer.toByteArray();

		this.patTech = PatTech.instance(patternTechnologyIdentifier);
		this.dataAlignment = dataAlignment;
		this.rasterDataCount = rasterDataCount;
		
		AFPInputStream in = new AFPInputStream(data);
		try {
			if (this.patTech == PatTech.Laser_Matrix_N_bit_Wide) {
				this.fontData = data;
			} else {
				this.OFLLen = in.readUnsignedInt();
				this.Checksum = in.readUnsignedInt();
				this.TIDLen = in.readUBin(2);
				if (this.TIDLen > 0) {
					this.techSpecObjectIdentifier = in.readBytes(this.TIDLen - 2);
				}
				if (this.patTech != PatTech.PFB_Type1) {
					this.ODescLen = in.readUBin(2);
					this.ObjDesc = in.readBytes(this.ODescLen - 2);
				}
				this.fontData = in.readBytes(in.remain());
			}
		} finally {
			in.close();
		}
	}
}
