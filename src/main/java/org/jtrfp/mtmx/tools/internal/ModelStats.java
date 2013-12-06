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

import org.jtrfp.jtrfp.bin.IBinData;
import org.jtrfp.jtrfp.bin.IBinVertex;

public class ModelStats {

	private final IBinData binData;

	private int minX = 0;

	private int maxX = 0;

	private int minY = 0;

	private int maxY = 0;

	private int minZ = 0;

	private int maxZ = 0;

	private int maxLength;

	private float scale;

	public ModelStats(IBinData binData) {
		this.binData = binData;

		compute();
	}

	private void compute() {
		IBinVertex[] vertexes = binData.getVertexes();
		for (IBinVertex vertex : vertexes) {
			if (vertex.getX() > maxX) {
				maxX = vertex.getX();
			}
			if (vertex.getY() > maxY) {
				maxY = vertex.getY();
			}
			if (vertex.getZ() > maxZ) {
				maxZ = vertex.getZ();
			}
			if (vertex.getX() < minX) {
				minX = vertex.getX();
			}
			if (vertex.getY() < minY) {
				minY = vertex.getY();
			}
			if (vertex.getZ() < minZ) {
				minZ = vertex.getZ();
			}

			maxLength = Math.max((maxZ - minZ), Math.max((maxY - minY), (maxX - minX)));
			scale = 2.0f / maxLength;
		}
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMinZ() {
		return minZ;
	}

	public int getMaxZ() {
		return maxZ;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public float getScale() {
		return scale;
	}

}
