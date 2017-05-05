package me.lumpchen.afp;

import me.lumpchen.afp.sf.Identifier.Tag;
import me.lumpchen.afp.sf.StructureField;

public class ObjectContainer extends AFPContainer {
	
	public ObjectContainer(StructureField structField) {
		super(structField);
		this.structField = structField;
		if (this.structField != null) {
			this.nameStr = this.structField.getNameStr();
		}
	}
	
	@Override
	public boolean isBegin() {
		if (Tag.BOC == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.EOC == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}

	@Override
	public void collect() {
	}
}
