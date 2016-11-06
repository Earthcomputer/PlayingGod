package net.earthcomputer.playinggod.client.gui;

import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;
import net.earthcomputer.playinggod.util.Vec2f;

@SideOnly(Side.CLIENT)
public class GuiTexture {

	private int texId;
	private Vec2f position;
	private Vec2f scale;

	public GuiTexture(int texId, Vec2f position, Vec2f scale) {
		this.texId = texId;
		this.position = position;
		this.scale = scale;
	}

	public int getTexId() {
		return texId;
	}

	public Vec2f getPosition() {
		return position;
	}

	public Vec2f getScale() {
		return scale;
	}

}
