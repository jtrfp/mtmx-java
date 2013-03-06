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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import jtrfp.common.FileStoreException;
import jtrfp.common.raw.RawImage;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import de.stefanteitge.mtmx.EngineException;
import de.stefanteitge.mtmx.texture.ITexture;
import de.stefanteitge.mtmx.texture.ITextureLoader;

/**
 * Basic texture loader from raw data.
 * (Inspired by LWJGL example texture loader.)
 * @author Stefan Teitge
 */
public class TextureLoader implements ITextureLoader {

	public TextureLoader() {
	}

	private int createTextureId() {
		IntBuffer textureId = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(textureId);
		return textureId.get();
	}

	private ByteBuffer makeByteBuffer(BufferedImage bufferedImage, int width, int height) {
		// create 24 bit RGB color model
		ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
				new int[] {8, 8, 8, 0},
				false,
				false,
				ComponentColorModel.OPAQUE,
				DataBuffer.TYPE_BYTE);
		// byte raster
		WritableRaster raster = Raster.createInterleavedRaster(
				DataBuffer.TYPE_BYTE,
				width,
				height,
				3,
				null);
		// create new byte based image
		BufferedImage byteImage = new BufferedImage(
				colorModel,
				raster,
				false,
				null);

		// copy image from original image (int) to new image (byte)
		Graphics g = byteImage.getGraphics();
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, width, height);
		g.drawImage(bufferedImage, 0, 0, null);

		// convert to linear buffer
		byte[] byteData = ((DataBufferByte) byteImage.getRaster().getDataBuffer()).getData();
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(byteData.length);
		byteBuffer.order(ByteOrder.nativeOrder());
		byteBuffer.put(byteData, 0, byteData.length);
		byteBuffer.flip();

		return byteBuffer;
	}


	@Override
	public ITexture load(RawImage rawImage) throws EngineException {
		BufferedImage bufferedImage = null;

		try {
			bufferedImage = rawImage.toImage();
		} catch (FileStoreException e) {
			throw new EngineException("Could not convert texture image.", e);
		}

		final int type = GL11.GL_TEXTURE_2D;
		final int width = bufferedImage.getWidth();
		final int height = bufferedImage.getHeight();

		int textureId = createTextureId();
		Texture texture = new Texture(type, textureId, width, height);
		GL11.glBindTexture(type, textureId); // really neccessary here?

		ByteBuffer buffer = makeByteBuffer(bufferedImage, width, height);
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		GL11.glTexImage2D(
				type,
				0,
				GL11.GL_RGB,
				width,
				height,
				0,
				GL11.GL_RGB,
				GL11.GL_UNSIGNED_BYTE,
				buffer);
		System.out.println("Created texture " + textureId);
		return texture;
	}
}
