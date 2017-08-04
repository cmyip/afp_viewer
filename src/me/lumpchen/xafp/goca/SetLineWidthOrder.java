package me.lumpchen.xafp.goca;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;
import me.lumpchen.xafp.ActiveEnvironmentGroup;
import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.ResourceManager;

public class SetLineWidthOrder extends DrawingOrder {

	private int MH;
	
	public SetLineWidthOrder() {
		
	}
	
	public String toString() {
		return "Set Line Width";
	}
	
	@Override
	public void render(ActiveEnvironmentGroup aeg, AFPGraphics graphics, ResourceManager resourceManager) {
		
	}

	@Override
	protected void readOperands(AFPInputStream in) throws IOException {
		this.MH = in.readUBin(1);
	}

}
