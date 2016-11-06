package net.earthcomputer.playinggod.client.renderer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.earthcomputer.playinggod.client.Camera;
import net.earthcomputer.playinggod.universe.Light;
import net.earthcomputer.playinggod.util.MathUtils;
import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;

@SideOnly(Side.CLIENT)
public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_RESOURCE = "shaders/vertex/terrain.vert";
	private static final String FRAGMENT_RESOURCE = "shaders/fragment/terrain.frag";

	private int loc_transformationMatrix;
	private int loc_projectionMatrix;
	private int loc_viewMatrix;
	private int loc_lightPos;
	private int loc_lightColor;
	private int loc_shineDamper;
	private int loc_reflectivity;
	private int loc_skyColor;
	private int loc_fogDensity;
	private int loc_fogGradient;
	private int loc_backgroundTex;
	private int loc_rTex;
	private int loc_gTex;
	private int loc_bTex;
	private int loc_blendMap;

	public TerrainShader() {
		super(VERTEX_RESOURCE, FRAGMENT_RESOURCE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "texCoords");
		bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = getUniformLocation("transformationMatrix");
		loc_projectionMatrix = getUniformLocation("projectionMatrix");
		loc_viewMatrix = getUniformLocation("viewMatrix");
		loc_lightPos = getUniformLocation("lightPos");
		loc_lightColor = getUniformLocation("lightColor");
		loc_shineDamper = getUniformLocation("shineDamper");
		loc_reflectivity = getUniformLocation("reflectivity");
		loc_skyColor = getUniformLocation("skyColor");
		loc_fogDensity = getUniformLocation("fogDensity");
		loc_fogGradient = getUniformLocation("fogGradient");
		loc_backgroundTex = getUniformLocation("backgroundTex");
		loc_rTex = getUniformLocation("rTex");
		loc_gTex = getUniformLocation("gTex");
		loc_bTex = getUniformLocation("bTex");
		loc_blendMap = getUniformLocation("blendMap");
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		loadMatrix(loc_transformationMatrix, matrix);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		loadMatrix(loc_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		loadMatrix(loc_viewMatrix, MathUtils.createViewMatrix(camera));
	}

	public void loadLight(Light light) {
		loadVector(loc_lightPos, light.getPosition().toGLVector());
		loadVector(loc_lightColor, light.getColor());
	}

	public void loadShineVariables(float damper, float reflectivity) {
		loadFloat(loc_shineDamper, damper);
		loadFloat(loc_reflectivity, reflectivity);
	}

	public void loadSkyColor(float r, float g, float b) {
		loadVector(loc_skyColor, new Vector3f(r, g, b));
	}

	public void loadFog(float density, float gradient) {
		loadFloat(loc_fogDensity, density);
		loadFloat(loc_fogGradient, gradient);
	}

	public void connectTextureUnits() {
		loadInt(loc_backgroundTex, 0);
		loadInt(loc_rTex, 1);
		loadInt(loc_gTex, 2);
		loadInt(loc_bTex, 3);
		loadInt(loc_blendMap, 4);
	}

}
