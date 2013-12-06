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

import java.util.ArrayList;
import java.util.List;

import org.jtrfp.jtrfp.Vertex3f;
import org.jtrfp.jtrfp.trk.ITrkData;
import org.jtrfp.mtmx.Engine;
import org.jtrfp.mtmx.truck.ITruck;
import org.lwjgl.opengl.GL11;

public class TruckDrawer implements IDrawer {

	private static final float MAX_STEER_ANGLE = 20.0f;

	private int renderMode = GL11.GL_POLYGON;

	private final ITruck truck;

	private final Engine engine;

	private final List<ModelDrawer> drawers;

	private float steerAngle = 0.0f;

	private ModelDrawer leftFrontTireDrawer;

	private ModelDrawer rightFrontTireDrawer;

	private ModelDrawer rightRearTireDrawer;

	private ModelDrawer leftRearTireDrawer;

	private Vertex3f translation;

	private Vertex3f rotationAxis;

	private float rotationAngle;

	private Vertex3f scale;

	public TruckDrawer(Engine engine, ITruck truck) {
		this.engine = engine;
		this.truck = truck;

		drawers = new ArrayList<ModelDrawer>();

		setupDrawers();
	}

	public ITruck getTruck() {
		return truck;
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
		if (rotationAngle < -180.0f) {
			rotationAngle += 360.0f;
		}
		if (rotationAngle > 180.0f) {
			rotationAngle -= 360.0f;
		}
		this.rotationAngle = rotationAngle;
		this.rotationAxis = rotationAxis;
	}

	public void setScale(Vertex3f scale) {
		this.scale = scale;
	}

	public Vertex3f getScale() {
		return scale;
	}

	public void draw() {
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

		for (ModelDrawer drawer : drawers) {
			drawer.draw();
		}
		GL11.glPopMatrix();
	}

	private void setupDrawers() {
		ModelDrawer bodyDrawer = new ModelDrawer(engine, truck.getBody());
		drawers.add(bodyDrawer);

		ITrkData truckData = truck.getTruckData();

		// tire positions
		Vertex3f leftFrontPos = truckData.getLeftFrontTirePos().multiply(IDrawer.FEET_SCALE);
		Vertex3f rightFrontPos = truckData.getRightFrontTirePos().multiply(IDrawer.FEET_SCALE);
		Vertex3f leftRearPos = truckData.getLeftRearTirePos().multiply(IDrawer.FEET_SCALE);
		Vertex3f rightRearPos = truckData.getRightRearTirePos().multiply(IDrawer.FEET_SCALE);

		// axle midpoints
		Vertex3f frontAxleMidpoint = Vertex3f.average(leftFrontPos, rightFrontPos);
		Vertex3f rearAxleMidpoint = Vertex3f.average(leftRearPos, rightRearPos);

		// front axle drawer
		ModelDrawer frontAxleDrawer = new ModelDrawer(engine, truck.getAxle());
		frontAxleDrawer.setTranslation(frontAxleMidpoint);
		drawers.add(frontAxleDrawer);

		// rear axle drawer
		ModelDrawer rearAxleDrawer = new ModelDrawer(engine, truck.getAxle());
		rearAxleDrawer.setTranslation(rearAxleMidpoint);
		drawers.add(rearAxleDrawer);

		// tire drawers
		leftFrontTireDrawer = new ModelDrawer(engine, truck.getLeftTire());
		leftFrontTireDrawer.setTranslation(leftFrontPos);
		drawers.add(leftFrontTireDrawer);

		leftRearTireDrawer = new ModelDrawer(engine, truck.getLeftTire());
		leftRearTireDrawer.setTranslation(leftRearPos);
		drawers.add(leftRearTireDrawer);

		rightFrontTireDrawer = new ModelDrawer(engine, truck.getRightTire());
		rightFrontTireDrawer.setTranslation(rightFrontPos);
		drawers.add(rightFrontTireDrawer);

		rightRearTireDrawer = new ModelDrawer(engine, truck.getRightTire());
		rightRearTireDrawer.setTranslation(rightRearPos);
		drawers.add(rightRearTireDrawer);

		//		Vertex3f drvshaftPosScaled = truckData.getDriveshaftPos().multiply(FEET_SCALE);
		//		float frontDistance = Vertex3f.distance(drvshaftPosScaled, frontAxleMidpoint);
		//		ModelStats drvShaftStats = new ModelStats(truck.getDriveShaft().getBinData());
		//		float len = drvShaftStats.getMaxLength() * SCALE;
		//		float driveshaftScale = frontDistance / len;
		//
		//		Vertex3f frontDriveshaftMidpoint = Vertex3f.average(
		//				drvshaftPosScaled,
		//				frontAxleMidpoint
		//		);
		//		float a = frontAxleMidpoint.getZ() + drvshaftPosScaled.getZ();
		//		float b = -1 * frontAxleMidpoint.getY() - drvshaftPosScaled.getY();
		//		float angle = (float) Math.acos(a / (Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2))));
		//		angle *= 90.0f / Math.PI;
		//		ModelDrawer driveshaftDrawer = new ModelDrawer(engine, truck.getDriveShaft());
		//		driveshaftDrawer.setTranslation(frontDriveshaftMidpoint);
		//		driveshaftDrawer.setRotation(40.0f, new Vertex3f(0.0f, 0.0f, 1.0f));
		//		drawers.add(driveshaftDrawer);
	}

	public int getRenderMode() {
		return renderMode;
	}

	public void setRenderMode(int renderMode) {
		this.renderMode = renderMode;

		for (ModelDrawer drawer : drawers) {
			drawer.setRenderMode(renderMode);
		}
	}

	public void setSteerAngle(float steerAngle) {
		if (steerAngle > 1.0f) {
			steerAngle = 1.0f;
		}
		if (steerAngle < -1.0f) {
			steerAngle = -1.0f;
		}
		this.steerAngle = steerAngle;

		final float frontAngle = steerAngle * MAX_STEER_ANGLE;
		final float rearAngle = -frontAngle / 4;
		final Vertex3f axis = new Vertex3f(0.0f, 1.0f, 0.0f);

		leftFrontTireDrawer.setRotation(frontAngle, axis);
		rightFrontTireDrawer.setRotation(frontAngle, axis);

		leftRearTireDrawer.setRotation(rearAngle, axis);
		rightRearTireDrawer.setRotation(rearAngle, axis);
	}

	public float getSteerAngle() {
		return steerAngle;
	}
}
