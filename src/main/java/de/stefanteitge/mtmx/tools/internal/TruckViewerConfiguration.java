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
package de.stefanteitge.mtmx.tools.internal;

import java.nio.FloatBuffer;

import jtrfp.common.pod.PodFile;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import de.stefanteitge.mtmx.AbstractEngineConfiguration;
import de.stefanteitge.mtmx.EngineException;
import de.stefanteitge.mtmx.draw.TruckDrawer;
import de.stefanteitge.mtmx.truck.ITruck;
import de.stefanteitge.mtmx.truck.ITruckManager;

public class TruckViewerConfiguration extends AbstractEngineConfiguration {

	private final PodFile podFile;

	private final String truckPath;

	private TruckDrawer truckDrawer;

	private float rot = 0.0f;

	private float rot2 = 0.0f;

	public TruckViewerConfiguration(PodFile podFile, String truckPath) {
		this.podFile = podFile;
		this.truckPath = truckPath;
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

		truckDrawer.draw();

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
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
					truckDrawer.setSteerAngle(truckDrawer.getSteerAngle() + 0.1f);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D) {
					truckDrawer.setSteerAngle(truckDrawer.getSteerAngle() - 0.1f);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_T) {
					if (truckDrawer.getRenderMode() == GL11.GL_POLYGON) {
						truckDrawer.setRenderMode(GL11.GL_LINE_LOOP);
					} else {
						truckDrawer.setRenderMode(GL11.GL_POLYGON);
					}
				}
			}
		}
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

		GL11.glShadeModel(GL11.GL_FLAT);
	}

	@Override
	public void initResources() throws EngineException {
		ITruckManager tm = getEngine().getResourceManager().getTruckManager();
		ITruck truck = tm.load(podFile, truckPath);
		truckDrawer = new TruckDrawer(getEngine(), truck);
	}

	@Override
	public void initView() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glFrustum(-1.0, 1.0, -1.0, 1.0, 1.5, 25.0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		//GL11.glClearDepth(0.5f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

}
