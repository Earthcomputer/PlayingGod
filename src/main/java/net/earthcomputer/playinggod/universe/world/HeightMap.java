package net.earthcomputer.playinggod.universe.world;

import java.util.Arrays;

import org.lwjgl.util.vector.Vector2f;

import net.earthcomputer.playinggod.util.MathUtils;
import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;
import net.earthcomputer.playinggod.util.Vec2f;
import net.earthcomputer.playinggod.util.Vec3f;

public class HeightMap {

	public static final int DEFAULT_VERTICES_PER_ROW = 128;

	private final int verticesPerRow;
	private final int arrayLen;

	private float[] heights;
	private Vec3f[] normals;

	public HeightMap(int verticesPerRow) {
		this.verticesPerRow = verticesPerRow;
		if (!isValidVerticesPerRow(verticesPerRow)) {
			throw new IllegalArgumentException("verticesPerRow invalid");
		}
		this.arrayLen = verticesPerRow * verticesPerRow;

		float[] heights = new float[arrayLen];
		Arrays.fill(heights, 0);
		Vec3f[] normals = new Vec3f[arrayLen];
		for (int i = 0; i < arrayLen; i++) {
			normals[i] = new Vec3f(0, 1, 0);
		}
		this.heights = heights;
		this.normals = normals;
	}

	public HeightMap(int verticesPerRow, float[] heights, Vec3f[] normals) {
		this.verticesPerRow = verticesPerRow;
		if (!isValidVerticesPerRow(verticesPerRow)) {
			throw new IllegalArgumentException("verticesPerRow invalid");
		}
		this.arrayLen = verticesPerRow * verticesPerRow;

		if (heights.length != arrayLen) {
			throw new IllegalArgumentException("heights should have a length of " + arrayLen);
		}
		if (normals.length != arrayLen) {
			throw new IllegalArgumentException("normals should have a length of " + arrayLen);
		}

		this.heights = heights;
		this.normals = normals;
	}

	public static boolean isValidVerticesPerRow(int verticesPerRow) {
		// Must be a power of 2 greater than or equal to 4
		return verticesPerRow >= 4 && (verticesPerRow & (verticesPerRow - 1)) == 0;
	}

	public float[] getRawHeightsArray() {
		return heights;
	}

	public Vec3f[] getRawNormalsArray() {
		return normals;
	}

	public float getHeightAt(int x, int z) {
		if (x < 0 || z < 0 || x >= verticesPerRow || z >= verticesPerRow) {
			return 0;
		}
		return heights[z * verticesPerRow + x];
	}

	public float getExactHeight(float xInTerrain, float zInTerrain, float terrainSize) {
		float gridSquareSize = terrainSize / (verticesPerRow - 1);
		int gridX = (int) Math.floor(xInTerrain / gridSquareSize);
		int gridZ = (int) Math.floor(zInTerrain / gridSquareSize);
		if (gridX >= verticesPerRow - 1 || gridZ >= verticesPerRow - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		float xInGridSquare = (xInTerrain % gridSquareSize) / gridSquareSize;
		float zInGridSquare = (zInTerrain % gridSquareSize) / gridSquareSize;
		if (xInGridSquare <= 1 - zInGridSquare) {
			return MathUtils.barryCentric(new Vec3f(0, getHeightAt(gridX, gridZ), 0),
					new Vec3f(1, getHeightAt(gridX + 1, gridZ), 0), new Vec3f(0, getHeightAt(gridX, gridZ + 1), 1),
					new Vec2f(xInGridSquare, zInGridSquare));
		} else {
			return MathUtils.barryCentric(new Vec3f(1, getHeightAt(gridX + 1, gridZ), 0),
					new Vec3f(1, getHeightAt(gridX + 1, gridZ + 1), 1), new Vec3f(0, getHeightAt(gridX, gridZ + 1), 1),
					new Vec2f(xInGridSquare, zInGridSquare));
		}
	}

	public void setHeightAt(int x, int z, float height) {
		if (x < 0 || z < 0 || x >= verticesPerRow || z >= verticesPerRow) {
			return;
		}
		heights[z * verticesPerRow + x] = height;
	}

	public Vec3f getNormalAt(int x, int z) {
		if (x < 0 || z < 0 || x >= verticesPerRow || z >= verticesPerRow) {
			return Vec3f.origin();
		}
		return normals[z * verticesPerRow + x];
	}

	public void setNormalAt(int x, int z, Vec3f normal) {
		if (x < 0 || z < 0 || x >= verticesPerRow || z >= verticesPerRow) {
			return;
		}
		normals[z * verticesPerRow + x] = normal;
	}

	public void generateNormals() {
		for (int x = 0; x < verticesPerRow; x++) {
			for (int z = 0; z < verticesPerRow; z++) {
				genNormalAt(x, z);
			}
		}
	}

	private void genNormalAt(int x, int z) {
		float heightL = getHeightAt(x - 1, z);
		float heightR = getHeightAt(x + 1, z);
		float heightU = getHeightAt(x, z + 1);
		float heightD = getHeightAt(x, z - 1);
		Vec3f normal = new Vec3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalize();
		setNormalAt(x, z, normal);
	}

	@SideOnly(Side.CLIENT)
	public Vector2f getTexCoordFor(int x, int z) {
		return new Vector2f((float) x / (float) (DEFAULT_VERTICES_PER_ROW - 1),
				(float) z / (float) (DEFAULT_VERTICES_PER_ROW - 1));
	}

	public int getVerticesPerRow() {
		return verticesPerRow;
	}

	public int getArraySize() {
		return arrayLen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(heights);
		result = prime * result + Arrays.hashCode(normals);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof HeightMap))
			return false;
		HeightMap other = (HeightMap) obj;
		if (!Arrays.equals(heights, other.heights))
			return false;
		if (!Arrays.equals(normals, other.normals))
			return false;
		return true;
	}

}
