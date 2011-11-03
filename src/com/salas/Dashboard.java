package com.salas;

import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.input.touch.TouchEvent;

import com.salas.Car.CarCommand;

public class Dashboard {
	
	private Rectangle hudTop;
	public HUD hud;
	private int hudWidth;
	private int hudHeight;
	
	private static final int HUD_TOP_HEIGHT = 50;
	private static final int PADBANK_SPACER = 20;
	
	public Dashboard(CommonActivity com) {
		hudWidth = com.screenWidth;
		hudHeight = HUD_TOP_HEIGHT;
		com.screenHeight -= HUD_TOP_HEIGHT;
	}

	public void createAndAttach(CommonActivity com, Scene scene, ZoomCamera camera) {
		hud = new HUD();
		camera.setHUD(hud);
		hudTop = new Rectangle(0, 0, hudWidth, hudHeight);
		hudTop.setColor(0.5f, 0.0f, 0.0f);
		camera.setCenter(camera.getCenterX(), camera.getCenterY() - hudHeight);
		hud.attachChild(hudTop);
	}
		
	protected void configHud() {
	}

	public void addTextBox(DashTextBox statMessage) {
		hudTop.attachChild(statMessage.textBox);
	}
		
	public void addPad(DashTouchPad forcePad) {
		hudTop.attachChild(forcePad.touchpad);
	}

	public void createAndAddPadBank(float xInit, float yPos, float width, float height,
									final Car acar, CarCommand[] padBankArgs) {
		float xPos = xInit;
		for (final CarCommand arg:padBankArgs) {
			Shape touchpad = new Rectangle(xPos, yPos, width, height) {
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, 
						 final float pTouchAreaLocalX, 
						 final float pTouchAreaLocalY) {
							if (pSceneTouchEvent.isActionUp()) {
								acar.do_command(arg);
							}
							return true;
				}
			};
			hud.registerTouchArea(touchpad);
			touchpad.setColor(0, 0.4f, 0.4f);
			hudTop.attachChild(touchpad);
			xPos += width + PADBANK_SPACER;
		};
	}
		

}
