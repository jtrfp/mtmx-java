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
package de.stefanteitge.mtmx.internal;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jtrfp.common.clr.IClrData;

import org.lwjgl.util.vector.Vector3f;

import de.stefanteitge.mtmx.EngineUtil;
import de.stefanteitge.mtmx.draw.IDrawer;
import de.stefanteitge.mtmx.terrain.ITerrainMap;

public final class VboUtil {

	private final ITerrainMap map;

	private final IClrData clrData;

	private int[] sizes;

	public VboUtil(ITerrainMap map, IClrData clrData) {
		this.map = map;
		this.clrData = clrData;
	}

	public static int indexBufferSize(ITerrainMap map) {
		final int w = map.getWidth();
		final int h = map.getHeight();
		return (w - 1) * (h - 1) * 2 * 3;
	}

	public IntBuffer createIndexBuffer() {
		final int w = map.getWidth();
		final int h = map.getHeight();

		int[] indexBuffer = new int[indexBufferSize(map)];

		int texCount = clrData.getColorCount();
		sizes = new int[texCount];
		int z = 0;

		for (int c = 0; c < texCount; c++) {
			int size = 0;
			for (int i = 0; i < w - 1; i++) {
				for (int j = 0; j < h - 1; j++) {
					int texNum = clrData.getColorAt(i, j);
					if (texNum == c) {

						final int jw = j * w;
						final int j1w = (j + 1) * w;

						// triangle 1
						indexBuffer[z] = (jw + (i + 1));
						z++;
						indexBuffer[z] = (jw + i);
						z++;
						indexBuffer[z] = (j1w + i);
						z++;

						// triangle 2
						indexBuffer[z] = (j1w + (i + 1));
						z++;
						indexBuffer[z] = (jw + (i + 1));
						z++;
						indexBuffer[z] = (j1w + i);
						z++;

						size++;
					}
				}
			}
			sizes[c] = size * 6;
		}
		int maxIndex = 0;
		for (int i = 0; i < indexBufferSize(map); i++) {
			if (indexBuffer[i] > maxIndex) {
				maxIndex = indexBuffer[i];
			}
		}

		System.out.println(
				"Created index buffer with " + z + " indexes "
				+ " (" +  (z / 3) + " triangles) using " + (maxIndex + 1) + " vertexes.");

		return EngineUtil.createIntBuffer(indexBuffer);
	}

	public FloatBuffer createVertexBuffer() {
		final int w = map.getWidth();
		final int h = map.getHeight();

		float[] vertexBuffer = new float[w * h * 3];
		int z = 0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int e = map.getElevationAt(j, i);

				vertexBuffer[z] = j * IDrawer.WORLD_SCALE_XZ;
				z++;
				vertexBuffer[z] = e * IDrawer.WORLD_SCALE_Y;
				z++;
				vertexBuffer[z] = i * IDrawer.WORLD_SCALE_XZ;
				z++;
			}
		}

		System.out.println(
				"Created vertex buffer with " + z + " coords and " + (z / 3) + " vertexes.");

		return EngineUtil.createFloatBuffer(vertexBuffer);
	}

	public FloatBuffer createNormalBuffer() {
		final int w = map.getWidth();
		final int h = map.getHeight();

		float[] vertexBuffer = new float[w * h * 3];


		int q = 0;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {

				Vector3f sum = new Vector3f(0.0f, 0.0f, 0.0f);

				Vector3f out = new Vector3f(0.0f, 0.0f, 0.0f);
				if (y > 0) {
					int eOut = map.getElevationAt(x, y - 1) - map.getElevationAt(x, y);
					out = new Vector3f(1.0f, eOut, 0.0f);
				}
				Vector3f in = new Vector3f(0.0f, 0.0f, 0.0f);
				if (y < h - 1) {
					int eIn = map.getElevationAt(x, y + 1) - map.getElevationAt(x, y);
					in = new Vector3f(-1.0f, eIn, 0.0f);
				}
				Vector3f left = new Vector3f(0.0f, 0.0f, 0.0f);
				if (x > 0) {
					int eLeft = map.getElevationAt(x - 1, y) - map.getElevationAt(x, y);
					left = new Vector3f(0.0f, eLeft, -1.0f);
				}
				Vector3f right = new Vector3f(0.0f, 0.0f, 0.0f);
				if (x < w - 1) {
					int eRight = map.getElevationAt(x + 1, y) - map.getElevationAt(x, y);
					right = new Vector3f(0.0f, eRight, 1.0f);
				}

				Vector3f res = new Vector3f();
				if (x > 0 && y > 0) {
					Vector3f.cross(out, left, res);
					Vector3f.add(sum, res, sum);
					//					sum += out.cross(left).normalise();
				}
				if (x > 0 && y < h - 1) {
					Vector3f.cross(left, in, res);
					Vector3f.add(sum, res, sum);
					//					sum += left.cross(in).normalise();
				}
				if (x < w - 1 && y < h - 1) {
					Vector3f.cross(in, right, res);
					Vector3f.add(sum, res, sum);
					//					sum += in.cross(right).normalise();
				}
				if (x < w - 1 && y > 0) {
					Vector3f.cross(right, out, res);
					Vector3f.add(sum, res, sum);
					//					sum += right.cross(out).normalise();
				}
				sum.normalise(sum);
				//sum.negate(sum);
				vertexBuffer[q] = sum.x;
				q++;
				vertexBuffer[q] = sum.y;
				q++;
				vertexBuffer[q] = sum.z;
				q++;
			}
		}

		return EngineUtil.createFloatBuffer(vertexBuffer);
	}

	public FloatBuffer createTexCoordBuffer() {
		final int w = map.getWidth();
		final int h = map.getHeight();

		float[] texBuffer = new float[w * h * 2];
		int z = 0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				texBuffer[z] = i * 1.0f;
				z++;
				texBuffer[z] = -j * 1.0f;
				z++;
			}
		}

		return EngineUtil.createFloatBuffer(texBuffer);
	}

	public int[] getSizes() {
		return sizes;
	}
}
