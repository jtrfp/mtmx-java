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


import org.lwjgl.opengl.GL11;

import de.stefanteitge.mtmx.texture.ITexture;

public class Texture implements ITexture {

	private int glTextureType;
	
	private int textureId;
	
	private int width;
	
	private int height;
	
	public Texture(int glTextureType, int textureId, int width, int height) {
		this.glTextureType = glTextureType;
		this.textureId = textureId;
		this.width = width;
		this.height = height;
	}

	public int getTextureId() {
		return textureId;
	}

	@Override
	public void bind() {
		GL11.glBindTexture(glTextureType, textureId);
	}

	public int getGlTextureType() {
		return glTextureType;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public void unbind() {
		GL11.glBindTexture(glTextureType, 0);
	}
}
