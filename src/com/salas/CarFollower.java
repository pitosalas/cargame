package com.salas;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class CarFollower implements ITimerCallback {
	Car car;
	DashTextBox box;

	public CarFollower(Car theCar, DashTextBox theBox) {
		car = theCar;
		box = theBox;
	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		box.textBox.setText(car.toString());
	}

}
