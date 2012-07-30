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
package de.stefanteitge.mtmx.core.file.internal.bin;

import java.io.IOException;

import de.stefanteitge.mtmx.core.file.FileLoadException;
import de.stefanteitge.mtmx.core.file.bin.IBinData;
import de.stefanteitge.mtmx.core.file.bin.IBinPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.pod.PodFileEntry;
import de.stefanteitge.mtmx.core.file.pod.PodFile;


public class BinPodFileEntry extends PodFileEntry implements IBinPodFileEntry {

	public BinPodFileEntry(PodFile podFile) {
		super(podFile);
	}

	@Override
	public IBinData getData() throws FileLoadException {
		try {
			BinDataLoader binDataLoader = new BinDataLoader();
			return binDataLoader.load(getInputStreamFromPod());
		} catch (IOException e) {
			throw new FileLoadException(e);
		}
	}
}
