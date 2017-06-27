package me.lumpchen.xafp.render;

import me.lumpchen.xafp.Page;

public interface Renderable {
	
	public void render(Page page, AFPGraphics graphics, ResourceManager resourceManager);
	
}
