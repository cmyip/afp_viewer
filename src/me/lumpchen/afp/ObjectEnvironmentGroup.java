package me.lumpchen.afp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.afp.sf.StructureField;
import me.lumpchen.afp.sf.triplet.Triplet;

public class ObjectEnvironmentGroup extends AFPObject {

	private byte[] OEGName;
	private List<Triplet> triplets;
	
	public ObjectEnvironmentGroup(StructureField structField) throws IOException {
		super(structField);
		this.parseData(this.structField.getData());
	}
	
	private void parseData(byte[] data) throws IOException {
		AFPInputStream in = new AFPInputStream(data);
		try {
			if (in.remain() > 0) {
				this.OEGName = in.readBytes(8);
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
