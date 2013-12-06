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
package org.jtrfp.mtmx;

import java.io.File;

import org.jtrfp.jtrfp.FileLoadException;
import org.jtrfp.jtrfp.act.IActPodFileEntry;
import org.jtrfp.jtrfp.pod.IPodFileEntry;
import org.jtrfp.jtrfp.pod.PodFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActDataTest {

	private static final int COLOR_COUNT = 256;

	private PodFile podFile;

	@Before
	public void setUp() {
		File file = new File(ITestConfig.MTM2_DIR, "crazy98.pod");

		Assert.assertTrue("Test POD file does not exist.", file.exists() && file.isFile());

		podFile = new PodFile(file);
	}

	@Test
	public void testEntryGetData() throws FileLoadException {
		IPodFileEntry entry = podFile.getData().findEntry("art\\crazy98.act");

		Assert.assertTrue("No ACT entry found.", entry instanceof IActPodFileEntry);

		int count = ((IActPodFileEntry) entry).getData().getColorCount();

		Assert.assertEquals("Color count is not " + COLOR_COUNT, COLOR_COUNT, count);
	}
}