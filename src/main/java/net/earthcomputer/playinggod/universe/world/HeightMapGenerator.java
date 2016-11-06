package net.earthcomputer.playinggod.universe.world;

import java.util.Random;

public class HeightMapGenerator {

	//private static final int MAX_HEIGHT = 50;

	private HeightMapGenerator() {
	}

	public static HeightMap generateHeightMap(Random random, int finalResolution) {
		HeightMap map = new HeightMap(finalResolution);
		float[] heights = map.getRawHeightsArray();
		for (int i = 0; i < heights.length; i++) {
			heights[i] = random.nextFloat() * 3.5f;
		}
		return map;
	}

}
