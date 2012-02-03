package com.salas;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;

public class CarFollower implements ITimerCallback {
	EntitySpriteAnd car;
	TextBox box;

	public CarFollower(EntitySpriteAnd theCar, TextBox theBox) {
		car = theCar;
		box = theBox;
	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
//		box.textBox.setText(car.toString());
	}

}
