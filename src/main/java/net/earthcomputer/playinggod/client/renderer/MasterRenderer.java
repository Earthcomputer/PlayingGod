package net.earthcomputer.playinggod.client.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import net.earthcomputer.playinggod.client.Camera;
import net.earthcomputer.playinggod.client.gui.GuiTexture;
import net.earthcomputer.playinggod.client.model.ModelLoader;
import net.earthcomputer.playinggod.client.model.TexturedModel;
import net.earthcomputer.playinggod.client.model.Textures;
import net.earthcomputer.playinggod.entity.Entity;
import net.earthcomputer.playinggod.universe.Light;
import net.earthcomputer.playinggod.universe.world.Terrain;
import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;
import net.earthcomputer.playinggod.util.Vec2f;

@SideOnly(Side.CLIENT)
public class MasterRenderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;

	private static final float SKY_RED = 0;
	private static final float SKY_GREEN = 0;
	private static final float SKY_BLUE = 0.05f;

	private static final float FOG_DENSITY = 0.001f;
	private static final float FOG_GRADIENT = 5;

	private Matrix4f projectionMatrix;

	private EntityShader entityShader = new EntityShader();
	private EntityRenderer entityRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	private GuiShader guiShader = new GuiShader();
	private GuiRenderer guiRenderer;

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	public MasterRenderer(ModelLoader loader) {
		enableCulling();
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		guiRenderer = new GuiRenderer(loader);
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(Light sun, Camera camera) {
		prepare();

		// Entities
		entityShader.start();
		entityShader.loadFog(FOG_DENSITY, FOG_GRADIENT);
		entityShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
		entityShader.loadLight(sun);
		entityShader.loadViewMatrix(camera);

		entityRenderer.render(entities);

		entityShader.stop();
		entities.clear();

		// Terrain
		terrainShader.start();
		terrainShader.loadFog(FOG_DENSITY, FOG_GRADIENT);
		terrainShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);

		terrainRenderer.render(terrains);

		terrainShader.stop();
		terrains.clear();

		guiRenderer.render(Collections
				.singletonList(new GuiTexture(Textures.anti_down_quark.getId(), Vec2f.origin(), new Vec2f(1, 1))));
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);

		if (batch == null) {
			batch = new ArrayList<Entity>();
			entities.put(entityModel, batch);
		}

		batch.add(entity);
	}

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void prepare() {
		GL11.glClearColor(SKY_RED, SKY_GREEN, SKY_BLUE, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float xScale = yScale / aspectRatio;
		float frustrumLength = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustrumLength);
		projectionMatrix.m33 = 0;
	}

}
