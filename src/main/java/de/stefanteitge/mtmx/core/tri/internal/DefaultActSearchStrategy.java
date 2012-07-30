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
package de.stefanteitge.mtmx.core.tri.internal;

import de.stefanteitge.mtmx.core.file.FileLoadException;
import de.stefanteitge.mtmx.core.file.act.IActPodFileEntry;
import de.stefanteitge.mtmx.core.file.pod.IPodFileEntry;
import de.stefanteitge.mtmx.core.file.raw.IRawPodFileEntry;
import de.stefanteitge.mtmx.core.tri.IActSearchStrategy;
import de.stefanteitge.mtmx.core.tri.ITriGameDir;

public class DefaultActSearchStrategy implements IActSearchStrategy {

	private final ITriGameDir gameDir;

	public DefaultActSearchStrategy(ITriGameDir gameDir) {
		this.gameDir = gameDir;
	}

	protected ITriGameDir getGameDir() {
		return gameDir;
	}

	public IActPodFileEntry find(IRawPodFileEntry rawEntry) {
		try {
			String search = rawEntry.getPath().toLowerCase().replace(".raw", ".act");
			IPodFileEntry entry = rawEntry.getPodFile().getData().findEntry(search);
			if (entry != null && entry instanceof IActPodFileEntry) {
				return (IActPodFileEntry) entry;
			}
		} catch (FileLoadException e) {
			return null;
		}

		return null;
	}

}
