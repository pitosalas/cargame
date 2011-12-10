package com.salas;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class PathCarBrain implements ITimerCallback {
	PathCar car;
	PathSegment currSeg;
	float pathlen;

	public PathCarBrain(PathCar theCar) {
		currSeg = null;
		car = theCar;
	}

/*
 * This is the key algorithm.
 */
	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		if (currSeg == null) {
			currSeg = car.getNextPathSegment();			
			// Make sure body is at the start of the segment.
			if (currSeg != null) {
				car.getBody().setTransform(currSeg.start, 0.0f);
//				car.sprite.setPosition(currSeg.start.x, currSeg.start.y);
			}
		}
		drive_along();
	}

	private void drive_along() {
		if (currSeg == null) return;
		Body bod = car.getBody();
		float curX = bod.getPosition().x;
		float curY = bod.getPosition().y;
		float pathLen = currSeg.distance();
		float distSoFar = currSeg.start.dst(curX, curY);
		float fractSoFar = distSoFar / pathLen;
		Vector2 place = bod.getWorldCenter();
		if (fractSoFar < 0.5) {
			Vector2 force = currSeg.velVector(10.0);
			bod.applyForce(force, place);
		} else if (fractSoFar >= 1.0) {
			bod.setLinearVelocity(0,0);
			currSeg = null;
		}
	}
}
