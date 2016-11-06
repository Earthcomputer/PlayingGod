package net.earthcomputer.playinggod.client.model;

import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTexture {

	private final int texId;

	private float shineDamper = 1;
	private float reflectivity = 0;

	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	private int texAtlasRows = 1;

	public ModelTexture(int id) {
		this.texId = id;
	}

	public int getId() {
		return texId;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public boolean hasTransparency() {
		return hasTransparency;
	}

	public void setTransparent(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}
	
	public boolean usesFakeLighting() {
		return useFakeLighting;
	}
	
	public void useFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public int getTextureAtlasRows() {
		return texAtlasRows;
	}

	public void setTextureAtlasRows(int texAtlasRows) {
		this.texAtlasRows = texAtlasRows;
	}
	
	public float getTexXOffset(int index) {
		int column = index % texAtlasRows;
		return (float) column / texAtlasRows;
	}
	
	public float getTexYOffset(int index) {
		int row = index / texAtlasRows;
		return (float) row / texAtlasRows;
	}

}
