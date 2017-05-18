package me.lumpchen.afp.render;

import me.lumpchen.afp.AFPObject;
import me.lumpchen.afp.CodePage;
import me.lumpchen.afp.Font;
import me.lumpchen.afp.Resource;
import me.lumpchen.afp.ResourceGroup;

public class ResourceManager {

	private FontManager fontManager;

	public ResourceManager(ResourceGroup resourceGroup) {
		this.fontManager = new FontManager();
		
		this.collect(resourceGroup);
	}
	
	public FontManager getFontManager() {
		return this.fontManager;
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
				}
			}
		}
	}
	

}
