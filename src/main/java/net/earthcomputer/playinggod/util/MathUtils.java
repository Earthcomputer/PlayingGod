package net.earthcomputer.playinggod.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.earthcomputer.playinggod.client.Camera;

public class MathUtils {

	private MathUtils() {
	}

	private static final float[] CACHED_SIN = new float[65536];

	static {
		for (int i = 0; i < 65536; i++) {
			CACHED_SIN[i] = (float) Math.sin((float) i / 65536f * ((float) Math.PI / 2f));
		}
	}

	public static float sin(float n) {
		// modulo doesn't work with negatives
		if (n < 0) {
			return -sin(-n);
		}

		// wrap n to 360
		n %= 360;

		// index in table, may be out of bounds
		int index = (int) (n * 65536f) / 90;

		if (index < 65536) { // 0 <= n < 90
			return CACHED_SIN[index];

		} else if (index < 65536 * 2) { // 90 <= n < 180

			if (index == 65536) { // n = 90
				return 1;

			} else { // 90 < n < 180
				return CACHED_SIN[(65536 * 2) - index];
			}

		} else if (index < 65536 * 3) { // 180 <= n < 270
			return -CACHED_SIN[index - (65536 * 2)];

		} else { // 270 <= n < 360

			if (index == 65536 * 3) { // n = 270
				return -1;

			} else { // 270 < n < 360
				return -CACHED_SIN[(65536 * 4) - index];
			}
		}
	}

	public static float cos(float n) {
		return sin(n + 90);
	}

	public static float barryCentric(Vec3f p1, Vec3f p2, Vec3f p3, Vec2f pos) {
		float det = (p2.getZ() - p3.getZ()) * (p1.getX() - p3.getX())
				+ (p3.getX() - p2.getX()) * (p1.getZ() - p3.getZ());
		float l1 = (p2.getZ() - p3.getZ()) * (pos.getX() - p3.getX())
				+ (p3.getX() - p2.getX()) * (pos.getY() - p3.getZ()) / det;
		float l2 = (p3.getZ() - p1.getZ()) * (pos.getX() - p3.getX())
				+ (p1.getX() - p3.getX()) * (pos.getY() - p3.getZ()) / det;
		float l3 = 1f - l1 - l2;
		return l1 * p1.getY() + l2 * p2.getY() + l3 * p3.getY();
	}

	@SideOnly(Side.CLIENT)
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		mat.translate(translation);
		mat.scale(new Vector3f(scale.x, scale.y, 1));
		return mat;
	}

	@SideOnly(Side.CLIENT)
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		mat.translate(translation);
		mat.rotate((float) Math.toRadians(rx), vecXAxis());
		mat.rotate((float) Math.toRadians(ry), vecYAxis());
		mat.rotate((float) Math.toRadians(rz), vecZAxis());
		mat.scale(new Vector3f(scale, scale, scale));
		return mat;
	}

	@SideOnly(Side.CLIENT)
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		mat.rotate((float) Math.toRadians(camera.getPitch()), vecXAxis());
		mat.rotate((float) Math.toRadians(camera.getYaw()), vecYAxis());
		mat.rotate((float) Math.toRadians(camera.getRoll()), vecZAxis());
		Vec3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());
		mat.translate(negativeCameraPos);
		return mat;
	}

	@SideOnly(Side.CLIENT)
	public static Vector3f vecXAxis() {
		return new Vector3f(1, 0, 0);
	}

	@SideOnly(Side.CLIENT)
	public static Vector3f vecYAxis() {
		return new Vector3f(0, 1, 0);
	}

	@SideOnly(Side.CLIENT)
	public static Vector3f vecZAxis() {
		return new Vector3f(0, 0, 1);
	}

}
