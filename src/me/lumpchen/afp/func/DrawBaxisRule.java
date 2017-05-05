package me.lumpchen.afp.func;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;
import me.lumpchen.afp.Page;
import me.lumpchen.afp.render.AFPGraphics;

public class DrawBaxisRule extends Function {

	private int RLENGTH;
	private int RWIDTH;
	
	public DrawBaxisRule() {
		this.type = PTX_DIR;
	}

	@Override
	public void render(Page page, AFPGraphics graphics) {
	}
	
	@Override
	void readFunction(AFPInputStream in) throws IOException {
		this.RLENGTH = in.readSBin(2);
		this.remain -= 2;
		
		if (this.remain > 0) {
			this.RWIDTH = in.readSBin(3);
			this.remain -= 3;
		}
	}
	
	@Override
	public String getCommandString() {
		return "DBR";
	}
	
	@Override
	public String getCommandDesc() {
		return "Draw B-axis Rule";
	}

}