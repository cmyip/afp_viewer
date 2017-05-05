package me.lumpchen.afp;

import me.lumpchen.afp.sf.StructureField;
import me.lumpchen.afp.sf.Identifier.Tag;

public class PresentationTextObject extends AFPContainer {
	
	private PresentationTextData ptx;
	
	public PresentationTextObject(StructureField structField) {
		super(structField);
		this.structField = structField;
		if (this.structField != null) {
			this.nameStr = this.structField.getNameStr();
		}
	}
	
	public PresentationTextData getPTX() {
		return this.ptx;
	}
	
	@Override
	public boolean isBegin() {
		if (Tag.BPT == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.EPT == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}

	@Override
	public void collect() {
		for (AFPObject child : this.children) {
			if (child instanceof PresentationTextData) {
				this.ptx = (PresentationTextData) child;
			}
		}
	}
}
