package net.earthcomputer.playinggod.client.renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.earthcomputer.playinggod.util.Side;
import net.earthcomputer.playinggod.util.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ShaderProgram {

	private static final List<ShaderProgram> existingShaders = new ArrayList<ShaderProgram>();

	private final int programId;
	private final int vertexShaderId;
	private final int fragmentShaderId;

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public ShaderProgram(String vertexResource, String fragmentResource) {
		vertexShaderId = loadShader(vertexResource, GL20.GL_VERTEX_SHADER);
		fragmentShaderId = loadShader(fragmentResource, GL20.GL_FRAGMENT_SHADER);
		programId = GL20.glCreateProgram();
		GL20.glAttachShader(programId, vertexShaderId);
		GL20.glAttachShader(programId, fragmentShaderId);
		bindAttributes();
		GL20.glLinkProgram(programId);
		GL20.glValidateProgram(programId);
		getAllUniformLocations();

		existingShaders.add(this);
	}

	protected abstract void getAllUniformLocations();

	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programId, uniformName);
	}

	public void start() {
		GL20.glUseProgram(programId);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public static void cleanUp() {
		GL20.glUseProgram(0);
		for (ShaderProgram program : existingShaders) {
			GL20.glDetachShader(program.programId, program.vertexShaderId);
			GL20.glDetachShader(program.programId, program.fragmentShaderId);
			GL20.glDeleteShader(program.vertexShaderId);
			GL20.glDeleteShader(program.fragmentShaderId);
			GL20.glDeleteProgram(program.programId);
		}
	}

	protected abstract void bindAttributes();

	protected void bindAttribute(int attr, String variableName) {
		GL20.glBindAttribLocation(programId, attr, variableName);
	}

	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}

	protected void loadVector(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.getX(), vector.getY());
	}

	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.getX(), vector.getY(), vector.getZ());
	}

	protected void loadBoolean(int location, boolean value) {
		loadFloat(location, value ? GL11.GL_TRUE : GL11.GL_FALSE);
	}

	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}

	private static int loadShader(String resourceName, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(ShaderProgram.class.getResourceAsStream("/assets/" + resourceName)));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		int shaderId = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderId, shaderSource);
		GL20.glCompileShader(shaderId);
		if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println(GL20.glGetShaderInfoLog(shaderId, 500));
			System.err.println("Could not compile shader");
			throw new RuntimeException();
		}
		return shaderId;
	}

}
