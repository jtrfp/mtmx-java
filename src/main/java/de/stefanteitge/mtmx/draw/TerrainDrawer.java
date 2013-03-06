/*
 * This file is part of mtmX.
 *
 * mtmX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mtmX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mtmX.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.stefanteitge.mtmx.draw;

import static org.lwjgl.opengl.ARBBufferObject.GL_STATIC_DRAW_ARB;
import static org.lwjgl.opengl.ARBBufferObject.glBindBufferARB;
import static org.lwjgl.opengl.ARBBufferObject.glBufferDataARB;
import static org.lwjgl.opengl.ARBBufferObject.glGenBuffersARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glVertexPointer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import de.stefanteitge.mtmx.Engine;
import de.stefanteitge.mtmx.ITextureManager;
import de.stefanteitge.mtmx.internal.VboUtil;
import de.stefanteitge.mtmx.terrain.ITerrainMap;
import de.stefanteitge.mtmx.texture.ITexture;
import de.stefanteitge.mtmx.world.IWorld;

public class TerrainDrawer implements IDrawer {

	private final int vertexBufferId;

	private final int indexBufferId;

	private final int normalBufferId;

	private int renderMode = GL11.GL_TRIANGLES;

	private final int texCoordBufferId;

	private final VboUtil vboUtil;

	private final IWorld world;

	private final Engine engine;

	public TerrainDrawer(Engine engine, IWorld world) {
		this.engine = engine;
		this.world = world;

		enableClientStates();

		vboUtil = new VboUtil(world.getTerrainMap(), world.getClrData());

		// index buffer
		indexBufferId = createVboId();
		bufferElementData(indexBufferId, vboUtil.createIndexBuffer());

		// vertex buffer
		vertexBufferId = createVboId();
		bufferData(vertexBufferId, vboUtil.createVertexBuffer());

		// normal buffer
		normalBufferId = createVboId();
		bufferData(normalBufferId, vboUtil.createNormalBuffer());

		// tex coord
		texCoordBufferId = createVboId();
		bufferData(texCoordBufferId, vboUtil.createTexCoordBuffer());

		disableClientStates();
	}


	public ITerrainMap getTerrainMap() {
		return world.getTerrainMap();
	}

	@Override
	public void draw() {
		glPushMatrix();

		enableClientStates();
		bindBuffers();
		drawTextured();
		disableClientStates();

		glPopMatrix();

		unbindBuffers();
	}

	private void unbindBuffers() {
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
	}

	private void bindBuffers() {
		// normals
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, normalBufferId);
		GL11.glNormalPointer(GL_FLOAT, 0, 0);

		// vertexes
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexBufferId);
		glVertexPointer(3, GL_FLOAT, 0, 0);

		// textures
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, texCoordBufferId);
		GL11.glTexCoordPointer(2, GL_FLOAT, 0, 0);

		// indexes
		glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, indexBufferId);
	}

	private void drawTextured() {
		int[] sizes = vboUtil.getSizes();
		int len = sizes.length;
		long offset = 0;

		String[] textureNames = world.getTexData().getTextureNames();
		ITextureManager textureManager = engine.getResourceManager().getTextureManager();

		for (int i = 0; i < len; i++) {
			int size = sizes[i];
			if (size > 0) {

				String name = textureNames[i];
				ITexture texture = textureManager.get(name);
				texture.bind();

				GL11.glDrawElements(
						renderMode,
						size,
						GL11.GL_UNSIGNED_INT,
						offset * (Integer.SIZE / 8));

				offset += size;
			}
		}
	}

	private void enableClientStates() {
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL11.GL_NORMAL_ARRAY);
		glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	private void disableClientStates() {
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL11.GL_NORMAL_ARRAY);
		glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	public static int createVboId() {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			IntBuffer buffer = BufferUtils.createIntBuffer(1);
			glGenBuffersARB(buffer);
			System.out.println("Created VBO ID: " + buffer.get(0));
			return buffer.get(0);
		}
		return 0;
	}

	public void bufferData(int id, FloatBuffer buffer) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			glBindBufferARB(GL_ARRAY_BUFFER_ARB, id);
			glBufferDataARB(GL_ARRAY_BUFFER_ARB, buffer, GL_STATIC_DRAW_ARB);
		}
	}

	public void bufferElementData(int id, IntBuffer buffer) {
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, id);
			glBufferDataARB(GL_ELEMENT_ARRAY_BUFFER_ARB, buffer, GL_STATIC_DRAW_ARB);
		}
	}

	public int getRenderMode() {
		return renderMode;
	}

	public void setRenderMode(int renderMode) {
		this.renderMode = renderMode;
	}
}
