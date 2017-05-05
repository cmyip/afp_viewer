package me.lumpchen.afp.func;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;
import me.lumpchen.afp.Page;
import me.lumpchen.afp.render.AFPGraphics;

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
	public void render(Page page, AFPGraphics graphics) {
		// TODO Auto-generated method stub
		
	}
}