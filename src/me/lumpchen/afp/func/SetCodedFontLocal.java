package me.lumpchen.afp.func;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;
import me.lumpchen.afp.MapCodedFontFormat2;
import me.lumpchen.afp.Page;
import me.lumpchen.afp.font.AFPFont;
import me.lumpchen.afp.render.AFPGraphics;

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
	public void render(Page page, AFPGraphics graphics) {
		MapCodedFontFormat2.Attribute mcf = page.getMapCodedFont(this.LID);
		if (mcf != null) {
			AFPFont font = new AFPFont(mcf.codePageName, mcf.characterSetName, mcf.fontSize);
			graphics.setAFPFont(font);
		}
	}
}











