package me.lumpchen.afp.func;

import java.awt.Color;
import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;
import me.lumpchen.afp.Page;
import me.lumpchen.afp.render.AFPGraphics;

public class SetTextColor extends Function {

	private int FRGCOLOR;
	private int PRECSION;
	
	public SetTextColor() {
		this.type = PTX_STC;
	}
	
	@Override
	void readFunction(AFPInputStream in) throws IOException {
		in.readCode(2);
		this.remain -= 2;
		if (this.remain > 0) {
			this.PRECSION = in.read();
			this.remain -= 1;
		}
	}
	
	@Override
	public String getCommandString() {
		return "STC";
	}
	
	@Override
	public String getCommandDesc() {
		return "Set Text Color";
	}

	@Override
	public void render(Page page, AFPGraphics graphics) {
		graphics.setColor(Color.black);
	}
}