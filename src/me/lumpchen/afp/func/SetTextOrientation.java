package me.lumpchen.afp.func;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;
import me.lumpchen.afp.Page;
import me.lumpchen.afp.render.AFPGraphics;
import me.lumpchen.afp.render.ResourceManager;

public class SetTextOrientation extends Function {

	private int IORNTION;
	private int BORNTION;
	
	SetTextOrientation() {
		this.type = PTX_STO;
	}
	
	@Override
	void readFunction(AFPInputStream in) throws IOException {
		if (this.remain > 0) {
			this.IORNTION = in.readCode(2);
			this.remain -= 2;
		}
		if (remain > 0) {
			this.BORNTION = in.readCode(2);
			this.remain -= 2;
		}
	}
	
	@Override
	public String getCommandString() {
		return "STO";
	}
	
	@Override
	public String getCommandDesc() {
		return "Set Text Orientation";
	}
	
	@Override
	public void render(Page page, AFPGraphics graphics, ResourceManager resourceManager) {
	}
}