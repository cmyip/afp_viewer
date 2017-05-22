package me.lumpchen.afp.render;

import me.lumpchen.afp.Page;

public interface Renderable {
	
	public void render(Page page, AFPGraphics graphics, ResourceManager resourceManager);
	
}
