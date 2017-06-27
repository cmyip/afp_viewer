package me.lumpchen.xafp.render;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.xafp.AFPObject;
import me.lumpchen.xafp.CodePage;
import me.lumpchen.xafp.Font;
import me.lumpchen.xafp.ImageObject;
import me.lumpchen.xafp.ObjectContainer;
import me.lumpchen.xafp.Resource;
import me.lumpchen.xafp.ResourceGroup;
import me.lumpchen.xafp.ObjectContainer.ObjectTypeIdentifier;

public class ResourceManager {

	private FontManager fontManager;
	private Map<String, ObjectContainer> objMap;
	private Map<String, ImageObject> iocaObjMap;

	public ResourceManager(ResourceGroup resourceGroup) {
		this.fontManager = new FontManager();
		this.objMap = new HashMap<String, ObjectContainer>();
		this.iocaObjMap = new HashMap<String, ImageObject>();
		
		this.collect(resourceGroup);
	}
	
	public FontManager getFontManager() {
		return this.fontManager;
	}
	
	public ObjectTypeIdentifier getObjectTypeIdentifier(String resName) {
		ObjectContainer obj = this.objMap.get(resName);
		if (obj == null) {
			return null;
		}
		return obj.getObjectTypeIdentifier();
	}
	
	public byte[] getObjectData(String resName) {
		ObjectContainer obj = this.objMap.get(resName);
		if (obj == null) {
			return null;
		}
		return obj.getObjectData();
	}
	
	public ImageObject getIOCAObject(String resName) {
		if (this.iocaObjMap.containsKey(resName)) {
			return this.iocaObjMap.get(resName);
		}
		
		return null;
	}
	
	private void collect(ResourceGroup resourceGroup) {
		for (Resource res : resourceGroup.getAllResource()) {
			Resource.Type type = res.getType();
			if (type != null) {
				if (Resource.Type.CODE_PAGE == type) {
					AFPObject[] children = res.getChildren();
					for (AFPObject child : children) {
						if (child instanceof CodePage) {
							this.fontManager.addCodePage(res.getNameStr(), (CodePage) child);
						}
					}
				} else if (Resource.Type.CHARACTER_SET == type) {
					AFPObject[] children = res.getChildren();
					for (AFPObject child : children) {
						if (child instanceof Font) {
							this.fontManager.addCharset(res.getNameStr(), (Font) child);
						}
					}
				} else if (Resource.Type.OBJECT_CONTAINER == type) {
					String key = res.getNameStr();
					AFPObject[] children = res.getChildren();
					for (AFPObject child : children) {
						if (child instanceof ObjectContainer) {
							this.objMap.put(key, (ObjectContainer) child);
						}
					}
				} else if (Resource.Type.IOCA == type) {
					String key = res.getNameStr();
					AFPObject[] children = res.getChildren();
					for (AFPObject child : children) {
						if (child instanceof ImageObject) {
							this.iocaObjMap.put(key, (ImageObject) child);
						}
					}
				} else {
					System.err.println(type);
				}
			}
		}
	}
	

}
