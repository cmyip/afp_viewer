package me.lumpchen.afp;

import me.lumpchen.afp.sf.StructureField;
import me.lumpchen.afp.sf.Identifier.Tag;

public class MediumMap extends AFPContainer {
	
	public MediumMap(StructureField structField) {
		super(structField);
		this.structField = structField;
		if (this.structField != null) {
			this.nameStr = this.structField.getNameStr();
		}
	}
	
	@Override
	public void collect() {
		
	}
	
	@Override
	public boolean isBegin() {
		if (Tag.BMM == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.EMM == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}
}
