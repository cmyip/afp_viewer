package me.lumpchen.afp;

import me.lumpchen.afp.sf.Identifier.Tag;
import me.lumpchen.afp.sf.StructureField;

public class ResourceGroup extends AFPContainer {

	public ResourceGroup(StructureField structField) {
		super(structField);
		if (this.structField != null) {
			this.nameStr = this.structField.getNameStr();
		}
	}
	
	public boolean addResource(Resource resource) {
		return this.addChild(resource);
	}
	
	@Override
	public boolean isBegin() {
		if (Tag.BRG == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.ERG == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}

	@Override
	public void collect() {
		// TODO Auto-generated method stub
		
	}
	
}
