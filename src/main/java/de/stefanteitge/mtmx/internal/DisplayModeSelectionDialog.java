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
package de.stefanteitge.mtmx.internal;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;

import org.lwjgl.opengl.DisplayMode;

public class DisplayModeSelectionDialog extends JFrame {

	private final DisplayMode[] displayModes;

	private DisplayMode selectedDisplayMode = null;

	private boolean fullscreen = false;

	private JList<DisplayMode> list;

	private JCheckBox fullscreenCheck;

	public DisplayModeSelectionDialog(DisplayMode[] displayModes) {
		this.displayModes = displayModes;

		setTitle("Select screen resolution");
		
		list = new JList<DisplayMode>(displayModes);

		
		if (displayModes.length > 0) {
			//list.select(0);
		}
		
//		list.addSelectionListener(new SelectionAdapter() {
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				performListSelect();
//			}
//		});
		
		getContentPane().add(list);
		
		fullscreenCheck = new JCheckBox();
		fullscreenCheck.setText("Fullscreen");	
		
//		fullscreenCheck.addSelectionListener(new SelectionAdapter() {
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				performFullscreenSelect();
//			}
//
//		});

		getContentPane().add(fullscreenCheck);
	}

	private void performFullscreenSelect() {
//		fullscreen = fullscreenCheck.getSelection();
	}

	private void performListSelect() {
//		selectedDisplayMode = displayModes[list.getSelectionIndex()];
	}

	public DisplayMode getDisplayMode() {
		return selectedDisplayMode;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}
}
