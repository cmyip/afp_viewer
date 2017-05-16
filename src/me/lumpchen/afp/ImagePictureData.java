package me.lumpchen.afp;

import java.io.IOException;

import me.lumpchen.afp.sf.StructureField;

public class ImagePictureData extends AFPObject {

	private byte[] IOCAdat;
	
	public ImagePictureData(StructureField structField) throws IOException {
		super(structField);
		this.parseData(this.structField.getData());
	}
	
	private void parseData(byte[] data) throws IOException {
		AFPInputStream in = new AFPInputStream(data);
		try {
			if (in.remain() > 0) {
				this.IOCAdat = in.readBytes(in.remain());
			}
		} finally {
			in.close();
		}
	}
}