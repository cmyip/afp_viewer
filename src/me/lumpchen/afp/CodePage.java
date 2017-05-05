package me.lumpchen.afp;

import me.lumpchen.afp.sf.StructureField;

import java.io.IOException;

import me.lumpchen.afp.sf.Identifier.Tag;

public class CodePage extends AFPContainer {
	
	private CodePageDescriptor descriptor;
	private CodePageIndex index;
	private CodePageControl control;
	
	public CodePage(StructureField structField) throws IOException {
		super(structField);
		this.structField = structField;
		if (this.structField != null) {
			this.nameStr = this.structField.getNameStr();
		}
	}
	
	public boolean addChild(AFPObject child) {
		boolean ret = this.children.add(child);
		if (child instanceof CodePageDescriptor) {
			this.descriptor = (CodePageDescriptor) child;
		} else if (child instanceof CodePageIndex) {
			this.index = (CodePageIndex) child;
			try {
				this.index.parseEntries(this.control.getCPIRGLen());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (child instanceof CodePageControl) {
			this.control = (CodePageControl) child;
		} else if (child instanceof NoOperation) {
			// ignore?
		}  else {
			throw new AFPException("Illegal child of CodePage: " + child);
		}
		return ret;
	}
	
	@Override
	public boolean isBegin() {
		if (Tag.BCP == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.ECP == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}

	@Override
	public void collect() {
	}
}
