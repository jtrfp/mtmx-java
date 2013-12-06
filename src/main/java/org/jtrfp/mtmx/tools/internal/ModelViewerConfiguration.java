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
package org.jtrfp.mtmx.tools.internal;

import java.nio.FloatBuffer;

import org.jtrfp.jtrfp.bin.IBinData;
import org.jtrfp.jtrfp.pod.PodFile;
import org.jtrfp.mtmx.AbstractEngineConfiguration;
import org.jtrfp.mtmx.EngineException;
import org.jtrfp.mtmx.draw.ModelDrawer;
import org.jtrfp.mtmx.model.IModel;
import org.jtrfp.mtmx.model.IModelManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class ModelViewerConfiguration extends AbstractEngineConfiguration {

	private float rot = 0.0f;

	private float rot2 = 0.0f;

	private int renderMode = GL11.GL_POLYGON;

	private ModelDrawer drawer;

	private final PodFile podFile;

	private final String path;

	private IBinData binData;

	public ModelViewerConfiguration(PodFile podFile, String modelPath) {
		this.podFile = podFile;
		path = modelPath;
	}

	@Override
	public void draw() {
		GL11.glClearColor(0.8f, 0.8f, 0.8f, 0.0f);

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

		GL11.glLoadIdentity();
		GLU.gluLookAt(5.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		GL11.glRotatef(rot , 0.0f, 1.0f, 0.0f);
		GL11.glRotatef(rot2 , 1.0f, 0.0f, 0.0f);
		drawer.draw();

		Keyboard.poll();

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					return;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
					rot -= 15.0f;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
					rot += 15.0f;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
					rot2 -= 15.0f;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
					rot2 += 15.0f;
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_T) {
					if (renderMode == GL11.GL_POLYGON) {
						renderMode = GL11.GL_LINE_LOOP;
					} else {
						renderMode = GL11.GL_POLYGON;
					}
				}
			}
		}
	}

	@Override
	public void initView() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glFrustum(-1.0, 1.0, -1.0, 1.0, 1.0, 40.0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		//GL11.glClearDepth(0.5f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public void initLight() {
		GL11.glEnable(GL11.GL_LIGHTING);

		float[] vec = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
		// XXX: FloatBuffer must have 4 additional floats free
		FloatBuffer diffBuffer = BufferUtils.createFloatBuffer(vec.length + 4);
		diffBuffer.put(vec);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT_AND_DIFFUSE, diffBuffer);
		GL11.glEnable(GL11.GL_LIGHT0);
	}

	@Override
	public void initResources() throws EngineException  {
		IModelManager mm = getEngine().getResourceManager().getModelManager();
		IModel model = mm.load(podFile, path);
		binData = model.getBinData();
		printModelStats();
		drawer = new ModelDrawer(getEngine(), model);
	}

	private void printModelStats() {
		System.out.println("Vertex count: " + binData.getVertexCount());
		System.out.println("Face count: " + binData.getFaces().length);
		System.out.println("Normals count: " + binData.getNormals().length);
		ModelStats modelStats = new ModelStats(binData);
		System.out.println("Min X: " + modelStats.getMinX());
		System.out.println("Max X: " + modelStats.getMaxX());
		System.out.println("Min Y: " + modelStats.getMinY());
		System.out.println("Max Y: " + modelStats.getMaxY());
		System.out.println("Min Z: " + modelStats.getMinZ());
		System.out.println("Max Z: " + modelStats.getMaxZ());
		System.out.println("Max length: " + modelStats.getMaxLength());
		System.out.println("Scale: " + modelStats.getScale());
	}
}
