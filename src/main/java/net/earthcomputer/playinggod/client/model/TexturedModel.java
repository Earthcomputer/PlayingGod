package net.earthcomputer.playinggod.client.model;

import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;

@SideOnly(Side.CLIENT)
public class TexturedModel {
	
	private final RawModel rawModel;
	private final ModelTexture tex;
	
	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		this.rawModel = rawModel;
		this.tex = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTex() {
		return tex;
	}

}
