package me.lumpchen.afp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.afp.sf.StructureField;
import me.lumpchen.afp.sf.triplet.Triplet;

public class ObjectAreaDescriptor extends AFPObject {

	private List<Triplet> triplets;
	
	public ObjectAreaDescriptor(StructureField structField) throws IOException {
		super(structField);
		this.parseData(this.structField.getData());
	}
	
	private void parseData(byte[] data) throws IOException {
		AFPInputStream in = new AFPInputStream(data);
		try {
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
