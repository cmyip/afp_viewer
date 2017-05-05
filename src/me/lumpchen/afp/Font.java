package me.lumpchen.afp;

import java.io.IOException;

import org.apache.fontbox.type1.Type1Font;

import me.lumpchen.afp.FontPatterns.PatTech;
import me.lumpchen.afp.sf.Identifier.Tag;
import me.lumpchen.afp.sf.StructureField;

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
	
	public void parse() throws IOException {
		PatTech patTech = this.patterns.getPatTech();
		if (patTech == PatTech.PFB_Type1) {
			byte[] fdata = this.patterns.getFontData();
			Type1Font type1Font = Type1Font.createWithPFB(fdata);
			System.err.println(type1Font.getFamilyName());
		}
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
				this.parse();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
