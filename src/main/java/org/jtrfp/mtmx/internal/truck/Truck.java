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

import org.jtrfp.jtrfp.trk.ITrkData;
import org.jtrfp.mtmx.model.IModel;
import org.jtrfp.mtmx.truck.ITruck;

public class Truck implements ITruck {

	private ITrkData truckData;

	private IModel body;

	private IModel axle;

	private IModel leftTire;

	private IModel rightTire;

	private IModel drvShaft;

	@Override
	public ITrkData getTruckData() {
		return truckData;
	}

	protected void setTruckData(ITrkData truckData) {
		this.truckData = truckData;
	}

	@Override
	public IModel getBody() {
		return body;
	}

	protected void setBody(IModel body) {
		this.body = body;
	}

	@Override
	public IModel getAxle() {
		return axle;
	}

	protected void setAxle(IModel axle) {
		this.axle = axle;
	}

	@Override
	public IModel getLeftTire() {
		return leftTire;
	}

	@Override
	public IModel getRightTire() {
		return rightTire;
	}

	protected void setLeftTire(IModel leftTire) {
		this.leftTire = leftTire;
	}

	protected void setRightTire(IModel rightTire) {
		this.rightTire = rightTire;
	}

	public void setDriveShaft(IModel drvShaft) {
		this.drvShaft = drvShaft;
	}

	public IModel getDriveShaft() {
		return drvShaft;
	}
}
