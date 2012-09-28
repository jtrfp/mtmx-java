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
package de.stefanteitge.mtmx.internal.truck;

import jtrfp.common.FileLoadException;
import jtrfp.common.pod.IPodFileEntry;
import jtrfp.common.pod.PodFile;
import jtrfp.common.trk.ITrkData;
import jtrfp.common.trk.ITrkPodFileEntry;
import de.stefanteitge.mtmx.Engine;
import de.stefanteitge.mtmx.EngineException;
import de.stefanteitge.mtmx.model.IModel;
import de.stefanteitge.mtmx.model.IModelManager;
import de.stefanteitge.mtmx.truck.ITruck;
import de.stefanteitge.mtmx.truck.ITruckManager;

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
