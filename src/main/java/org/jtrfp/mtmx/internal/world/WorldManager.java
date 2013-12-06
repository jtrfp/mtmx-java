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

import org.jtrfp.jtrfp.FileLoadException;
import org.jtrfp.jtrfp.clr.IClrData;
import org.jtrfp.jtrfp.clr.IClrPodFileEntry;
import org.jtrfp.jtrfp.lvl.ILvlData;
import org.jtrfp.jtrfp.lvl.ILvlPodFileEntry;
import org.jtrfp.jtrfp.lvl.LvlDataKeys;
import org.jtrfp.jtrfp.pod.IPodFileEntry;
import org.jtrfp.jtrfp.pod.PodFile;
import org.jtrfp.jtrfp.raw.IRawData;
import org.jtrfp.jtrfp.raw.IRawPodFileEntry;
import org.jtrfp.jtrfp.sit.ISitData;
import org.jtrfp.jtrfp.sit.ISitPodFileEntry;
import org.jtrfp.jtrfp.sit.SitDataKeys;
import org.jtrfp.jtrfp.tex.ITexData;
import org.jtrfp.jtrfp.tex.ITexPodFileEntry;
import org.jtrfp.mtmx.Engine;
import org.jtrfp.mtmx.EngineException;
import org.jtrfp.mtmx.ITextureManager;
import org.jtrfp.mtmx.terrain.RawDataTerrainMap;
import org.jtrfp.mtmx.world.IWorld;
import org.jtrfp.mtmx.world.IWorldManager;

public class WorldManager implements IWorldManager {

	private World world;
	private final Engine engine;

	public WorldManager(Engine engine) {
		this.engine = engine;
	}

	@Override
	public IWorld getLoadedWorld() {
		return world;
	}

	@Override
	public IWorld load(PodFile podFile, String path) throws EngineException {

		world = new World();

		try {
			ISitPodFileEntry sitEntry = (ISitPodFileEntry) podFile.getData().findEntry(path);
			ISitData sitData = sitEntry.getData();
			world.setSitData(sitData);

			String lvlPath = "levels\\" + sitData.getValue(SitDataKeys.LEVEL_FILE_NAME);
			ILvlPodFileEntry lvlEntry = (ILvlPodFileEntry) podFile.getData().findEntry(lvlPath);
			ILvlData lvlData = lvlEntry.getData();
			world.setLvlData(lvlData);

			// TODO read tex file
			String texPath = "data\\" + lvlData.getValue(LvlDataKeys.TEXTURE_LIST_NAME);
			ITexPodFileEntry texEntry = (ITexPodFileEntry) podFile.getData().findEntry(texPath);
			ITexData texData = texEntry.getData();
			world.setTexData(texData);

			String clrPath = "data\\" + world.getLvlData().getValue(LvlDataKeys.COLOR_MAP_NAME);
			IClrPodFileEntry crlEntry = (IClrPodFileEntry) podFile.getData().findEntry(clrPath);
			IClrData clrData = crlEntry.getData();
			world.setClrData(clrData);

			String rawPath = "data\\" + lvlData.getValue(LvlDataKeys.HEIGHT_MAP_NAME);
			IPodFileEntry entry = podFile.getData().findEntry(rawPath);
			IRawData rawData = ((IRawPodFileEntry) entry).getRawData();
			world.setTerrainMap(new RawDataTerrainMap(rawData));

			String[] textureNames = texData.getTextureNames();
			ITextureManager tm = engine.getResourceManager().getTextureManager();
			for (String textureName : textureNames) {
				tm.load(podFile, textureName);
			}
		} catch (FileLoadException e) {
			world = null;
			throw new EngineException("Failed loading world.", e);
		}

		return world;
	}

	@Override
	public IWorld load(ISitPodFileEntry sitEntry) throws EngineException {
		return load(sitEntry.getPodFile(), sitEntry.getPath());
	}
}
