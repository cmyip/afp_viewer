package me.lumpchen.xafp.render;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.xafp.CodePage;
import me.lumpchen.xafp.Font;
import me.lumpchen.xafp.FontControl.PatTech;
import me.lumpchen.xafp.font.AFPBitmapFont;
import me.lumpchen.xafp.font.AFPFont;
import me.lumpchen.xafp.font.AFPType1Font;

public class FontManager {

	private Map<String, CodePage> codePageMap;
	private Map<String, Font> charsetMap;
	
	public FontManager() {
		this.codePageMap = new HashMap<String, CodePage>();
		this.charsetMap = new HashMap<String, Font>();
	}
	
	public AFPFont getFont(String codePageName, String characterSetName) {
		CodePage codePage = this.codePageMap.get(codePageName);
		Font charset = this.charsetMap.get(characterSetName);
		
		if (codePage == null || charset == null) {
			return null;
		}
		AFPFont font = null;
		if (PatTech.Laser_Matrix_N_bit_Wide == charset.getPatTech()) {
			font = new AFPBitmapFont(codePage, charset);
		} else if (PatTech.PFB_Type1 == charset.getPatTech()) {
			font = new AFPType1Font(codePage, charset);
		} else if (PatTech.CID_Keyed_font_Type0 == charset.getPatTech()) {
			throw new java.lang.IllegalArgumentException("CID_Keyed_font_Type0 still not implemented.");
		}
		
		return font;
	}
	
	public void addCodePage(String resName, CodePage codePage) {
		this.codePageMap.put(resName, codePage);
	}
	
	public void addCharset(String resName, Font charset) {
		this.charsetMap.put(resName, charset);
	}
	
}
