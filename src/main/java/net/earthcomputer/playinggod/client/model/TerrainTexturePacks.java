package net.earthcomputer.playinggod.client.model;

public class TerrainTexturePacks {

	private TerrainTexturePacks() {
	}
	
	public static TerrainTexturePack dflt;
	
	public static void loadTerrainTexturePacks() {
		dflt = loadTexPack(Textures.grass, Textures.sand, Textures.flowers, Textures.path);
	}
	
	private static TerrainTexturePack loadTexPack(TerrainTexture backTex, TerrainTexture rTex, TerrainTexture gTex, TerrainTexture bTex) {
		return new TerrainTexturePack(backTex, rTex, gTex, bTex);
	}
	
}
