package me.lumpchen.afp.font;

public class AFPFont {

	public String codePageName;
	public String characterSetName;
	public float fontSize;
	
	public AFPFont(String codePageName, String characterSetName, float fontSize) {
		this.codePageName = codePageName;
		this.characterSetName = characterSetName;
		this.fontSize = fontSize;
	}
}
