package me.lumpchen.afp;

import java.io.IOException;

import me.lumpchen.afp.sf.StructureField;

public class MediumCopyCount extends AFPObject {

	public MediumCopyCount(StructureField structField) throws IOException {
		super(structField);
		this.parseData(this.structField.getData());
	}
	
	private void parseData(byte[] data) throws IOException {
		AFPInputStream in = new AFPInputStream(data);
	}
}