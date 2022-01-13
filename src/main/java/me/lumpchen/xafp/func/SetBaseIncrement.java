package me.lumpchen.xafp.func;

import me.lumpchen.xafp.AFPInputStream;
import me.lumpchen.xafp.ActiveEnvironmentGroup;
import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.ResourceManager;

import java.io.IOException;

public class SetBaseIncrement extends Function {

	private int INCRMENT;

	SetBaseIncrement() {
		this.type = PTX_SBI;
	}

	@Override
	public void render(ActiveEnvironmentGroup aeg, AFPGraphics graphics, ResourceManager resourceManager) {
		System.out.println(getCommandDesc() + " " + this.INCRMENT);
	}

	@Override
	void readFunction(AFPInputStream in) throws IOException {
		this.INCRMENT = in.readSBin(2);
		this.remain -= 2;

		int bytes = in.readSBin(2);
	}

	@Override
	public String getCommandString() {
		return "SBI";
	}

	@Override
	public String getCommandDesc() {
		return "Set Base Increment";
	}
}
