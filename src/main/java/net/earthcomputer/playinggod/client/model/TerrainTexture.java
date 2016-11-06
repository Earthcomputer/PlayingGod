package net.earthcomputer.playinggod.client.model;

import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;

@SideOnly(Side.CLIENT)
public class TerrainTexture {

	private final int texId;

	public TerrainTexture(int texId) {
		this.texId = texId;
	}

	public int getTextureId() {
		return texId;
	}

}
