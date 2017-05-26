package me.lumpchen.afp.ioca;

import java.io.IOException;

import me.lumpchen.afp.AFPInputStream;

public class ImageSegment {
	
	public static final int Begin = 0x70;
	public static final int End = 0x71;
	
	public static final int IDE_Size_ID = 0x96;
	
	private ImageContent imageContent;
	private byte[] name;
	
	public ImageSegment() {
	}
	
	private void readBegin(AFPInputStream in) throws IOException {
		int length = in.readUBin(1);
		
		if (length > 0) {
			this.name = in.readBytes(4);
		}
	}
	
	private void readEnd(AFPInputStream in) throws IOException {
		int length = in.readUBin(1);
	}
	
	public void setImageContent(ImageContent imageContent) {
		this.imageContent = imageContent;
	}
	
	public static ImageSegment parse(byte[] data) throws IOException {
		ImageSegment imageSegment = new ImageSegment();
		ImageContent imageContent = null;
		AFPInputStream in = new AFPInputStream(data);
		
		while (in.remain() > 0) {
			int id = in.readCode();
			if (id == ImageSegment.Begin) {
				imageSegment.readBegin(in);
			} else if (id == ImageSegment.End) {
				imageSegment.readEnd(in);
				break; // read over
			} else if (id == ImageContent.Begin) {
				imageContent = new ImageContent();
				imageContent.readBegin(in);
			} else if (id == ImageContent.End) {
				imageSegment.setImageContent(imageContent);
			} else if (id == ImageSize.ID) {
				ImageSize size = new ImageSize();
				size.read(in);
				imageContent.setImageSize(size);
			} else if (id == ImageEncoding.ID) {
				ImageEncoding encoding = new ImageEncoding();
				encoding.read(in);
				imageContent.setImageEncoding(encoding);
			} else if (id == ImageSegment.IDE_Size_ID) {
				int length = in.readUBin(1);
				int IDESZ = in.readUBin(1);
				imageContent.setIDESize(IDESZ);
			}
		}

		return imageSegment;
	}
}
