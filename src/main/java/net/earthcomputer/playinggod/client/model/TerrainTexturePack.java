package net.earthcomputer.playinggod.client.model;

import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;

@SideOnly(Side.CLIENT)
public class TerrainTexturePack {

	private final TerrainTexture backgroundTex;
	private final TerrainTexture rTex;
	private final TerrainTexture gTex;
	private final TerrainTexture bTex;

	public TerrainTexturePack(TerrainTexture backgroundTex, TerrainTexture rTex, TerrainTexture gTex,
			TerrainTexture bTex) {
		this.backgroundTex = backgroundTex;
		this.rTex = rTex;
		this.gTex = gTex;
		this.bTex = bTex;
	}

	public TerrainTexture getBackgroundTexture() {
		return backgroundTex;
	}

	public TerrainTexture getRedTexture() {
		return rTex;
	}

	public TerrainTexture getGreenTexture() {
		return gTex;
	}

	public TerrainTexture getBlueTexture() {
		return bTex;
	}

}
