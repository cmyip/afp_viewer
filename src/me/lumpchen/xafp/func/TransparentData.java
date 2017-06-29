package me.lumpchen.xafp.func;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;
import me.lumpchen.xafp.ActiveEnvironmentGroup;
import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.ResourceManager;

public class TransparentData extends Function {

	private byte[] TRNDATA;
	
	public TransparentData() {
		this.type = PTX_TRN;
	}

	@Override
	public void render(ActiveEnvironmentGroup aeg, AFPGraphics graphics, ResourceManager resourceManager) {
//		graphics.drawString(new String(this.TRNDATA), 0, 0);
		graphics.drawString(this.TRNDATA, 0, 0);
	}
	
	@Override
	void readFunction(AFPInputStream in) throws IOException {
		if (this.remain > 0) {
			this.TRNDATA = in.readBytes(this.remain);
			this.remain = 0;
		}
	}
	
	@Override
	public String getCommandString() {
		return "TRN";
	}
	
	@Override
	public String getCommandDesc() {
		return "Transparent Data";
	}

}