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
package org.jtrfp.mtmx.draw;


import org.jtrfp.jtrfp.Vertex3f;
import org.jtrfp.jtrfp.bin.IBinData;
import org.jtrfp.jtrfp.bin.IBinFace;
import org.jtrfp.jtrfp.bin.IBinTexCoord;
import org.jtrfp.jtrfp.bin.IBinVertex;
import org.jtrfp.mtmx.Engine;
import org.jtrfp.mtmx.ITextureManager;
import org.jtrfp.mtmx.model.IModel;
import org.jtrfp.mtmx.texture.ITexture;
import org.lwjgl.opengl.GL11;

public class ModelDrawer implements IDrawer {

	private final IModel model;

	private Vertex3f translation;

	private Vertex3f rotationAxis;

	private Vertex3f scale;

	private final Engine engine;

	private int renderMode = GL11.GL_POLYGON;

	private float rotationAngle;

	public ModelDrawer(Engine engine, IModel model) {
		this.engine = engine;
		this.model = model;
	}

	public Vertex3f getTranslation() {
		return translation;
	}

	public void setTranslation(Vertex3f translation) {
		this.translation = translation;
	}

	public Vertex3f getRotationAxis() {
		return rotationAxis;
	}

	public float getRotationAngle() {
		return rotationAngle;
	}

	public void setRotation(Float rotationAngle, Vertex3f rotationAxis) {
		this.rotationAngle = rotationAngle;
		this.rotationAxis = rotationAxis;
	}

	public IModel getModel() {
		return model;
	}

	public int getRenderMode() {
		return renderMode;
	}

	public void setRenderMode(int renderMode) {
		this.renderMode = renderMode;
	}

	public void draw() {
		IBinData binData = model.getBinData();
		IBinVertex[] vertexes = binData.getVertexes();
		IBinFace[] faces = binData.getFaces();

		GL11.glPushMatrix();

		if (translation != null) {
			GL11.glTranslatef(translation.getX(), translation.getY(), translation.getZ());
		}
		if (scale != null) {
			GL11.glScalef(scale.getX(), scale.getY(), scale.getZ());
		}
		if (rotationAxis != null) {
			GL11.glRotatef(
					rotationAngle,
					rotationAxis.getX(),
					rotationAxis.getY(),
					rotationAxis.getZ());
		}

		float n = 1.0f / 65536;
		for (int i = 0; i < faces.length; i++) {
			IBinFace face = faces[i];

			ITextureManager textureManager = engine.getResourceManager().getTextureManager();
			ITexture texture = textureManager.get(face.getTextureName());
			texture.bind();

			IBinVertex normal = face.getNormal();

			GL11.glBegin(renderMode);
			IBinTexCoord[] coords = face.getTexCoords();

			for (int j = coords.length - 1; j >= 0; j--) {
				IBinTexCoord coord = coords[j];
				IBinVertex vertex = vertexes[coord.getVertexIndex()];

				GL11.glNormal3f(-normal.getX() * n, normal.getY() * n, normal.getZ() * n);
				GL11.glTexCoord2f(coord.getU(), coord.getV());

				GL11.glVertex3f(
						-vertex.getX() * IDrawer.SCALE,
						vertex.getY() * IDrawer.SCALE,
						vertex.getZ() * IDrawer.SCALE);
			}
			GL11.glEnd();
		}

		GL11.glPopMatrix();
	}

	public void setScale(Vertex3f scale) {
		this.scale = scale;
	}

	public Vertex3f getScale() {
		return scale;
	}
}
