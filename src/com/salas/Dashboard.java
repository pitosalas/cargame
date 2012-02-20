package com.salas;

import java.util.ArrayList;

import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.input.touch.TouchEvent;

public class Dashboard {
	
	private Rectangle hudTop;
	public HUD hud;
	private int hudWidth;
	private int hudHeight;
	
	private static final int HUD_TOP_HEIGHT = 50;
	
	public Dashboard(CommonActivity com) {
		hudWidth = com.screenWidth;
		hudHeight = HUD_TOP_HEIGHT;
		com.screenHeight -= HUD_TOP_HEIGHT;
	}

	public void createAndAttach(CommonActivity com, Scene scene, ZoomCamera camera) {
		hud = new HUD();
		camera.setHUD(hud);
		hudTop = new Rectangle(0, 0, hudWidth, hudHeight);
		hudTop.setColor(0.2f, 0.2f, 0.2f);
		camera.setCenter(camera.getCenterX(), camera.getCenterY() - hudHeight);
		hud.attachChild(hudTop);
	}
		
	protected void configHud() {
	}

	public void addTextBox(TextBox statMessage) {
		hudTop.attachChild(statMessage.textBox);
	}
		
	class TweakBoxInfo {
		TweakBox tweakbox;
		TextBox textbox;
		public TweakBoxInfo(TweakBox twb, TextBox txb) {
			tweakbox = twb;
			textbox = txb;
		}
	}

	ArrayList<TweakBoxInfo> tbInfo = new ArrayList<TweakBoxInfo>();
	static int TWEAKBOXTEXTWIDTH = 120;
	static int TWEAKBOXPADWIDTH = 32;
	static int TWEAKSPACER = 3;
	static int TWEAKBOXFULLWIDTH = TWEAKBOXTEXTWIDTH + 2*TWEAKBOXPADWIDTH + 2*TWEAKSPACER;
	static int TWEAKBOXHEIGHT = 36;
	
	public void addTweakboxes() {
		int xPos = 4;
		int yPos = 7;
		float color = 0.2f;
		for (final TweakBox twb : TweakBox.boxes().values()) {

			Rectangle tweakBoxRect = new Rectangle(xPos,  yPos, TWEAKBOXFULLWIDTH, TWEAKBOXHEIGHT);
			tweakBoxRect.setColor(color,0,0);

			// A textbox in the middle
			final TextBox txb = new TextBox(TWEAKBOXPADWIDTH+TWEAKSPACER, yPos);
			txb.setText(twb.fullString());
			tweakBoxRect.attachChild(txb.textBox);
			
			// A minus pad on the left
			Shape minusPad = new Rectangle(0, yPos-5, TWEAKBOXPADWIDTH, TWEAKBOXHEIGHT) {
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, 
						final float pTouchAreaLocalX, 
						final float pTouchAreaLocalY) {
						if (pSceneTouchEvent.isActionUp()) {
								twb.decrement();
								txb.setText(twb.fullString());
						}
						return true;
					}
				};
			hud.registerTouchArea(minusPad);
			tweakBoxRect.attachChild(minusPad);
			minusPad.setAlpha(0);
			minusPad.attachChild(UtilitySprites.minusIconSprite(0, 0));
			
			// A plus pad on the right
			Shape plusPad = new Rectangle(TWEAKBOXPADWIDTH+TWEAKBOXTEXTWIDTH+TWEAKSPACER, yPos-5, TWEAKBOXPADWIDTH, TWEAKBOXHEIGHT) {
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, 
					final float pTouchAreaLocalX, 
					final float pTouchAreaLocalY) {
					if (pSceneTouchEvent.isActionUp()) {
							twb.increment();
							txb.setText(twb.fullString());
					}
					return true;
				}
			};
			hud.registerTouchArea(plusPad);
			tweakBoxRect.attachChild(plusPad);
			plusPad.setAlpha(0.0f);
			plusPad.attachChild(UtilitySprites.plusIconSprite(0, 0));
			
			tbInfo.add(new TweakBoxInfo(twb, txb));
			
			xPos += TWEAKBOXFULLWIDTH+10;
			color += 0.2f;
			hudTop.attachChild(tweakBoxRect);
		}
		
	}
		

}
