package me.lumpchen.xafp.func;

import java.io.IOException;

import me.lumpchen.xafp.AFPInputStream;
import me.lumpchen.xafp.Page;
import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.ResourceManager;

public class NoOperation extends Function {

	private byte[] IGNDATA;
	
	public NoOperation() {
		this.type = PTX_DIR;
	}
	
	@Override
	void readFunction(AFPInputStream in) throws IOException {
		if (this.remain > 0) {
			this.IGNDATA = in.readBytes(this.remain);
			this.remain = 0;
		}
	}
	
	@Override
	public String getCommandString() {
		return "NOP";
	}
	
	@Override
	public String getCommandDesc() {
		return "No Operation";
	}
	
	@Override
	public void render(Page page, AFPGraphics graphics, ResourceManager resourceManager) {
		// TODO Auto-generated method stub
		
	}
}