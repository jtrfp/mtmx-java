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
package de.stefanteitge.mtmx.core.file.tex;

import java.io.File;

import de.stefanteitge.mtmx.core.file.FileLoadException;
import de.stefanteitge.mtmx.core.file.internal.tex.TexDataLoader;


public class TexFile {

	private final File file;

	private ITexData texData;

	public TexFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public ITexData getData() throws FileLoadException {
		if (texData == null) {
			texData = TexDataLoader.load(this);
		}

		return texData;
	}
}