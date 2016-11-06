package net.earthcomputer.playinggod.client.model;

import net.earthcomputer.playinggod.client.objloader.OBJLoader;

public class Models {

	private Models() {
	}
	
	public static RawModel quark;
	public static RawModel player;
	
	public static void loadModels(ModelLoader loader) {
		quark = createModel("quark", loader);
		player = createModel("player", loader);
	}
	
	private static RawModel createModel(String name, ModelLoader loader) {
		return OBJLoader.loadObjModel(name).createRawModel(loader);
	}
	
}
