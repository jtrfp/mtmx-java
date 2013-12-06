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
package org.jtrfp.mtmx.internal;

import java.util.HashMap;

import org.jtrfp.jtrfp.FileLoadException;
import org.jtrfp.jtrfp.act.DefaultActColorTable;
import org.jtrfp.jtrfp.act.IActData;
import org.jtrfp.jtrfp.act.IActPodFileEntry;
import org.jtrfp.jtrfp.game.ITriGameDir;
import org.jtrfp.jtrfp.pod.PodFile;
import org.jtrfp.jtrfp.raw.IRawPodFileEntry;
import org.jtrfp.jtrfp.raw.RawImage;
import org.jtrfp.mtmx.EngineException;
import org.jtrfp.mtmx.ITextureManager;
import org.jtrfp.mtmx.texture.ITexture;


public class TextureManager implements ITextureManager {

	private final TextureLoader textureLoader;

	private ITexture lastTexture;

	private String lastTextureName;

	private final HashMap<String, ITexture> textures;

	private final ITriGameDir gameDir;

	public TextureManager(ITriGameDir gameDir) {
		this.gameDir = gameDir;
		textureLoader = new TextureLoader();
		textures = new HashMap<String, ITexture>();
	}

	@Override
	public ITexture load(PodFile podFile, String textureName) throws EngineException {

		ITexture mapTexture = textures.get(textureName);
		if (mapTexture != null) {
			return mapTexture;
		}

		System.out.println("Loading " + textureName); // XXX
		try {
			String path = "art\\" + textureName;
			IRawPodFileEntry rawEntry = (IRawPodFileEntry) podFile.getData().findEntry(path);
			IActPodFileEntry actEntry = gameDir.getActSearchStrategy().find(rawEntry);

			IActData colorTable = null;
			if (actEntry == null) {
				colorTable = new DefaultActColorTable(256);
			} else {
				colorTable = actEntry.getData();
			}

			RawImage rawImage = new RawImage(rawEntry.getRawData(), colorTable);
			lastTexture = textureLoader.load(rawImage);
			lastTextureName = textureName;
			textures.put(textureName, lastTexture);

			return lastTexture;
		} catch (FileLoadException e) {
			throw new EngineException(e);
		}
	}

	@Override
	public ITexture get(String textureName) {
		if (textureName.equals(lastTextureName)) {
			return lastTexture;
		}
		ITexture mapTexture = textures.get(textureName);
		if (mapTexture != null) {
			lastTexture = mapTexture;
			lastTextureName = textureName;
			return mapTexture;
		}
		return null;
	}


}
