package net.earthcomputer.playinggod.universe;

import org.lwjgl.util.vector.Vector3f;

import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;
import net.earthcomputer.playinggod.util.Vec3f;

@SideOnly(Side.CLIENT)
public class Light {

	private Vec3f position;
	private Vector3f color;

	public Light(Vec3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}

	public Vec3f getPosition() {
		return position;
	}

	public void setPosition(Vec3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

}
