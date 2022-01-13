package me.lumpchen.xafp;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.xafp.func.Function;
import me.lumpchen.xafp.render.AFPGraphics;
import me.lumpchen.xafp.render.Renderable;
import me.lumpchen.xafp.render.ResourceManager;
import me.lumpchen.xafp.sf.StructureField;
import me.lumpchen.xafp.sf.Identifier.Tag;

public class ResourceGroup extends AFPContainer implements Renderable {

	private List<Resource> resourceList;

	public ResourceGroup(StructureField structField) {
		super(structField);
		this.resourceList = new ArrayList<Resource>();
		if (this.structField != null) {
			this.nameStr = this.structField.getNameStr();
		}
	}

	public Resource[] getAllResource() {
		return this.resourceList.toArray(new Resource[this.resourceList.size()]);
	}

	public Resource getResource(String resName) {
		for (Resource res : this.resourceList) {
			if (resName.equals(res.getNameStr())) {
				return res;
			}
		}
		return null;
	}

	@Override
	public void collect() {
		for (AFPObject child : this.children) {
			if (child instanceof Resource) {
				Resource res = (Resource) child;
				this.resourceList.add(res);
			}
		}
	}

	@Override
	public boolean isBegin() {
		if (Tag.BRG == this.structField.getStructureTag()) {
			return true;
		} else if (Tag.ERG == this.structField.getStructureTag()) {
			return false;
		}
		return false;
	}


	@Override
	public void render(ActiveEnvironmentGroup aeg, AFPGraphics graphics, ResourceManager resourceManager) {
		for (AFPObject child : children) {
			if (child instanceof Renderable) {
				Renderable renderObj = (Renderable) child;
				renderObj.render(aeg, graphics, resourceManager);
			} else if (child instanceof PresentationTextObject) {
				PresentationTextObject ptxObj = (PresentationTextObject) child;
				for (PresentationTextData ptx : ptxObj.getPTX()) {
					List<Function> cs = ptx.getControlSequence();
					for (Function func : cs) {
						func.render(aeg, graphics, resourceManager);
					}
				}
			}
		}
	}
}
