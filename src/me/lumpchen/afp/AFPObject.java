package me.lumpchen.afp;

import me.lumpchen.afp.sf.StructureField;
import me.lumpchen.afp.sf.Identifier.Tag;

public abstract class AFPObject {

	protected StructureField structField;
	
	public AFPObject(StructureField structField) {
		this.structField = structField;
	}
	
	public byte[] getStructureData() {
		return this.structField.getData();
	}
	
	public Tag getStructureTag() {
		return this.structField.getStructureTag();
	}
	
}
