package com.salas;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;


public class SteerableCarBrain implements ITimerCallback {

	SteerableCar car;
	LevelModel map;

	public SteerableCarBrain(LevelModel theMap, SteerableCar theCar) {
		car = theCar;
		map = theMap;
	}

	@Override
	public void onTimePassed(TimerHandler timerHandler) {

		// Compute the steering force by applying all the steering behaviors
		Vector2 steeringForce = car.steering().calculate();
		
		// Apply the calculated steering force to the car's entity
		car.entity().applyForce(steeringForce);
		
		// Make the car's sprite (display) point in the direction of motion
		car.setRotation(car.entity().getHeading());		
	}
}
