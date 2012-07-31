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
package de.stefanteitge.mtmx.core;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.stefanteitge.mtmx.core.file.FileLoadException;
import de.stefanteitge.mtmx.core.file.raw.IRawData;
import de.stefanteitge.mtmx.core.file.raw.RawFile;

public class RawFileTest {

	private RawFile rawFile;

	@Before
	public void setUp() {
		File file = new File(ITestConfig.EXTRACTED_MTM2_FILES_DIR, "ART/C8OFF31.RAW");

		Assert.assertTrue("Raw file does not exist.", file.exists());

		rawFile = new RawFile(file);
	}

	@Test
	public void testGetRawdata() throws FileLoadException {
		IRawData rawData = rawFile.getRawData();
		Assert.assertEquals("Width should be 64.", 64, rawData.getWidth());
		Assert.assertEquals("Height should be 64.", 64, rawData.getHeight());
	}
}
