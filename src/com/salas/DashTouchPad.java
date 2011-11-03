package com.salas;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.input.touch.TouchEvent;

import android.util.Log;

public class DashTouchPad {

	public Shape touchpad;
	
	public void loadResources(CarGameActivity car2ExpActivity) {
	}

	public void createAndAttach(float pX, float pY, float pWidth, float pHeight, final Car acar, final int direction, Dashboard dash) {
		touchpad = new Rectangle(pX, pY, pWidth, pHeight) {
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, 
										 final float pTouchAreaLocalX, 
										 final float pTouchAreaLocalY) {
				acar.applyForce(direction);
				return true;
			};
		};
		dash.hud.registerTouchArea(touchpad);
		touchpad.setColor(0, 0.4f, 0.4f);
		dash.addPad(this);

	}
}

