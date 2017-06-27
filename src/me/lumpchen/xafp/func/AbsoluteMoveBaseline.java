package me.lumpchen.xafp.func;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;
import me.lumpchen.xafp.Page;
import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.ResourceManager;

/**
 * The Absolute Move Baseline control sequence moves the baseline coordinate 
 * relative to the I-axis.
 * */

public class AbsoluteMoveBaseline extends Function {

	private int displacement;
	
	public AbsoluteMoveBaseline() {
		this.type = PTX_SEC;
	}
	
	@Override
	void readFunction(AFPInputStream in) throws IOException {
		int DSPLCMNT = in.readSBin(2);
		this.displacement = DSPLCMNT;
		this.remain -= 2;
	}

	@Override
	public void render(Page page, AFPGraphics graphics, ResourceManager resourceManager) {
		float amb = (float) page.unit2Point(this.displacement);
		graphics.setTextPosY(amb);
	}
	
	@Override
	public String getCommandString() {
		return "SEC";
	}
	
	@Override
	public String getCommandDesc() {
		return "Absolute Move Baseline";
	}

}