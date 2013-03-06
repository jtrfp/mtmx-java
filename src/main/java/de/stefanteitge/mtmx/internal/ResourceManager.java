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

import de.stefanteitge.mtmx.Engine;
import de.stefanteitge.mtmx.IResourceManager;
import de.stefanteitge.mtmx.ITextureManager;
import de.stefanteitge.mtmx.internal.model.ModelManager;
import de.stefanteitge.mtmx.internal.truck.TruckManager;
import de.stefanteitge.mtmx.internal.world.WorldManager;
import de.stefanteitge.mtmx.model.IModelManager;
import de.stefanteitge.mtmx.truck.ITruckManager;
import de.stefanteitge.mtmx.world.IWorldManager;

public class ResourceManager implements IResourceManager {

	private final IWorldManager worldManager;

	private final ITextureManager textureManager;

	private final TruckManager truckManager;

	private final IModelManager modelManager;

	public ResourceManager(Engine engine) {
		worldManager = new WorldManager(engine);
		textureManager = new TextureManager(engine.getGameDir());
		truckManager = new TruckManager(engine);
		modelManager = new ModelManager(engine);
	}

	public IWorldManager getWorldManager() {
		return worldManager;
	}

	public ITextureManager getTextureManager() {
		return textureManager;
	}

	@Override
	public ITruckManager getTruckManager() {
		return truckManager;
	}

	@Override
	public IModelManager getModelManager() {
		return modelManager;
	}
}
