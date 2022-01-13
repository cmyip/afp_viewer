package me.lumpchen.xafp.func;

import me.lumpchen.xafp.AFPConst;
import me.lumpchen.xafp.AFPException;
import me.lumpchen.xafp.AFPInputStream;
import me.lumpchen.xafp.ActiveEnvironmentGroup;
import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.GraphicsState;
import me.lumpchen.xafp.render.ResourceManager;
import me.lumpchen.xafp.render.StructuredAFPGraphics;

import java.io.IOException;
import java.util.logging.Logger;

public class TransparentDataUnchained extends Function {

	private byte[] TRNDATA;
	private int headlength;

	public TransparentDataUnchained(int headlength) {
		this.type = PTX_TRN;
		this.headlength = headlength;
	}

	@Override
	public void render(ActiveEnvironmentGroup aeg, AFPGraphics graphics, ResourceManager resourceManager) {
		if (graphics instanceof StructuredAFPGraphics) {
			((StructuredAFPGraphics) graphics).beginText();
		}
		char[] str = null;
		if (aeg.getCharacterEncoding() == ActiveEnvironmentGroup.CHARACTER_ENCODING_SIGNGLE_BYTE) {
			str = new char[this.TRNDATA.length];
			for (int i = 0; i < this.TRNDATA.length; i++) {
				str[i] = (char) (this.TRNDATA[i] & 0xFF);
			}
		} else if (aeg.getCharacterEncoding() == ActiveEnvironmentGroup.CHARACTER_ENCODING_UNICODE16BE) {
			String s = AFPConst.toUincode16BEString(this.TRNDATA);
			str = s.toCharArray();
		}
		GraphicsState state = graphics.getGraphicsState();
		// state.textState.font = resourceManager.getFontManager().getFont();
		graphics.drawString(str, 0, 0);

		if (graphics instanceof StructuredAFPGraphics) {
			((StructuredAFPGraphics) graphics).endText();
		}
	}

	@Override
	void readFunction(AFPInputStream in) throws IOException {
		this.TRNDATA = in.readBytes(headlength - 2);
		int bytes = in.readSBin(2);
	}

	@Override
	public String getCommandString() {
		return "TRN";
	}

	@Override
	public String getCommandDesc() {
		return "Transparent Data Unchained";
	}

}
