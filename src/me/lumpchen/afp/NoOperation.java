package me.lumpchen.afp;

import java.io.IOException;

import me.lumpchen.afp.sf.StructureField;

public class NoOperation extends AFPObject {

	private byte[] UndfData;
	
	public NoOperation(StructureField structField) throws IOException {
		super(structField);
		this.UndfData = this.structField.getData();
	}

	public byte[] getData() {
		return this.UndfData;
	}
}
