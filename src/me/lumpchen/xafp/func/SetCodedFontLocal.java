package me.lumpchen.xafp.func;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;
import me.lumpchen.xafp.MapCodedFontFormat2;
import me.lumpchen.xafp.Page;
import me.lumpchen.xafp.font.AFPFont;
import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.ResourceManager;

public class SetCodedFontLocal extends Function {

	private int LID;
	
	public SetCodedFontLocal() {
		this.type = PTX_SCFL;
	}
	
	@Override
	void readFunction(AFPInputStream in) throws IOException {
		this.LID = in.readCode();
		this.remain -= 1;
	}
	
	@Override
	public String getCommandString() {
		return "SCFL";
	}
	
	@Override
	public String getCommandDesc() {
		return "Set Coded Font Local";
	}

	@Override
	public void render(Page page, AFPGraphics graphics, ResourceManager resourceManager) {
		MapCodedFontFormat2.Attribute mcf = page.getMapCodedFont(this.LID);
		if (mcf != null) {
			AFPFont font = resourceManager.getFontManager().getFont(mcf.codePageName, mcf.characterSetName);
			graphics.setAFPFont(font, mcf.fontSize);
		}
	}
}











