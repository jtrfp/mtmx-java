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
import de.stefanteitge.mtmx.core.file.act.ActFile;
import de.stefanteitge.mtmx.core.file.raw.RawFile;
import de.stefanteitge.mtmx.core.file.raw.RawImage;

public class RawImageTest {

	private RawImage rawImage;

	@Before
	public void setUp() throws FileLoadException {
		File file = new File(ITestConfig.EXTRACTED_MTM2_FILES_DIR, "art/C8OFF31.RAW");
		Assert.assertTrue("Raw file does not exist.", file.exists());
		RawFile rawFile = new RawFile(file);

		File file2 = new File(ITestConfig.EXTRACTED_MTM2_FILES_DIR, "art/C8OFF31.ACT");
		Assert.assertTrue("ACT file does not exist.", file2.exists());
		ActFile actFile = new ActFile(file2);

		rawImage = new RawImage(rawFile.getRawData(), actFile.getData());
	}

	@Test
	public void testStoreAsBitmap() throws Exception {
		File bitmapFile = new File("out/C8OFF31.bmp");
		rawImage.storeAsBitmap(bitmapFile);

		Assert.assertTrue("Bitmap has not been created.", bitmapFile.exists());

		Assert.assertTrue("Bitmap has no size.", bitmapFile.length() > 0);
	}
}