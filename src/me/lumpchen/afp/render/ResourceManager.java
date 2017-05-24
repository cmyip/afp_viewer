package me.lumpchen.afp.render;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.afp.AFPObject;
import me.lumpchen.afp.CodePage;
import me.lumpchen.afp.Font;
import me.lumpchen.afp.ObjectContainer;
import me.lumpchen.afp.ObjectContainer.ObjectTypeIdentifier;
import me.lumpchen.afp.Resource;
import me.lumpchen.afp.ResourceGroup;

public class ResourceManager {

	private FontManager fontManager;
	private Map<String, ObjectContainer> objMap;

	public ResourceManager(ResourceGroup resourceGroup) {
		this.fontManager = new FontManager();
		this.objMap = new HashMap<String, ObjectContainer>();
		
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
					System.out.println(key);
				} else {
					System.out.println(type);
				}
			}
		}
	}
	

}
