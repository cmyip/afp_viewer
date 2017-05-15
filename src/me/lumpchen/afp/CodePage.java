package me.lumpchen.afp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.lumpchen.afp.sf.Identifier.Tag;
import me.lumpchen.afp.sf.StructureField;

public class CodePage extends AFPContainer {
	
	private CodePageDescriptor descriptor;
	private CodePageIndex index;
	private CodePageControl control;
	
	private Map<Integer, Integer> unicode2CodePointMap;
	private Map<Integer, Integer> codePoint2UnicodeMap;
	
	public CodePage(StructureField structField) throws IOException {
		super(structField);
		this.structField = structField;
		if (this.structField != null) {
			this.nameStr = this.structField.getNameStr();
		}
	}
	
	public Map<Integer, Integer> unicode2CodePointMap() {
		return this.unicode2CodePointMap;
	}
	
	public Map<Integer, Integer> codePoint2UnicodeMap() {
		return this.codePoint2UnicodeMap;
	}
	
	@Override
	public void collect() {
		for (AFPObject child : this.children) {
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
		}
		
		this.unicode2CodePointMap = new HashMap<Integer, Integer>();
		this.codePoint2UnicodeMap = new HashMap<Integer, Integer>();
		if (this.index != null) {
			CodePageIndex.Entry[] entries = this.index.getEntries();
			for (CodePageIndex.Entry entry : entries) {
				int codePoint = entry.CodePoint;
				String gcgid = entry.getGCGIDStr();
				int unicode = GCGIDDatabase.getUnicode(gcgid);
				if (unicode > 0) {
					this.unicode2CodePointMap.put(unicode, codePoint);
					this.codePoint2UnicodeMap.put(codePoint, unicode);
				}
			}
		}
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

}
