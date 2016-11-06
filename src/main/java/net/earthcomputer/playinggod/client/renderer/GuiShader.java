package net.earthcomputer.playinggod.client.renderer;

import org.lwjgl.util.vector.Matrix4f;

import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiShader extends ShaderProgram {

	private static final String VERTEX_RESOURCE = "shaders/vertex/gui.vert";
	private static final String FRAGMENT_RESOURCE = "shaders/fragment/gui.frag";
	
	private int loc_transformationMatrix;
	
	public GuiShader() {
		super(VERTEX_RESOURCE, FRAGMENT_RESOURCE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		loadMatrix(loc_transformationMatrix, matrix);
	}

}
