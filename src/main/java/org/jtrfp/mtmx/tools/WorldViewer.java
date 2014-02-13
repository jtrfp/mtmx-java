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
package org.jtrfp.mtmx.tools;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.jtrfp.jtrfp.game.GameDirFactory;
import org.jtrfp.jtrfp.game.ITriGameDir;
import org.jtrfp.jtrfp.pod.PodFile;
import org.jtrfp.mtmx.Engine;
import org.jtrfp.mtmx.EngineException;
import org.jtrfp.mtmx.tools.internal.WorldViewerConfiguration;

public class WorldViewer {

	public static void main(String[] args) {
		Options options = new Options();
		
		Option gameDirOption = new Option("g", "game-dir", true, "the MTM 1 or 2 game directory (required)");
		gameDirOption.setRequired(true);
		options.addOption(gameDirOption);
		
		Option podFileOption = new Option("p", "pod-file", true, "the POD file to use (required)");
		podFileOption.setRequired(true);
		options.addOption(podFileOption);
		
		Option heightMapOption = new Option("h", "height-map", true, "the height map in the pod file (required)");
		heightMapOption.setRequired(true);
		options.addOption(heightMapOption);
		
		GnuParser parser = new GnuParser();
		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(options, args);
		} catch (Exception e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("mtmx-world-viewer [OPTIONS...]", options);
			System.exit(1);
		}
		
		try {
			WorldViewer viewer = new WorldViewer();
			viewer.run(commandLine.getOptionValue("g"), commandLine.getOptionValue("p"), commandLine.getOptionValue("h"));
		} catch (ViewerException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}

	public void run(ITriGameDir gameDir, PodFile podFile, String path) throws ViewerException {
		try {
			Engine engine = new Engine(gameDir, new WorldViewerConfiguration(podFile, path));
			engine.start("mtmX World Viewer", false);
		} catch (EngineException e) {
			throw new ViewerException(e);
		}
	}

	public void run(String gameDirPath, String podFileName, String path) throws ViewerException {
		ITriGameDir gameDir = GameDirFactory.create(new File(gameDirPath));
		run(gameDir, gameDir.findPodFile(podFileName), path);
	}
}
