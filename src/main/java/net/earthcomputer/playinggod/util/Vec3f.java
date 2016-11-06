package net.earthcomputer.playinggod.util;

import org.lwjgl.util.vector.Vector3f;

public class Vec3f {

	private float x;
	private float y;
	private float z;

	public Vec3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static Vec3f origin() {
		return new Vec3f(0, 0, 0);
	}
	
	public float lengthSquared() {
		return x * x + y * y + z * z;
	}
	
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}
	
	public void normalize() {
		float length = length();
		x /= length;
		y /= length;
		z /= length;
	}

	@SideOnly(Side.CLIENT)
	public Vector3f toGLVector() {
		return new Vector3f(x, y, z);
	}

	@SideOnly(Side.CLIENT)
	public static Vec3f fromGLVector(Vector3f vector) {
		return new Vec3f(vector.x, vector.y, vector.z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vec3f))
			return false;
		Vec3f other = (Vec3f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

}
