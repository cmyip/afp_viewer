package me.lumpchen.afp;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import me.lumpchen.afp.ObjectContainer.ObjectTypeIdentifier;
import me.lumpchen.afp.render.AFPGraphics;
import me.lumpchen.afp.render.Renderable;
import me.lumpchen.afp.render.ResourceManager;
import me.lumpchen.afp.sf.StructureField;
import me.lumpchen.afp.sf.triplet.Triplet;

public class IncludeObject extends AFPObject implements Renderable {

	private byte[] ObjName;
	private int ObjType;
	private int XoaOset;
	private int YoaOset;
	private int XoaOrent;
	private int YoaOrent;
	private int XocaOset;
	private int YocaOset;
	private int RefCSys;
	private List<Triplet> Triplets;

	public IncludeObject(StructureField structField) throws IOException {
		super(structField);
		this.Triplets = new ArrayList<Triplet>();
		this.parseData(this.structField.getData());
	}

	private void parseData(byte[] data) throws IOException {
		AFPInputStream in = new AFPInputStream(data);
		try {
			this.ObjName = in.readBytes(8);
			in.read(); // reserverd
			
			this.ObjType = in.readCode();
			
			this.XoaOset = in.readSBin(3);
			this.YoaOset = in.readSBin(3);
			
			this.XoaOrent = in.readCode(2);
			this.YoaOrent = in.readCode(2);
			
			this.XocaOset = in.readSBin(3);
			this.YocaOset = in.readSBin(3);
			
			this.RefCSys = in.readCode();
			
			while (in.remain() > 0) {
				// read triplets
				Triplet triplet = Triplet.readTriple(in);
				this.Triplets.add(triplet);
			}
		} finally {
			in.close();
		}
	}

	public byte[] getObjName() {
		return ObjName;
	}

	public int getObjType() {
		return ObjType;
	}

	public int getXoaOset() {
		return XoaOset;
	}

	public int getYoaOset() {
		return YoaOset;
	}

	public int getXoaOrent() {
		return XoaOrent;
	}

	public int getYoaOrent() {
		return YoaOrent;
	}

	public int getXocaOset() {
		return XocaOset;
	}

	public int getYocaOset() {
		return YocaOset;
	}

	public int getRefCSys() {
		return RefCSys;
	}

	public List<Triplet> getTriplets() {
		return Triplets;
	}

	@Override
	public void render(Page page, AFPGraphics graphics, ResourceManager resourceManager) {
		int x = (int) Math.round(page.unit2Point(this.XoaOset));
		int y = (int) Math.round(page.unit2Point(this.YoaOset));
		
		String resName = AFPConst.ebcdic2Ascii(this.ObjName);
		
		ObjectTypeIdentifier objectTypeIdentifier = resourceManager.getObjectTypeIdentifier(resName);
		ObjectTypeIdentifier.Component component = objectTypeIdentifier.getComponent();
		if (ObjectTypeIdentifier.Component.JFIF == component) {
			byte[] imageData = resourceManager.getObjectData(resName);
			try {
				BufferedImage bimg = ImageIO.read(new ByteArrayInputStream(imageData));
				graphics.drawImage(bimg, x, y);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}












