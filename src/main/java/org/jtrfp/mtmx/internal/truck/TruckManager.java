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
package org.jtrfp.mtmx.internal.truck;

import org.jtrfp.jtrfp.FileLoadException;
import org.jtrfp.jtrfp.pod.IPodFileEntry;
import org.jtrfp.jtrfp.pod.PodFile;
import org.jtrfp.jtrfp.trk.ITrkData;
import org.jtrfp.jtrfp.trk.ITrkPodFileEntry;
import org.jtrfp.mtmx.Engine;
import org.jtrfp.mtmx.EngineException;
import org.jtrfp.mtmx.model.IModel;
import org.jtrfp.mtmx.model.IModelManager;
import org.jtrfp.mtmx.truck.ITruck;
import org.jtrfp.mtmx.truck.ITruckManager;

public class TruckManager implements ITruckManager {

	private static final String PATH_PREFIX = "models\\";
	private final Engine engine;

	public TruckManager(Engine engine) {
		this.engine = engine;
	}

	@Override
	public ITruck load(PodFile podFile, String trkPath) throws EngineException {
		Truck truck = new Truck();
		try {
			// load TRK data
			IPodFileEntry entry = podFile.getData().findEntry(trkPath);
			if (entry == null || !(entry instanceof ITrkPodFileEntry)) {
				throw new EngineException("Truck '" + trkPath + " does not exist.");
			}

			ITrkData trkData = ((ITrkPodFileEntry) entry).getData();
			truck.setTruckData(trkData);

			IModelManager mm = engine.getResourceManager().getModelManager();

			// load body model
			IModel body = mm.load(podFile, PATH_PREFIX + trkData.getTruckModelBaseName() + ".bin");
			truck.setBody(body);

			// load axle model
			IModel axle = mm.load(podFile, PATH_PREFIX + trkData.getAxleModelName());
			truck.setAxle(axle);

			// load left tire model
			String leftTirePath = PATH_PREFIX + trkData.getTireModelBaseName() + "16R.bin"; // XXX
			IModel leftTire = mm.load(podFile, leftTirePath);
			truck.setLeftTire(leftTire);

			// load right tire model
			String rightTirePath = PATH_PREFIX + trkData.getTireModelBaseName() + "16L.bin"; // XXX
			IModel rightTire = mm.load(podFile, rightTirePath);
			truck.setRightTire(rightTire);

			// load drive shaft model
			IModel drvShaft = mm.load(podFile, "models\\drvshaft.bin");
			truck.setDriveShaft(drvShaft);

		} catch (FileLoadException e) {
			throw new EngineException(e);
		}
		return truck;
	}

	@Override
	public ITruck load(ITrkPodFileEntry trkEntry) throws EngineException {
		return load(trkEntry.getPodFile(), trkEntry.getPath());
	}
}
