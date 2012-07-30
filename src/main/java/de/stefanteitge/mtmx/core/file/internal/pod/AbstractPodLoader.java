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
package de.stefanteitge.mtmx.core.file.internal.pod;

import java.io.IOException;
import java.io.InputStream;

import de.stefanteitge.mtmx.core.file.internal.act.ActPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.audio.ModPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.audio.WavPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.bin.BinPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.clr.ClrPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.image.BmpPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.image.TifPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.kfm.KfmPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.lvl.LvlPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.raw.RawPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.sit.SitPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.tex.TexPodFileEntry;
import de.stefanteitge.mtmx.core.file.internal.trk.TrkPodFileEntry;
import de.stefanteitge.mtmx.core.file.pod.PodFile;


public abstract class AbstractPodLoader {

	private final InputStream is;

	private final PodFile podFile;

	protected final byte[] buffer4 = new byte[4];

	private long count;

	public AbstractPodLoader(InputStream is, PodFile podFile) {
		this.is = is;
		this.podFile = podFile;
		count = 4;
	}

	//	protected InputStream getIs() {
	//		return is;
	//	}

	protected PodFile getPodFile() {
		return podFile;
	}

	protected void read(byte[] buffer) throws IOException {
		count += is.read(buffer);
	}

	protected int read() throws IOException {
		int read = is.read();
		if (read > -1) {
			count++;
		}
		return read;
	}

	protected void read4(int amount) throws IOException {
		for (int i = 0; i < amount; i++) {
			count += is.read(buffer4);
		}
	}

	abstract public PodData load() throws IOException;

	protected void seekForwardTo(int offset) throws IOException {
		if (offset <= count) {
			return;
		}
		count += is.skip(offset - count);
	}

	protected PodFileEntry createPodFileEntry(PodData podData, String path) {
		String localPath = path.toLowerCase();
		if (localPath.endsWith(".act")) {
			return new ActPodFileEntry(podFile);
		} else if (localPath.endsWith(".raw")) {
			return new RawPodFileEntry(podFile);
		} else if (localPath.endsWith(".sit")) {
			return new SitPodFileEntry(podFile);
		} else if (localPath.endsWith(".trk")) {
			return new TrkPodFileEntry(podFile);
		} else if (localPath.endsWith(".bmp")) {
			return new BmpPodFileEntry(podFile);
		} else if (localPath.endsWith(".wav")) {
			return new WavPodFileEntry(podFile);
		} else if (localPath.endsWith(".bin")) {
			return new BinPodFileEntry(podFile);
		} else if (localPath.endsWith(".lvl")) {
			return new LvlPodFileEntry(podFile);
		} else if (localPath.endsWith(".mod")) {
			return new ModPodFileEntry(podFile);
		} else if (localPath.endsWith(".tex")) {
			return new TexPodFileEntry(podFile);
		} else if (localPath.endsWith(".tif")) {
			return new TifPodFileEntry(podFile);
		} else if (localPath.endsWith(".clr")) {
			return new ClrPodFileEntry(podFile);
		} else if (localPath.endsWith(".kfm")) {
			return new KfmPodFileEntry(podFile);
		}

		PodFileEntry podFileEntry = new PodFileEntry(podFile);
		podData.addUntypedEntry(podFileEntry);
		return podFileEntry;
	}
}