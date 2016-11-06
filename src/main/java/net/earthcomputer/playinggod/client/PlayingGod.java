package net.earthcomputer.playinggod.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import net.earthcomputer.playinggod.client.model.ModelLoader;
import net.earthcomputer.playinggod.client.model.Models;
import net.earthcomputer.playinggod.client.model.TerrainTexturePacks;
import net.earthcomputer.playinggod.client.model.TexturedModel;
import net.earthcomputer.playinggod.client.model.TexturedModels;
import net.earthcomputer.playinggod.client.model.Textures;
import net.earthcomputer.playinggod.client.renderer.MasterRenderer;
import net.earthcomputer.playinggod.client.renderer.ShaderProgram;
import net.earthcomputer.playinggod.entity.Entity;
import net.earthcomputer.playinggod.entity.Player;
import net.earthcomputer.playinggod.universe.Light;
import net.earthcomputer.playinggod.universe.world.Terrain;
import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;
import net.earthcomputer.playinggod.util.Vec3f;

@SideOnly(Side.CLIENT)
public class PlayingGod {

	private static PlayingGod INSTANCE;

	private ModelLoader modelLoader;
	private MasterRenderer renderer;
	private List<IKeyboardListener> keyboardListeners = new ArrayList<IKeyboardListener>();
	private List<Runnable> futureTasks = new ArrayList<Runnable>();

	private PlayingGod() {
	}

	public static void main(String[] args) {
		PlayingGod game = new PlayingGod();
		INSTANCE = game;
		game.initialize();
		game.loop();
		game.stop();
	}

	public static PlayingGod getInstance() {
		return INSTANCE;
	}

	private void initialize() {
		DisplayManager.createDisplay();

		System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));

		modelLoader = new ModelLoader();

		DisplayManager.drawLogo(modelLoader);

		renderer = new MasterRenderer(modelLoader);

		Models.loadModels(modelLoader);
		Textures.loadTextures(modelLoader);
		TexturedModels.loadTexturedModels();
		TerrainTexturePacks.loadTerrainTexturePacks();
	}

	private void loop() {
		// In the beginning, God created the heavens and the Earth
		Light sun = new Light(new Vec3f(0, 2000, -40), new Vector3f(1, 0.95f, 0.9f));

		Terrain terrain = new Terrain(0, -1);
		terrain.setTextures(TerrainTexturePacks.dflt, Textures.blend_map, modelLoader);
		terrain.regenerateModel();

		// Then He filled the Earth with animals...
		TexturedModel[] models = { TexturedModels.upQuark, TexturedModels.downQuark, TexturedModels.strangeQuark,
				TexturedModels.antiUpQuark, TexturedModels.antiDownQuark, TexturedModels.antiStrangeQuark };

		Random random = new Random();
		Entity[] entities = new Entity[200];
		for (int i = 0; i < entities.length; i++) {
			entities[i] = new Entity(
					new Vec3f(random.nextInt(500) - 250, random.nextInt(90) + 10, random.nextInt(500) - 250),
					random.nextInt(360), random.nextInt(360), random.nextInt(360), 1);
			entities[i].setModel(models[random.nextInt(models.length)]);
		}

		// ... and man
		Player player = new Player(new Vec3f(0, 0, -20), 0, 0, 0, 1);
		player.setModel(TexturedModels.player);

		Camera camera = new Camera(player);
		addKeyboardListener(camera);

		// This is where our story starts
		while (!Display.isCloseRequested()) {
			while (Keyboard.next()) {
				char typedChar = Keyboard.getEventCharacter();
				int keyCode = Keyboard.getEventKey();
				boolean repeat = Keyboard.isRepeatEvent();
				if (Keyboard.getEventKeyState()) {
					for (IKeyboardListener listener : keyboardListeners) {
						listener.onKeyPress(typedChar, keyCode, repeat);
					}
				} else {
					for (IKeyboardListener listener : keyboardListeners) {
						listener.onKeyRelease(typedChar, keyCode, repeat);
					}
				}
			}

			synchronized (futureTasks) {
				int numFutureTasks = futureTasks.size();
				for (int i = 0; i < numFutureTasks; i++) {
					futureTasks.get(i).run();
				}

				futureTasks.clear();
			}

			player.move();
			camera.move();

			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			if (!camera.isFirstPersonMode()) {
				renderer.processEntity(player);
			}

			renderer.processTerrain(terrain);

			renderer.render(sun, camera);

			DisplayManager.updateDisplay();
			checkGlError();
		}
	}

	private void checkGlError() {
		int errorCode = GL11.glGetError();
		if (errorCode != GL11.GL_NO_ERROR) {
			System.err.println("GL Error: " + GLU.gluErrorString(errorCode));
		}
	}

	private void stop() {
		ShaderProgram.cleanUp();
		modelLoader.cleanUp();
		DisplayManager.closeDisplay();
	}

	public void addKeyboardListener(IKeyboardListener keyboardListener) {
		keyboardListeners.add(keyboardListener);
	}

	public void removeKeyboardListener(IKeyboardListener keyboardListener) {
		keyboardListeners.remove(keyboardListener);
	}

	public void addFutureTask(Runnable futureTask) {
		synchronized (futureTasks) {
			futureTasks.add(futureTask);
		}
	}

	public ModelLoader getModelLoader() {
		return modelLoader;
	}

	public MasterRenderer getRenderer() {
		return renderer;
	}

}
