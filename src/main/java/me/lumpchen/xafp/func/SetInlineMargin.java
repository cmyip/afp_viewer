package me.lumpchen.xafp.func;

import me.lumpchen.xafp.AFPInputStream;
import me.lumpchen.xafp.ActiveEnvironmentGroup;
import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.ResourceManager;

import java.io.IOException;

public class SetInlineMargin extends Function {

	private int DISPLCMNT;

	SetInlineMargin() {
		this.type = PTX_SBI;
	}

	@Override
	public void render(ActiveEnvironmentGroup aeg, AFPGraphics graphics, ResourceManager resourceManager) {
		System.out.println(getCommandDesc() + " " + this.DISPLCMNT);
	}

	@Override
	void readFunction(AFPInputStream in) throws IOException {
		this.DISPLCMNT = in.readSBin(2);
		this.remain -= 2;
	}

	@Override
	public String getCommandString() {
		return "SIM";
	}

	@Override
	public String getCommandDesc() {
		return "Set Inline Margin";
	}
}
