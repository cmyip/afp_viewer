package me.lumpchen.xafp.font;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.fontbox.util.BoundingBox;

import me.lumpchen.xafp.CodePage;
import me.lumpchen.xafp.Font;
import me.lumpchen.xafp.FontPatternsMap;
import me.lumpchen.xafp.FontPatternsMap.Pattern;

public class AFPBitmapFont implements AFPFont {
	
	private CodePage codePage;
	private Font charset;
	
	private Encoding encoding;
	private FontPatternsMap patternsMap;
	private ByteBuffer patternData;
	
	public AFPBitmapFont(CodePage codePage, Font charset) {
		this.codePage = codePage;
		this.charset = charset;
		
		this.patternsMap = this.charset.getPatternsMap();
		this.patternData = ByteBuffer.wrap(this.charset.getFontPatterns().getFontData());
		this.initEncoding(this.codePage, charset);
	}
	
	private void initEncoding(final CodePage codePage, final Font charset) {
		this.encoding = new Encoding() {
			@Override
			public int getMaxCodePoint() {
				return charset.getPatternsMap().getPatterCount();
			}

			@Override
			public int getMinCodePoint() {
				return 0;
			}

			@Override
			public int getCodePoint(int unicode) {
				Integer codepoint = (Integer) codePage.unicode2CodePointMap().get(new Integer(unicode));
				if (codepoint != null) {
					return codepoint.intValue();
				}
				return 0xFFFF;
			}

			@Override
			public String getCharacterName(int codepoint) {
				String cid = codePage.getCodePoint2CharIDMap().get(new Integer(codepoint));
				if (cid == null) {
					return codePage.getDefaultCID();
				}
				return cid;
			}

			@Override
			public int getUnicode(int codepoint) {
				Integer unicode = (Integer) codePage.codePoint2UnicodeMap().get(new Integer(codepoint));
				if (unicode != null) {
					return unicode.intValue();
				}
				return 0xFFFF;
			}

			@Override
			public boolean isDefinedCodePoint(int codepoint) {
				return codePage.getCodePoint2CharIDMap().containsKey(codepoint);
			}};
	}
	
	public byte[] getBitmap(int codePoint) throws IOException {
		Pattern pattern = this.patternsMap.getPattern(codePoint);
		if (pattern == null) {
			return null;
		}
		return null;
	}
	
	@Override
	public Encoding getEncoding() {
		return this.encoding;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoundingBox getFontBBox() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Number> getFontMatrix() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getWidth(String name) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasGlyph(String name) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getTechSpecName(String gcgid) {
		return null;
	}
}
