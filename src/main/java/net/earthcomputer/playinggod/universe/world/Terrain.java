package net.earthcomputer.playinggod.universe.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

import net.earthcomputer.playinggod.client.model.ModelLoader;
import net.earthcomputer.playinggod.client.model.RawModel;
import net.earthcomputer.playinggod.client.model.TerrainTexture;
import net.earthcomputer.playinggod.client.model.TerrainTexturePack;
import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;
import net.earthcomputer.playinggod.util.Vec2f;
import net.earthcomputer.playinggod.util.Vec3f;

public class Terrain {

	private static final float SIZE = 800;

	private int gridX;
	private int gridZ;

	private float x;
	private float z;

	private HeightMap heightMap;

	@SideOnly(Side.CLIENT)
	private RawModel model;
	@SideOnly(Side.CLIENT)
	private TerrainTexturePack texturePack;
	@SideOnly(Side.CLIENT)
	private TerrainTexture blendMap;

	@SideOnly(Side.CLIENT)
	private ModelLoader modelLoader;

	private static final Random random = new Random(0);

	private static final Map<Vec2f, Terrain> terrains = new HashMap<Vec2f, Terrain>();

	/**
	 * If called on the client,
	 * {@link #setTextures(TerrainTexturePack, TerrainTexture, ModelLoader)} and
	 * {@link #regenerateModel()} should be called in that order after
	 * instantiation
	 */
	public Terrain(int gridX, int gridZ) {
		terrains.put(new Vec2f(gridX, gridZ), this);
		this.gridX = gridX;
		this.gridZ = gridZ;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.heightMap = generateHeightMap(gridX, gridZ, random);
	}

	public static Terrain getTerrainAt(float worldX, float worldZ) {
		return terrains.get(new Vec2f((float) Math.floor(worldX / SIZE), (float) Math.floor(worldZ / SIZE)));
	}

	public static float getExactHeight(float worldX, float worldZ) {
		Terrain terrain = getTerrainAt(worldX, worldZ);
		if (terrain == null) {
			return 0;
		}
		return terrain.getHeightMap().getExactHeight(worldX - terrain.x, worldZ - terrain.z, SIZE);
	}

	@SideOnly(Side.CLIENT)
	public void setTextures(TerrainTexturePack texPack, TerrainTexture blendMap, ModelLoader loader) {
		this.texturePack = texPack;
		this.blendMap = blendMap;
		this.modelLoader = loader;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public float getGridX() {
		return gridX;
	}

	public float getGridZ() {
		return gridZ;
	}

	@SideOnly(Side.CLIENT)
	public RawModel getModel() {
		return model;
	}

	@SideOnly(Side.CLIENT)
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	@SideOnly(Side.CLIENT)
	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	public HeightMap getHeightMap() {
		return heightMap;
	}

	private static HeightMap generateHeightMap(int x, int z, Random random) {
		random.setSeed((long) x * 2394872398L + (long) z * 243948237L);
		HeightMap heightMap = HeightMapGenerator.generateHeightMap(random, HeightMap.DEFAULT_VERTICES_PER_ROW);
		heightMap.generateNormals();
		return heightMap;
	}

	@SideOnly(Side.CLIENT)
	public void regenerateModel() {
		int verticesPerRow = heightMap.getVerticesPerRow();
		int arrSize = heightMap.getArraySize();

		// Data from height map
		float[] heights = heightMap.getRawHeightsArray();
		Vec3f[] normalVectors = heightMap.getRawNormalsArray();

		// Data we're producing
		float[] vertices = new float[arrSize * 3];
		float[] normals = new float[arrSize * 3];
		float[] textureCoords = new float[arrSize * 2];
		int[] indices = new int[6 * (verticesPerRow - 1) * (verticesPerRow - 1)];

		// Generate vertices, normals and tex coords
		int index = 0;
		for (int z = 0; z < verticesPerRow; z++) {
			for (int x = 0; x < verticesPerRow; x++) {
				// vertex
				vertices[index * 3] = (float) x / ((float) verticesPerRow - 1) * SIZE;
				vertices[index * 3 + 1] = heights[index];
				vertices[index * 3 + 2] = (float) z / ((float) verticesPerRow - 1) * SIZE;

				// normal
				Vec3f normal = normalVectors[index];
				normals[index * 3] = normal.getX();
				normals[index * 3 + 1] = normal.getY();
				normals[index * 3 + 2] = normal.getZ();

				// tex coord
				Vector2f texCoord = heightMap.getTexCoordFor(x, z);
				textureCoords[index * 2] = texCoord.x;
				textureCoords[index * 2 + 1] = texCoord.y;

				index++;
			}
		}

		// Generate indices
		index = 0;
		for (int gz = 0; gz < verticesPerRow - 1; gz++) {
			for (int gx = 0; gx < verticesPerRow - 1; gx++) {
				int topLeft = (gz * verticesPerRow) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * verticesPerRow) + gx;
				int bottomRight = bottomLeft + 1;
				indices[index++] = topLeft;
				indices[index++] = bottomLeft;
				indices[index++] = topRight;
				indices[index++] = topRight;
				indices[index++] = bottomLeft;
				indices[index++] = bottomRight;
			}
		}

		this.model = modelLoader.loadToVAO(vertices, textureCoords, normals, indices);
	}

}
