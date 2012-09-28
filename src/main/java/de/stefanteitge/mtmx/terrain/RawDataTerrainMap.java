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
package de.stefanteitge.mtmx.terrain;

import jtrfp.common.raw.IRawData;

public class RawDataTerrainMap implements ITerrainMap {

	private IRawData rawData;
	
	private int maxElevation;
	
	public RawDataTerrainMap(IRawData rawData) {
		super();
		this.rawData = rawData;
		
		computeMaxElevation();
	}

	@Override
	public int getElevationAt(int x, int y) {
		// TODO: check bounds
		return rawData.getValueAt(x, y);
	}

	@Override
	public int getHeight() {
		return rawData.getHeight();
	}

	@Override
	public int getMaxElevation() {
		return maxElevation;
	}

	@Override
	public int getWidth() {
		return rawData.getWidth();
	}

	protected void computeMaxElevation() {
		int max = 0;
		for (int i = 0; i < getWidth(); i++) {
			for (int j = 0; j < getHeight(); j++) {
				int is = Math.abs(getElevationAt(i, j));
				if (is > max) {
					max = is;
				}
			}
		}
		maxElevation = max;
	}
}
