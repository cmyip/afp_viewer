package me.lumpchen.xafp;

import java.io.IOException;

import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.Renderable;
import me.lumpchen.xafp.render.ResourceManager;
import me.lumpchen.xafp.sf.StructureField;
import me.lumpchen.xafp.sf.Identifier.Tag;

public class GraphicsObject extends AFPContainer implements Renderable {

	public GraphicsObject(StructureField structField) throws IOException {
		super(structField);
		this.parseData(this.structField.getData());
	}
	
	private void parseData(byte[] data) throws IOException {
		if (data == null || data.length <= 0) {
			return;
		}
		AFPInputStream in = new AFPInputStream(data);
		try {
			
		} finally {
			in.close();
		}
	}
	
	@Override
	public void render(Page page, AFPGraphics graphics, ResourceManager resourceManager) {
		
	}

	@Override
	public void collect() {
		
	}
	
	@Override
	public boolean isBegin() {
		if (Tag.BGR == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.EGR == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}

}
