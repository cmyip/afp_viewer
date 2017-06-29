package me.lumpchen.xafp;

import me.lumpchen.xafp.sf.StructureField;
import me.lumpchen.xafp.sf.Identifier.Tag;

public class ActiveEnvironmentGroup extends AFPContainer {

	private MapCodedFontFormat2 mcf;
	private PageDescriptor pageDesciptor;
	private PresentationTextDescriptor ptd;
	
	private double scaleRatio;
	
	private double hRes;
	private double vRes;
	
	private double width;
	private double height;
	
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
		
		if (this.pageDesciptor != null) {
			this.hRes = ((double) this.pageDesciptor.getXpgUnits()) / this.pageDesciptor.getXpgBase();
			this.vRes = ((double) this.pageDesciptor.getXpgUnits()) / this.pageDesciptor.getXpgBase();
			
			this.scaleRatio = this.pageDesciptor.getXpgBase() / this.pageDesciptor.getXpgUnits();
			this.width = this.unit2Point(this.pageDesciptor.getXpgSize());
			this.height = this.unit2Point(this.pageDesciptor.getYpgSize());
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
	
	public double unit2Inch(int val) {
		double inch = ((double) val) * this.scaleRatio;
		return inch;
	}
	
	public double unit2Point(int val) {
		double pt = ((double) val) * this.scaleRatio * 72;
		return pt;
	}

	public MapCodedFontFormat2.Attribute getMapCodedFont(int ResLID) {
		return this.getMapCodedFontFormat2().getResourceAttributes(ResLID);
	}
	
	public MapCodedFontFormat2 getMcf() {
		return mcf;
	}

	public PageDescriptor getPageDesciptor() {
		return pageDesciptor;
	}

	public PresentationTextDescriptor getPtd() {
		return ptd;
	}

	public double getScaleRatio() {
		return scaleRatio;
	}

	public double gethRes() {
		return hRes;
	}

	public double getvRes() {
		return vRes;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
	
}
