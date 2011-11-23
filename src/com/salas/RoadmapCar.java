package com.salas;

import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.salas.Tile.TDir;

public class RoadmapCar extends Car {

	static int VELOCITY = 5;
	static int TURNFORCE = 7;
	static float TURN_EPSILON = 0.1f;

	private GameLevel rmap;
	private Tile curTile;
	private Tile turnTile;
	private TDir toDirection;
	private TDir fromDirection;
	boolean driving;
	boolean turning;
	
	public void placeOnRoadmap(GameLevel aRoadmap) {
		rmap = aRoadmap;
		TPos start = rmap.getStartingTPos();
		toDirection = rmap.getStartingDirection();
		setPosAndRotation(start, toDirection);
		driving = false;
		Log.v("CARDRIVE", "Car placed on map. Pos:"+start+", dir:"+toDirection);
	}

	public void start() {
		Log.v("CARDRIVE", "Car starts driving.");
		driving = true;
	}
	
	public boolean isDriving() {
		return driving;
	}
	
	public boolean isTurning() {
		return turning;
	}
	
	public Tile currTileOnMap() {
		return curTile;
	}
	
	public void setDirection(TDir newDir) {
		toDirection = newDir;
	}

	public boolean atIntersection() {
		updateRoadmapTile();
		return rmap.isIntersection(toDirection, curTile);
	}
	
	public boolean atDeadEnd() {
		updateRoadmapTile();
		return rmap.isDeadEnd(toDirection, curTile);
	}


	void updateRoadmapTile() {
		curTile = rmap.getTile(new TPos(sprite.getX(), sprite.getY()));
	}

// These are called for each frame to set the velocity 	
	public void driveAlong() {
		float x = 0, y = 0;
		switch (toDirection) {
			case north: y = -VELOCITY; break;
			case south: y = VELOCITY; break;
			case east: x = VELOCITY; break;
			case west: x = -VELOCITY; break;
		}
		body.setLinearVelocity(x, y);
	}
	
	public boolean near(float value, float target) {
		float diff = Math.abs(value - target);
		return (diff <= TURN_EPSILON);
	}

	public void makeTurn(TDir newDir) {
		Log.v("CARTURN", "Making a turn from " + toDirection + " to " + newDir);
		fromDirection = toDirection;
		setDirection(newDir);
		turning = true;
		turnTile = curTile;
		handleTurn2();
	}
	
	public void handleTurn2() {
		//
		// the following is just to save typing. We are going to set up the TPos c to be 
		// the center point of the turn around which the centripidal force works.
		//
		TDir f = fromDirection;
		TDir t = toDirection;
		TDir n = TDir.north;
		TDir e = TDir.east;
		TDir w = TDir.west;
		TDir s = TDir.south;
		TPos c = null;
		
		if ((f == n && t == e) || (f == w && t == s)) c = rmap.getSpriteBRPos(curTile);
		if ((f == n && t == w) || (f == e && t == s)) c = rmap.getSpriteBLPos(curTile);
		if ((f == s && t == e) || (f == e && t == n)) c = rmap.getSpriteTLPos(curTile);
		if ((f == w && t == n) || (f == s && t == e)) c = rmap.getSpriteTRPos(curTile);
		
		Vector2 centVect = velVector(getBodyPos(), tPos2Vector2(c), TURNFORCE);
		Log.v("CARTURN","Turning "+fromDirection+" to "+toDirection+")");
		Log.v("CARTURN", "   current pos=[" + sprite.getX()+","+sprite.getY()+"], speed= " + body.getLinearVelocity());
		Log.v("CARTURN", "   turn center: "+c.toString()+", Current tile: "+curTile.getCoord());
		Log.v("CARTURN", "   cent vec: "+ centVect);
		
		// Apply turn force of strength TURNFORCE in the direction of the center of the turn "c"
		body.applyForce(centVect, body.getWorldCenter());
		if(rmap.getTile(new TPos(sprite.getX(), sprite.getY())) != turnTile) {
			// we've left the tile, so the turn is officially over.
			Log.v("CARTURN", "Turn completed.");
			turnTile = null;
			turning = false;
		}
	}

	public void handleTurn1() {
		Vector2 old = body.getLinearVelocity();
		float newX = 0, newY = 0;
		float oldX = old.x, oldY = old.y;
		Log.v("CARTURN","Still turning "+fromDirection+" to "+toDirection+". Pos=" + body.getPosition() + 
				", speed= " + body.getLinearVelocity());
		
		// Slow down in FROM direction for turn
		switch (fromDirection) {
		case north:
			if (!near(oldY, 0)) newY = TURNFORCE; break;
		case south:
			if (!near(oldY, 0)) newY = -TURNFORCE; break;
		case east:
			if (!near(oldX, 0)) newX = -TURNFORCE; break;
		case west:
			if (!near(oldX, 0)) newX = TURNFORCE; break;
		}
		
		// Speed up in TO direction for turn
		switch (toDirection) {
		case north:
			if (!near(newY, -VELOCITY)) newY = -TURNFORCE; break;
		case south:
			if (!near(newY, VELOCITY)) newY = TURNFORCE; break;
		case east:
			if (!near(newX, VELOCITY)) newX = TURNFORCE; break;
		case west:
			if (!near(newX, VELOCITY)) newX =  - TURNFORCE; break;
		}
		Log.v("CARTURN", "  oldV=("+oldX+","+oldY+")... newV=("+newX+","+newY+")");
		body.applyForce(new Vector2(newX, newY), body.getWorldCenter());
		
		// Check if we have completed the turn
		boolean turnComplete = false;
		switch (toDirection) {
		case north:
			if (near(newX, 0) && near(newY, -TURNFORCE)) turnComplete = true; break; 
		case south:
			if (near(newX, 0) && near(newY, TURNFORCE)) turnComplete = true; break; 
		case east:
			if (near(newX, TURNFORCE) && near(newY, 0)) turnComplete = true; break; 
		case west:
			if (near(newX, - TURNFORCE) && near(newY, 0)) turnComplete = true; break; 
		}
		if (turnComplete) {
			Log.v("CARTURN", "Turn completed");
			turning = false;
		}
	}

	public void comeToStop() {
		Log.v("CARTURN", "Stopping");
		body.setLinearVelocity(0, 0);
		driving = false;
	}

	public TDir getDirection() {
		return toDirection;
	}
	
	public String toString() {
		String carCoordString;
		if (curTile == null) {
			carCoordString = "<none>";
		} else {
			carCoordString = curTile.getCoord().toString();
		}
		return "Car: direction: "+toDirection+", coords: " + carCoordString;
	}
	
	private Vector2 velVector(Vector2 start, Vector2 end, double length) {
		double a = end.x - start.x;
		double o = end.y - start.y;
		double h = Math.sqrt((a * a) + (o * o));
		double ratio = length / h;
        Vector2 res = new Vector2((float) (a * ratio), (float) (o * ratio));
        return res;
	}
	
	private Vector2 tPos2Vector2(TPos pos) {
			Vector2 v = new Vector2(pos.x, pos.y);
			return v.mul(1.0f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
	}

}
