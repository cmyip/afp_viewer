package me.lumpchen.afp.render;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.afp.CodePage;
import me.lumpchen.afp.Font;
import me.lumpchen.afp.font.AFPFont;
import me.lumpchen.afp.font.AFPCodedFont;

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
		
		AFPFont font = new AFPCodedFont(codePage, charset);
		return font;
	}
	
	public void addCodePage(String resName, CodePage codePage) {
		this.codePageMap.put(resName, codePage);
	}
	
	public void addCharset(String resName, Font charset) {
		this.charsetMap.put(resName, charset);
	}
	
}
