package net.earthcomputer.playinggod.client.model;

public class TexturedModels {

	private TexturedModels() {
	}

	public static TexturedModel upQuark;
	public static TexturedModel downQuark;
	public static TexturedModel strangeQuark;
	public static TexturedModel antiUpQuark;
	public static TexturedModel antiDownQuark;
	public static TexturedModel antiStrangeQuark;
	public static TexturedModel player;

	public static void loadTexturedModels() {
		upQuark = loadModel(Models.quark, Textures.up_quark);
		downQuark = loadModel(Models.quark, Textures.down_quark);
		strangeQuark = loadModel(Models.quark, Textures.strange_quark);
		antiUpQuark = loadModel(Models.quark, Textures.anti_up_quark);
		antiDownQuark = loadModel(Models.quark, Textures.anti_down_quark);
		antiStrangeQuark = loadModel(Models.quark, Textures.anti_strange_quark);
		player = loadModel(Models.player, Textures.player);
	}

	private static TexturedModel loadModel(RawModel model, ModelTexture texture) {
		return new TexturedModel(model, texture);
	}

}
