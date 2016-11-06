package net.earthcomputer.playinggod.client.model;

public class Textures {

	private Textures() {
	}
	
	public static ModelTexture up_quark;
	public static ModelTexture down_quark;
	public static ModelTexture strange_quark;
	public static ModelTexture anti_up_quark;
	public static ModelTexture anti_down_quark;
	public static ModelTexture anti_strange_quark;
	public static ModelTexture player;
	
	public static TerrainTexture grass;
	public static TerrainTexture sand;
	public static TerrainTexture flowers;
	public static TerrainTexture path;
	public static TerrainTexture blend_map;
	
	public static void loadTextures(ModelLoader loader) {
		up_quark = loadMTexture("quark_u", loader);
		down_quark = loadMTexture("quark_d", loader);
		strange_quark = loadMTexture("quark_s", loader);
		anti_up_quark = loadMTexture("quark_au", loader);
		anti_down_quark = loadMTexture("quark_ad", loader);
		anti_strange_quark = loadMTexture("quark_as", loader);
		player = loadMTexture("player", loader);
		
		grass = loadTTexture("grass", loader);
		sand = loadTTexture("sand", loader);
		flowers = loadTTexture("flowers", loader);
		path = loadTTexture("path", loader);
		blend_map = loadTTexture("blendMap", loader);
	}
	
	private static ModelTexture loadMTexture(String name, ModelLoader loader) {
		return new ModelTexture(loader.loadTexture(name));
	}
	
	private static TerrainTexture loadTTexture(String name, ModelLoader loader) {
		return new TerrainTexture(loader.loadTexture(name));
	}
	
}
