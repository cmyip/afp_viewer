package me.lumpchen.afp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.afp.ioca.ImageSegment;
import me.lumpchen.afp.render.AFPGraphics;
import me.lumpchen.afp.render.Renderable;
import me.lumpchen.afp.render.ResourceManager;
import me.lumpchen.afp.sf.Identifier.Tag;
import me.lumpchen.afp.sf.StructureField;
import me.lumpchen.afp.sf.triplet.Triplet;

public class ImageObject extends AFPContainer implements Renderable {

	private byte[] IdoName;
	private List<Triplet> triplets;
	
	private ObjectEnvironmentGroup oeg;
	
	private ImageSegment imgSegment;
	
	public ImageObject(StructureField structField) throws IOException {
		super(structField);
		this.parseData(this.structField.getData());
	}
	
	@Override
	public void collect() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			for (AFPObject child : this.children) {
				if (child instanceof ObjectEnvironmentGroup) {
					this.oeg = (ObjectEnvironmentGroup) child;
				} else if (child instanceof ImagePictureData) {
					ImagePictureData ipd = (ImagePictureData) child;
					os.write(ipd.getIOCAdata());
				}
			}
			
			byte[] imageSegmentData = os.toByteArray();
			this.imgSegment = new ImageSegment();
			this.imgSegment.read(new AFPInputStream(imageSegmentData));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void parseData(byte[] data) throws IOException {
		AFPInputStream in = new AFPInputStream(data);
		try {
			if (in.remain() > 0) {
				this.IdoName = in.readBytes(8);
			}
			
			while (in.remain() > 0) {
				Triplet triplet = Triplet.readTriple(in);
				if (this.triplets == null) {
					this.triplets = new ArrayList<Triplet>();
				}
				this.triplets.add(triplet);
			}
			
		} finally {
			in.close();
		}
	}
	
	@Override
	public void render(Page page, AFPGraphics graphics, ResourceManager resourceManager) {

	}

	@Override
	public boolean isBegin() {
		if (Tag.BIM == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.EIM == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}

}
