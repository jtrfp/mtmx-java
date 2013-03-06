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
package de.stefanteitge.mtmx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class EngineUtil {

	public static FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buffer = ByteBuffer.allocateDirect((Float.SIZE / 8) * data.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static IntBuffer createIntBuffer(int[] data) {
		IntBuffer buffer = ByteBuffer.allocateDirect((Integer.SIZE / 8) * data.length).order(ByteOrder.nativeOrder()).asIntBuffer();
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
