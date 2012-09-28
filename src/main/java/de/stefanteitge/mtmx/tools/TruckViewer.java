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
package de.stefanteitge.mtmx.tools;

import java.io.File;

import jtrfp.common.pod.PodFile;
import jtrfp.game.GameDirFactory;
import jtrfp.game.ITriGameDir;
import de.stefanteitge.mtmx.Engine;
import de.stefanteitge.mtmx.EngineException;
import de.stefanteitge.mtmx.tools.internal.TruckViewerConfiguration;


public class TruckViewer {

	public static void main(String[] args) {
		try {
			if (args.length != 3) {
				String msg = "You must provide game dir, POD file name and truck path as arguments.";
				System.out.println(msg);
				System.exit(1);
			}
			(new TruckViewer()).run(args[0], args[1], args[2]);
		} catch (ViewerException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public void run(ITriGameDir gameDir, PodFile podFile, String path) throws ViewerException {
		try {
			Engine engine = new Engine(gameDir, new TruckViewerConfiguration(podFile, path));
			engine.start("mtmX Truck Viewer", false);
		} catch (EngineException e) {
			throw new ViewerException(e);
		}
	}

	public void run(String gameDirPath, String podFileName, String path) throws ViewerException {
		ITriGameDir gameDir = GameDirFactory.create(new File(gameDirPath));
		run(gameDir, gameDir.findPodFile(podFileName), path);
	}
}

