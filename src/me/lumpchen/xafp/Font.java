package me.lumpchen.xafp;

import java.io.IOException;
import java.util.Map;

import me.lumpchen.xafp.sf.StructureField;
import me.lumpchen.xafp.sf.Identifier.Tag;

public class Font extends AFPContainer {
	
	private FontDescriptor desciptor;
	private FontControl control;
	private FontOrientation orientation;
	private FontPosition position;
	private FontIndex index;
	private FontNameMap nameMap;
	private FontPatterns patterns;
	
	public Font(StructureField structField) {
		super(structField);
		this.structField = structField;
		if (this.structField != null) {
			this.nameStr = this.structField.getNameStr();
		}
	}
	
	public Map<String, String> getNameMap() {
		return this.nameMap.getNameMap();
	}
	
	public String getTypefaceStr() {
		if (this.desciptor == null) {
			return "";
		}
		String face = AFPConst.ebcdic2Ascii(this.desciptor.getTypeFcDesc());
		return face;
	}
	
	public FontPatterns getFontPatterns() {
		return this.patterns;
	}
	
	@Override
	public boolean isBegin() {
		if (Tag.BFN == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.EFN == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}

	@Override
	public void collect() {
		try {
			for (int i = 0; i < this.children.size(); i++) {
				AFPObject child = this.children.get(i);
				
				if (child instanceof FontDescriptor) {
					this.desciptor = (FontDescriptor) child;
				} else if (child instanceof FontControl) {
					this.control = (FontControl) child;
				} else if (child instanceof FontOrientation) {
					this.orientation = (FontOrientation) child;
					this.orientation.parseData(this.control.getFNORGLen());
				} else if (child instanceof FontPosition) {
					this.position = (FontPosition) child;
					this.position.parseData(this.control.getFNPRGLen());
				} else if (child instanceof FontIndex) {
					if (this.index == null) {
						this.index = (FontIndex) child;
					} else {
						this.index.appendData(child.getStructureData());
					}
				} else if (child instanceof FontNameMap) {
					if (this.nameMap == null) {
						this.nameMap = (FontNameMap) child;
					} else {
						this.nameMap.appendData(child.getStructureData());
					}
				} else if (child instanceof FontPatterns) {
					if (this.patterns == null) {
						this.patterns = (FontPatterns) child;
					} else {
						this.patterns.appendData(child.getStructureData());
					}
				}
			}
			
			if (this.index != null) {
				this.index.parseData(this.control.getFNIRGLen());
			}
			if (this.nameMap != null) {
				this.nameMap.parseData(this.control.getFNNMapCnt());
			}
			if (this.patterns != null) {
				this.patterns.parseData(this.control.getPatTech(), this.control.getPatAlign(), this.control.getRPatDCnt());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
