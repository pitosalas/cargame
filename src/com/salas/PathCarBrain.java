package com.salas;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

import com.badlogic.gdx.math.Vector2;

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
				car.body.setTransform(currSeg.start, 0.0f);
//				car.sprite.setPosition(currSeg.start.x, currSeg.start.y);
			}
		}
		drive_along();
	}

	private void drive_along() {
		if (currSeg == null) return;
		float curX = car.body.getPosition().x;
		float curY = car.body.getPosition().y;
		float pathLen = currSeg.distance();
		float distSoFar = currSeg.start.dst(curX, curY);
		float fractSoFar = distSoFar / pathLen;
		Vector2 place = car.body.getWorldCenter();
		if (fractSoFar < 0.5) {
			Vector2 force = currSeg.velVector(10.0);
			car.body.applyForce(force, place);
		} else if (fractSoFar >= 1.0) {
			car.body.setLinearVelocity(0,0);
			currSeg = null;
		}
	}
}
