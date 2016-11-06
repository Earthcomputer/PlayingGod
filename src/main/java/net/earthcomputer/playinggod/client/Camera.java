package net.earthcomputer.playinggod.client;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.earthcomputer.playinggod.entity.Player;
import net.earthcomputer.playinggod.util.MathUtils;
import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;
import net.earthcomputer.playinggod.util.Vec3f;

@SideOnly(Side.CLIENT)
public class Camera implements IKeyboardListener {

	private static Camera instance;

	private float distFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private float thirdPersonPitch = 20;

	private boolean firstPersonMode = false;

	private float firstPersonZoom = 0;

	private Vec3f position = Vec3f.origin();
	private float pitch;
	private float yaw;
	private float roll;

	private Player player;

	public static Camera getInstance() {
		return instance;
	}

	public Camera(Player player) {
		instance = this;
		this.player = player;
	}

	public Vec3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public boolean isFirstPersonMode() {
		return firstPersonMode;
	}

	public void move() {
		if (firstPersonMode) {
			checkFirstPersonZoom();
			checkFirstPersonPitch();
			checkFirstPersonYaw();
			float horizontalDist = calcFirstPersonHDistFromPlayer();
			float verticalDist = calcFirstPersonVDistFromPlayer();
			calcAndSetCameraPosition(horizontalDist, verticalDist, 180 - yaw);
		} else {
			checkZoom();
			checkPitch();
			checkAngleAroundPlayer();
			yaw = 180 - (player.getRotY() + angleAroundPlayer);
			pitch = thirdPersonPitch;
			float horizontalDist = calcHorizontalDistFromPlayer();
			float verticalDist = calcVerticalDistFromPlayer();
			calcAndSetCameraPosition(horizontalDist, verticalDist, player.getRotY());
		}
	}

	@Override
	public void onKeyPress(char typedChar, int keyCode, boolean repeat) {
		if (keyCode == Keyboard.KEY_F5) {
			if (firstPersonMode) {
				firstPersonMode = false;
			} else {
				firstPersonMode = true;
				yaw = 180 - player.getRotY();
				pitch = 0;
			}
		}
	}

	@Override
	public void onKeyRelease(char typedChar, int keyCode, boolean repeat) {
		// nop
	}

	private void checkFirstPersonZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		firstPersonZoom += zoomLevel;
		if (firstPersonZoom < 0) {
			firstPersonZoom = 0;
		} else if (firstPersonZoom > 500) {
			firstPersonZoom = 500;
		}
	}

	private void checkZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distFromPlayer -= zoomLevel;
		if (distFromPlayer < 10) {
			distFromPlayer = 10;
		} else if (distFromPlayer > 100) {
			distFromPlayer = 100;
		}
	}

	private void checkFirstPersonPitch() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			pitch -= 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			pitch += 0.2f;
		}
		if (pitch < -90) {
			pitch = -90;
		} else if (pitch > 90) {
			pitch = 90;
		}
	}

	private void checkPitch() {
		if (Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			thirdPersonPitch -= pitchChange;
			if (thirdPersonPitch < -90) {
				thirdPersonPitch = -90;
			} else if (thirdPersonPitch > 90) {
				thirdPersonPitch = 90;
			}
		}
	}

	private void checkFirstPersonYaw() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			yaw -= 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			yaw += 0.2f;
		}
	}

	private void checkAngleAroundPlayer() {
		if (Mouse.isButtonDown(0)) {
			float yawChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= yawChange;
		}
	}

	private float calcFirstPersonHDistFromPlayer() {
		return -(firstPersonZoom * MathUtils.cos(pitch));
	}

	private float calcHorizontalDistFromPlayer() {
		return distFromPlayer * MathUtils.cos(pitch);
	}

	private float calcFirstPersonVDistFromPlayer() {
		return -(firstPersonZoom * MathUtils.sin(pitch));
	}

	private float calcVerticalDistFromPlayer() {
		return distFromPlayer * MathUtils.sin(pitch);
	}

	private void calcAndSetCameraPosition(float horizontalDist, float verticalDist, float hRotation) {
		float theta = hRotation + angleAroundPlayer;
		float offsetX = horizontalDist * MathUtils.sin(theta);
		float offsetZ = horizontalDist * MathUtils.cos(theta);

		Vec3f playerPos = player.getPosition();
		position.set(playerPos.getX() - offsetX, playerPos.getY() + verticalDist + 5, playerPos.getZ() - offsetZ);
	}

}
