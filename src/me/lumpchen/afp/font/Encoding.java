package me.lumpchen.afp.font;

public interface Encoding {

	public int getMaxCodePoint();
	
	public int getMinCodePoint();
	
	public int getCodePoint(int unicode);
	
	public String getCharacterName(int codepoint);

	public int getUnicode(int codepoint);
	
	public boolean isDefinedCodePoint(int codepoint);
}
