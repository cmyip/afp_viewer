package me.lumpchen.xafp.func;

import me.lumpchen.xafp.AFPInputStream;
import me.lumpchen.xafp.ActiveEnvironmentGroup;
import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.ResourceManager;

import java.io.IOException;

public class BeginLine extends Function {

	private byte[] IGNDATA;

	public BeginLine() {
		this.type = PTX_BLN;
	}

	@Override
	void readFunction(AFPInputStream in) throws IOException {
		int bytes = in.readSBin(2);
	}

	@Override
	public String getCommandString() {
		return "BLN";
	}

	@Override
	public String getCommandDesc() {
		return "Begin Line";
	}

	@Override
	public void render(ActiveEnvironmentGroup aeg, AFPGraphics graphics, ResourceManager resourceManager) {
		// TODO Auto-generated method stub

	}
}
