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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jtrfp.jtrfp.game.ITriGameDir;
import org.jtrfp.mtmx.internal.DisplayModeSelectionDialog;
import org.jtrfp.mtmx.internal.ResourceManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Engine {

	private final ITriGameDir gameDir;

	private final ResourceManager resourceManager;

	private final Logger logger;

	private final AbstractEngineConfiguration engineConfiguration;

	private boolean stop = false;

	public Engine(ITriGameDir gameDir, AbstractEngineConfiguration engineConfiguration) {
		this.gameDir = gameDir;
		this.engineConfiguration = engineConfiguration;

		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

		resourceManager = new ResourceManager(this);
	}

	protected Logger getLogger() {
		return logger;
	}

	public ITriGameDir getGameDir() {
		return gameDir;
	}

	public DisplayMode getDisplayMode() {
		return Display.getDisplayMode();
	}

	public IResourceManager getResourceManager() {
		return resourceManager;
	}

	public void start(String title, boolean chooseDisplayMode) throws EngineException {
		try {
			if (chooseDisplayMode) {
				setupDisplay();
			} else {
				Display.setDisplayMode(new DisplayMode(640, 480));
			}
			Display.setTitle(title);
			Display.create();

			getLogger().log(Level.INFO, "GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
			getLogger().log(Level.INFO, "GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
			getLogger().log(Level.INFO, "GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));

			engineConfiguration.init(this);

			engineConfiguration.initResources();

			Keyboard.create();
			Keyboard.enableRepeatEvents(false);
			engineConfiguration.initKeyboard();

			engineConfiguration.initLight();

			DisplayMode displayMode = getDisplayMode();
			GL11.glViewport(0, 0, displayMode.getWidth(), displayMode.getHeight());
			engineConfiguration.initView();

			loop();

			destroy();
		} catch (LWJGLException e) {
			destroy();
			throw new EngineException("Failed to create display.", e);
		}
	}

	public void destroy() {
		engineConfiguration.destroy();
		Display.destroy();
	}

	private void loop() {
		long startTime = System.currentTimeMillis() + 1000;
		long fps = 0;

		while (!Display.isCloseRequested() && !stop) {

			engineConfiguration.draw();

			Display.update();
			if (startTime > System.currentTimeMillis()) {
				fps++;
			} else {
				startTime = System.currentTimeMillis() + 1000;
				System.out.println(fps + " FPS");
				fps = 0;
			}
		}
	}

	private void setupDisplay() throws LWJGLException {
		DisplayMode[] displayModes = Display.getAvailableDisplayModes();

		DisplayModeSelectionDialog dialog = new DisplayModeSelectionDialog(displayModes);
		dialog.setVisible(true);
//		if (result == Dialog.OK) {
//			Display.setFullscreen(dialog.isFullscreen());
//			Display.setDisplayMode(dialog.getDisplayMode());
//		} else {
			Display.setDisplayMode(new DisplayMode(640, 480));
//		}
	}

	public void stop() {
		stop = true;
	}
}
