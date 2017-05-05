package me.lumpchen.afp;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.afp.sf.StructureField;

public abstract class AFPContainer extends AFPObject {
	
	protected List<AFPObject> children;
	protected String nameStr;
	
	public AFPContainer(StructureField structField) {
		super(structField);
		this.children = new ArrayList<AFPObject>();
		if (this.structField != null) {
			this.nameStr = this.structField.getNameStr();
		}
	}
	
	public boolean addChild(AFPObject child) {
		return this.children.add(child);
	}
	
	public String getNameStr() {
		return this.nameStr;
	}
	
	@Override
	public String toString() {
		return this.structField.getStructureTag().getDesc();
	}
	
	public abstract void collect();
	
	public abstract boolean isBegin();
	
}
