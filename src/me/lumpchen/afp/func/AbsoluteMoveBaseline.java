package me.lumpchen.afp.func;

import java.awt.geom.AffineTransform;
import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;
import me.lumpchen.afp.Page;
import me.lumpchen.afp.render.AFPGraphics;
import me.lumpchen.afp.render.ResourceManager;

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
		double amb = page.unit2Point(this.displacement);
		graphics.setTranslateY(amb);
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