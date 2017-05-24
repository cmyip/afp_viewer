package me.lumpchen.afp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.afp.sf.StructureField;
import me.lumpchen.afp.sf.Identifier.Tag;
import me.lumpchen.afp.sf.triplet.Triplet;

public class ImageObject extends AFPContainer {

	private byte[] IdoName;
	private List<Triplet> triplets;
	
	public ImageObject(StructureField structField) throws IOException {
		super(structField);
		this.parseData(this.structField.getData());
	}
	
	@Override
	public void collect() {
		
	}

	private void parseData(byte[] data) throws IOException {
		AFPInputStream in = new AFPInputStream(data);
		try {
			if (in.remain() > 0) {
				this.IdoName = in.readBytes(8);
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

	@Override
	public boolean isBegin() {
		if (Tag.BIM == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.EIM == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}
}
