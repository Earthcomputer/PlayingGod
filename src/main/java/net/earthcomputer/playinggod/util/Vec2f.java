package net.earthcomputer.playinggod.util;

import org.lwjgl.util.vector.Vector2f;

public class Vec2f {

	private float x;
	private float y;

	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
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
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public static Vec2f origin() {
		return new Vec2f(0, 0);
	}
	
	public float lengthSquared() {
		return x * x + y * y;
	}
	
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}
	
	public void normalize() {
		float length = length();
		x /= length;
		y /= length;
	}
	
	@SideOnly(Side.CLIENT)
	public Vector2f toGLVector() {
		return new Vector2f(x, y);
	}
	
	@SideOnly(Side.CLIENT)
	public static Vec2f fromGLVector(Vector2f vector) {
		return new Vec2f(vector.x, vector.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vec2f))
			return false;
		Vec2f other = (Vec2f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

}
