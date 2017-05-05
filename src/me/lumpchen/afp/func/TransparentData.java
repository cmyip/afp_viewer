package me.lumpchen.afp.func;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;
import me.lumpchen.afp.Page;
import me.lumpchen.afp.render.AFPGraphics;

public class TransparentData extends Function {

	private byte[] TRNDATA;
	
	public TransparentData() {
		this.type = PTX_TRN;
	}

	@Override
	public void render(Page page, AFPGraphics graphics) {
		graphics.drawString(new String(this.TRNDATA), 0, 0);
	}
	
	@Override
	void readFunction(AFPInputStream in) throws IOException {
		if (this.remain > 0) {
			this.TRNDATA = in.readBytes(this.remain);
			
			System.err.println(new String(this.TRNDATA));
			this.remain = 0;
		}
	}
	
	@Override
	public String getCommandString() {
		return "TRN";
	}
	
	@Override
	public String getCommandDesc() {
		return "Transparent Data";
	}

}