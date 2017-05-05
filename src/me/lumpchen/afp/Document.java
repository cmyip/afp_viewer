package me.lumpchen.afp;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.afp.sf.Identifier.Tag;
import me.lumpchen.afp.sf.StructureField;

public class Document extends AFPContainer {

	private List<Page> pages;
	
	public Document(StructureField structField) {
		super(structField);
		this.structField = structField;
		if (this.structField != null) {
			this.nameStr = this.structField.getNameStr();
		}
		this.pages = new ArrayList<Page>();
	}
	
	public List<Page> getPageList() {
		return this.pages;
	}
	
	@Override
	public boolean isBegin() {
		if (Tag.BDT == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.EDT == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}

	@Override
	public void collect() {
		for (AFPObject child : this.children) {
			if (child instanceof Page) {
				this.pages.add((Page) child);
			} else if (child instanceof NamedPageGroup) {
				this.pages.addAll(((NamedPageGroup) child).getPageList());
			}
		}
	}
}
