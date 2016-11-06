package net.earthcomputer.playinggod.entity;

import org.lwjgl.input.Keyboard;

import net.earthcomputer.playinggod.client.Camera;
import net.earthcomputer.playinggod.client.DisplayManager;
import net.earthcomputer.playinggod.universe.world.Terrain;
import net.earthcomputer.playinggod.util.MathUtils;
import net.earthcomputer.playinggod.util.Vec3f;

public class Player extends Entity {

	private static final float WALK_SPEED = 0.02f;
	private static final float SPRINT_SPEED = 0.05f;
	private static final float TURN_SPEED = 0.16f;
	private static final float GRAVITY = -0.0001f;
	private static final float JUMP_POWER = 0.04f;

	private float speed = 0;
	private float turnSpeed = 0;
	private float verticalSpeed = 0;

	private boolean isInAir = false;

	public Player(Vec3f position, float rotX, float rotY, float rotZ, float scale) {
		super(position, rotX, rotY, rotZ, scale);
	}

	public void move() {
		checkInputs();
		increaseRotation(0, turnSpeed * DisplayManager.getFrameTimeMillis(), 0);
		float distance = speed * DisplayManager.getFrameTimeMillis();
		float dx = distance * MathUtils.sin(getRotY());
		float dz = distance * MathUtils.cos(getRotY());
		increasePosition(dx, 0, dz);

		verticalSpeed += GRAVITY * DisplayManager.getFrameTimeMillis();
		increasePosition(0, verticalSpeed * DisplayManager.getFrameTimeMillis(), 0);
		
		float terrainHeight = Terrain.getExactHeight(getPosition().getX(), getPosition().getZ());
		if (getPosition().getY() < terrainHeight) {
			getPosition().setY(terrainHeight);
			verticalSpeed = 0;
			isInAir = false;
		}
	}

	private void jump() {
		if (!isInAir) {
			verticalSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs() {
		if (Camera.getInstance().isFirstPersonMode()) {
			return;
		}

		boolean wDown = Keyboard.isKeyDown(Keyboard.KEY_W);
		boolean sDown = Keyboard.isKeyDown(Keyboard.KEY_S);
		boolean sprinting = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		if (wDown ^ sDown) {
			if (wDown) {
				speed = sprinting ? SPRINT_SPEED : WALK_SPEED;
			} else {
				speed = sprinting ? -SPRINT_SPEED : -WALK_SPEED;
			}
		} else {
			speed = 0;
		}

		boolean aDown = Keyboard.isKeyDown(Keyboard.KEY_A);
		boolean dDown = Keyboard.isKeyDown(Keyboard.KEY_D);
		if (aDown ^ dDown) {
			if (aDown) {
				turnSpeed = TURN_SPEED;
			} else {
				turnSpeed = -TURN_SPEED;
			}
		} else {
			turnSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}

}
