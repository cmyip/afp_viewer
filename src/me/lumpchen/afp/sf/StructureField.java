package me.lumpchen.afp.sf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.afp.AFPConst;
import me.lumpchen.afp.AFPInputStream;
import me.lumpchen.afp.StructureFieldReader.SFDataReader;
import me.lumpchen.afp.sf.Identifier.Tag;
import me.lumpchen.afp.sf.triplet.Triplet;

public class StructureField {

	private Introducer introducer;
	private byte[] sfData;
	private byte[] nameBytes;
	private List<Triplet> triplets;
	
	public StructureField(Introducer introducer) {
		this.introducer = introducer;
	}
	
	@Override
	public String toString() {
		return this.introducer.getIdentifier().getTag().toString();
	}
	
	public byte[] getData() {
		return this.sfData;
	}
	
	public Tag getStructureTag() {
		return this.introducer.getIdentifier().getTag();
	}
	
	public String getNameStr() {
		return AFPConst.ebcdic2Ascii(this.nameBytes);
	}
	
	public byte[] getNameBytes() {
		return this.nameBytes;
	}
	
	public void setNameBytes(byte[] nameBytes) {
		this.nameBytes = nameBytes;
	}
	
	public void addTriplet(Triplet triplet) {
		if (this.triplets == null) {
			this.triplets = new ArrayList<Triplet>();
		}
		this.triplets.add(triplet);
	}
	
	public void read(AFPInputStream in, SFDataReader dataReader) throws IOException {
		int hasRead = this.introducer.getLength();
		
		int remain = this.introducer.getStructureFieldLength() - hasRead;
		if (remain > 0) {
			this.sfData = in.readBytes(remain);
			dataReader.read(this);
		}
	}
	
}
