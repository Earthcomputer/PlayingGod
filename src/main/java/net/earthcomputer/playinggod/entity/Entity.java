package net.earthcomputer.playinggod.entity;

import net.earthcomputer.playinggod.util.Vec3f;
import net.earthcomputer.playinggod.client.model.TexturedModel;
import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;

public class Entity {

	@SideOnly(Side.CLIENT)
	private TexturedModel model;
	@SideOnly(Side.CLIENT)
	private int texIndex;

	private Vec3f position;
	private float rotX, rotY, rotZ;
	private float scale;

	public Entity(Vec3f position, float rotX, float rotY, float rotZ, float scale) {
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public Entity(Vec3f position, int texIndex, float rotX, float rotY, float rotZ, float scale) {
		this.position = position;
		this.texIndex = texIndex;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public void increasePosition(float dx, float dy, float dz) {
		position = new Vec3f(position.getX() + dx, position.getY() + dy, position.getZ() + dz);
	}

	public void increaseRotation(float dx, float dy, float dz) {
		rotX += dx;
		rotY += dy;
		rotZ += dz;
	}

	@SideOnly(Side.CLIENT)
	public TexturedModel getModel() {
		return model;
	}

	@SideOnly(Side.CLIENT)
	public void setModel(TexturedModel model) {
		this.model = model;
	}

	@SideOnly(Side.CLIENT)
	public int getTexIndex() {
		return texIndex;
	}

	@SideOnly(Side.CLIENT)
	public void setTexIndex(int texIndex) {
		this.texIndex = texIndex;
	}

	public Vec3f getPosition() {
		return position;
	}

	public void setPosition(Vec3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
