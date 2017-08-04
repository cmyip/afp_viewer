package me.lumpchen.xafp.goca;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;
import me.lumpchen.xafp.ActiveEnvironmentGroup;
import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.ResourceManager;

public class SetCurrentPositionOrder extends DrawingOrder {

	private int LENGTH;
	private int XPOS;
	private int YPOS;
	
	public SetCurrentPositionOrder() {
		
	}
	
	public String toString() {
		return "Set Current Position";
	}
	
	@Override
	public void render(ActiveEnvironmentGroup aeg, AFPGraphics graphics, ResourceManager resourceManager) {
		
	}

	@Override
	protected void readOperands(AFPInputStream in) throws IOException {
		this.LENGTH = in.readUBin(1);
		
		this.XPOS = in.readSBin(2);
		this.YPOS = in.readSBin(2);
	}

}
