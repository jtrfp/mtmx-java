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
package de.stefanteitge.mtmx.core.file.trk;

import de.stefanteitge.mtmx.core.file.Vertex3f;

public interface ITrkData {

	String getTruckName();

	String getTruckModelBaseName();

	String getTireModelBaseName();

	String getAxleModelName();

	String getShockTextureName();

	String getBarTextureName();

	Vertex3f getAxlebarOffset();

	Vertex3f getDriveshaftPos();

	Vertex3f getLeftFrontTirePos();

	Vertex3f getRightFrontTirePos();

	Vertex3f getLeftRearTirePos();

	Vertex3f getRightRearTirePos();
}