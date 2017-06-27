package me.lumpchen.xafp;

import me.lumpchen.xafp.sf.StructureField;
import me.lumpchen.xafp.sf.Identifier.Tag;

public class ActiveEnvironmentGroup extends AFPContainer {

	private MapCodedFontFormat2 mcf;
	private PageDescriptor pageDesciptor;
	private PresentationTextDescriptor ptd;
	
	public ActiveEnvironmentGroup(StructureField structField) {
		super(structField);
		this.structField = structField;
		if (this.structField != null) {
			this.nameStr = this.structField.getNameStr();
		}
	}
	
	public MapCodedFontFormat2 getMapCodedFontFormat2() {
		return this.mcf;
	}
	
	public PageDescriptor getPageDescriptor() {
		return this.pageDesciptor;
	}
	
	public PresentationTextDescriptor getPresentationTextDescriptor() {
		return this.ptd;
	}
	
	@Override
	public void collect() {
		for (int i = 0; i < this.children.size(); i++) {
			AFPObject child = this.children.get(i);
			if (child instanceof MapCodedFontFormat2) {
				this.mcf = (MapCodedFontFormat2) child;
			} else if (child instanceof PageDescriptor) {
				this.pageDesciptor = (PageDescriptor) child;
			} else if (child instanceof PresentationTextDescriptor) {
				this.ptd = (PresentationTextDescriptor) child;
			} else {
				
			}
		}
	}
	
	@Override
	public boolean isBegin() {
		if (Tag.BAG == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.EAG == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}

}
