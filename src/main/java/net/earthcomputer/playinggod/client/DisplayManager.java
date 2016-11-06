package net.earthcomputer.playinggod.client;

import java.util.Collections;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import net.earthcomputer.playinggod.client.gui.GuiTexture;
import net.earthcomputer.playinggod.client.model.ModelLoader;
import net.earthcomputer.playinggod.client.renderer.GuiRenderer;
import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;
import net.earthcomputer.playinggod.util.Vec2f;

@SideOnly(Side.CLIENT)
public class DisplayManager {

	private DisplayManager() {
	}

	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 720;
	public static final int MAX_FPS = 120;

	private static long lastFrameTime;
	private static long delta;

	public static void createDisplay() {
		ContextAttribs attr = new ContextAttribs(3, 0).withForwardCompatible(true);

		try {
			Display.setDisplayMode(new DisplayMode(DEFAULT_WIDTH, DEFAULT_HEIGHT));
			Display.create(new PixelFormat(), attr);
			Display.setTitle("Playing God");
		} catch (LWJGLException e) {
			JOptionPane.showMessageDialog(null, "You must have OpenGL version 3.0 or greater", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw new RuntimeException("Failed to set up the display", e);
		}

		GL11.glViewport(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		lastFrameTime = getCurrentTime();
	}

	public static void drawLogo(ModelLoader loader) {
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		guiRenderer.render(Collections.singletonList(new GuiTexture(loader.loadTexture("logo"), new Vec2f(0, 0),
				new Vec2f(Display.getWidth() / 640f, Display.getHeight() / 480f))));
	}

	public static void updateDisplay() {
		Display.sync(MAX_FPS);
		Display.update();

		long currentFrameTime = getCurrentTime();
		delta = currentFrameTime - lastFrameTime;
		lastFrameTime = currentFrameTime;
	}

	public static long getFrameTimeMillis() {
		return delta;
	}

	public static void closeDisplay() {
		Display.destroy();
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

}
