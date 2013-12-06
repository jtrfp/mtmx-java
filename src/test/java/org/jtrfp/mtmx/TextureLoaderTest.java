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

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.jtrfp.jtrfp.game.GameDirFactory;
import org.jtrfp.jtrfp.game.ITriGameDir;
import org.jtrfp.jtrfp.pod.PodFile;
import org.jtrfp.mtmx.texture.ITexture;
import org.junit.Before;
import org.junit.Test;

public class TextureLoaderTest {

	private static final String RAW_NAME = "c2rd02.raw";

	private static final String POD_FILE_NAME = "crazy98.pod";

	private Engine engine;

	private ITriGameDir mtm2GameDir;

	@Before
	public void setUp() {
		mtm2GameDir = GameDirFactory.create(new File(ITestConfig.MTM2_DIR));
		assertNotNull("MTM2 game dir not correctly set.", mtm2GameDir);
	}

	@Test
	public void testLoad() throws Exception {
		engine = new Engine(mtm2GameDir, new TestLoadConfiguration());
		engine.start("mtmX Texture Loading Test", false);
	}

	private class TestLoadConfiguration extends AbstractEngineConfiguration {

		@Override
		public void draw() {
			engine.stop();
		}

		@Override
		public void initLight() {
		}

		@Override
		public void initResources() throws EngineException {
			final PodFile podFile = engine.getGameDir().findPodFile(POD_FILE_NAME);
			assertNotNull("POD file not found.", podFile);

			ITexture texture = engine.getResourceManager().getTextureManager().load(podFile, RAW_NAME);
			assertNotNull("No texture loaded.", texture);
		}

		@Override
		public void initView() {
		}

	}
}
