package com.salas;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import android.graphics.Color;
import android.util.Log;


// AndEngine implementation of World class from carmodel
public class WorldAE extends World {
	GameContext gameContext;
	Line redVector;
	Line greenVector;
	TextBox toolTipA;
	TextBox toolTipB;
	
	WorldAE(GameContext gc) {
		gameContext = gc;
	}

	@Override
	void showCursorAt(Vector2 loc) {
		UtilitySprites.cursorSprite(box2d2PixelCoordinate(loc));
	}
	
	static Vector2 pixel2Box2dCoordinate(Vector2 from) {
		Float pixel2meter = new Float(1.0f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		Log.i("entity", pixel2meter.toString());
		return from.mul(pixel2meter);
	}
	
	static Vector2 box2d2PixelCoordinate(Vector2 from) {
		return from.cpy().mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
	}

	@Override
	void showRedVectorFrom(Vector2 origin, Vector2 vector) {
	if (redVector == null) {
			redVector = new Line(0, 0, 0, 0, 2.0f);
			redVector.setColor(1.0f, 0.0f, 0.0f);
			gameContext.scene.attachChild(redVector);
		}
		final Vector2 startpoint = box2d2PixelCoordinate(origin);
		final Vector2 endpoint = box2d2PixelCoordinate(vector).add(startpoint);
		redVector.setPosition(startpoint.x, startpoint.y, endpoint.x, endpoint.y);
	}

	@Override
	void showGreenVectorFrom(Vector2 origin, Vector2 vector) {
	if (greenVector == null) {
			greenVector = new Line(0, 0, 0, 0, 2.0f);
			greenVector.setColor(0.0f, 1.0f, 0.0f);
			gameContext.scene.attachChild(greenVector);
		}
		final Vector2 startpoint = box2d2PixelCoordinate(origin);
		final Vector2 endpoint = box2d2PixelCoordinate(vector).add(startpoint);
		greenVector.setPosition(startpoint.x, startpoint.y, endpoint.x, endpoint.y);
	}
	@Override
	void showPath(Path apath) {
		for (Vector2 wpoint : apath.waypoints) {
			showCursorAt(wpoint);
		}
	}

	@Override
	void log(String string) {
		Log.i("world", string);
	}

	@Override
	void showTooltipA(float x, float y, String text) {
		Vector2 pixelWhere = box2d2PixelCoordinate(new Vector2(x,y));
		if (toolTipA == null) {
			toolTipA = new TextBox((int) pixelWhere.x, (int) pixelWhere.y);
			gameContext.scene.attachChild(toolTipA.textBox);
		}
		toolTipA.textBox.setColor(1.0f, 1.0f, 0.0f);
		toolTipA.textBox.setPosition(pixelWhere.x+15, pixelWhere.y+30);
		toolTipA.textBox.setText(text);
	}

	void showTooltipB(float x, float y, String text) {
		Vector2 pixelWhere = box2d2PixelCoordinate(new Vector2(x,y));
		if (toolTipB == null) {
			toolTipB = new TextBox((int) pixelWhere.x, (int) pixelWhere.y);
			gameContext.scene.attachChild(toolTipB.textBox);
		}
		toolTipB.textBox.setPosition(pixelWhere.x+15, pixelWhere.y-60);
		toolTipB.textBox.setColor(0.0f, 1.0f, 1.0f);
		toolTipB.textBox.setText(text);
	}
	
}
