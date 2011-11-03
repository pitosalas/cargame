package com.salas;

import com.badlogic.gdx.math.Vector2;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;


public class PathSegment {
	Vector2 start;
	Vector2 end;
	Vector2 temp = new Vector2();
	
	public PathSegment(Vector2 from, Vector2 to) {
		start = from.mul(1.0f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		end = to.mul(1.0f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);;
	}

	float distance() {
		float res = start.dst(end);
		return res;
	}
	
	Vector2 velVector(double length) {
		double a = end.x - start.x;
		double o = end.y - start.y;
		double h = Math.sqrt((a * a) + (o * o));
		double ratio = length / h;
		temp.x = (float) (a * ratio);
		temp.y = (float) (o * ratio);
		return temp;
	}
}
