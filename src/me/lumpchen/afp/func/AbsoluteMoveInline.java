package me.lumpchen.afp.func;

import java.awt.geom.AffineTransform;
import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;
import me.lumpchen.afp.Page;
import me.lumpchen.afp.render.AFPGraphics;
import me.lumpchen.afp.render.ResourceManager;

public class AbsoluteMoveInline extends Function {

	private int DSPLCMNT;
	
	AbsoluteMoveInline() {
		this.type = PTX_AMI;
	}
	
	@Override
	void readFunction(AFPInputStream in) throws IOException {
		this.DSPLCMNT = in.readSBin(2);
		this.remain -= 2;
	}
	
	@Override
	public void render(Page page, AFPGraphics graphics, ResourceManager resourceManager) {
		double ami = page.unit2Point(this.DSPLCMNT);
		graphics.setTranslateX(ami);
	}
	
	@Override
	public String getCommandString() {
		return "AMI";
	}
	
	@Override
	public String getCommandDesc() {
		return "Absolute Move Inline";
	}

}