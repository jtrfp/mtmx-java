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
package org.jtrfp.mtmx.internal.world;

import org.jtrfp.jtrfp.clr.IClrData;
import org.jtrfp.jtrfp.lvl.ILvlData;
import org.jtrfp.jtrfp.sit.ISitData;
import org.jtrfp.jtrfp.tex.ITexData;
import org.jtrfp.mtmx.terrain.ITerrainMap;
import org.jtrfp.mtmx.world.IWorld;


public class World implements IWorld {

	private ITerrainMap terrainMap;

	private ITexData texData;

	private ILvlData lvlData;

	private ISitData sitData;

	private IClrData clrData;

	public World() {
	}

	@Override
	public ITerrainMap getTerrainMap() {
		return terrainMap;
	}

	public void setTerrainMap(ITerrainMap terrainMap) {
		this.terrainMap = terrainMap;
	}

	public void setSitData(ISitData sitData) {
		this.sitData = sitData;
	}

	public void setLvlData(ILvlData lvlData) {
		this.lvlData = lvlData;
	}

	public void setTexData(ITexData texData) {
		this.texData = texData;
	}

	public ITexData getTexData() {
		return texData;
	}

	public ILvlData getLvlData() {
		return lvlData;
	}

	public ISitData getSitData() {
		return sitData;
	}

	public void setClrData(IClrData clrData) {
		this.clrData = clrData;
	}

	public IClrData getClrData() {
		return clrData;
	}
}
