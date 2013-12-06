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

import org.jtrfp.jtrfp.pod.PodFile;
import org.jtrfp.mtmx.AbstractEngineConfiguration;
import org.jtrfp.mtmx.EngineException;
import org.jtrfp.mtmx.EngineUtil;
import org.jtrfp.mtmx.draw.IDrawer;
import org.jtrfp.mtmx.draw.TerrainDrawer;
import org.jtrfp.mtmx.world.IWorld;
import org.jtrfp.mtmx.world.IWorldManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class WorldViewerConfiguration extends AbstractEngineConfiguration {

	private final PodFile podFile;

	private final String sitPath;

	private IWorld world;

	private int lookX = 1;

	private int lookZ = 1;

	private TerrainDrawer terrainDrawer;

	public WorldViewerConfiguration(PodFile podFile, String sitPath) {
		this.podFile = podFile;
		this.sitPath = sitPath;
	}

	@Override
	public void draw() {
		GL11.glClearColor(0.8f, 0.8f, 0.8f, 0.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();

		float e = terrainDrawer.getTerrainMap().getElevationAt(1, 1) * IDrawer.WORLD_SCALE_XZ;

		GLU.gluLookAt(
				-1.0f + lookX * IDrawer.WORLD_SCALE_XZ,
				1.0f + e,
				-1.0f + lookZ * IDrawer.WORLD_SCALE_XZ,
				0.0f + lookX * IDrawer.WORLD_SCALE_XZ,
				0.0f + e,
				0.0f + lookZ * IDrawer.WORLD_SCALE_XZ,
				0.0f,
				1.0f,
				0.0f);

		GL11.glColor3f(0.0f, 1.0f, 0.0f);
		terrainDrawer.draw();

		Keyboard.poll();

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					return;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
					lookX -= 5;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
					lookX += 5;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
					lookZ += 5;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
					lookZ -= 5;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_T) {
					if (terrainDrawer.getRenderMode() == GL11.GL_TRIANGLES) {
						terrainDrawer.setRenderMode(GL11.GL_LINE_LOOP);
					} else {
						terrainDrawer.setRenderMode(GL11.GL_TRIANGLES);
					}
				}
			}
		}
	}

	@Override
	public void initLight() {
		GL11.glEnable(GL11.GL_LIGHTING);

		// XXX: FloatBuffer must have 4 additional floats free
		float[] amb = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f};
		FloatBuffer ambientBuffer = EngineUtil.createFloatBuffer(amb);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, ambientBuffer);

		// XXX: FloatBuffer must have 4 additional floats free
		float[] vec = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f};
		FloatBuffer diffBuffer = EngineUtil.createFloatBuffer(vec);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, diffBuffer);
		GL11.glEnable(GL11.GL_LIGHT0);

		GL11.glShadeModel(GL11.GL_FLAT);
	}

	@Override
	public void initResources() throws EngineException {
		IWorldManager wm = getEngine().getResourceManager().getWorldManager();
		world = wm.load(podFile, sitPath);
		terrainDrawer = new TerrainDrawer(getEngine(), world);
	}

	@Override
	public void initView() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glFrustum(-1.0, 1.0, -1.0, 1.0, 5.0f, 150 * IDrawer.WORLD_SCALE_XZ);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

}
