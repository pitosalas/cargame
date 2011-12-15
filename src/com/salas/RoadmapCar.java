package com.salas;

import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.salas.TileModel.TDir;

public class RoadmapCar extends Car {

	static int VELOCITY = 5;
	static int TURNFORCE = 5;
	static float TURN_EPSILON = 0.1f;

	private LevelModel level;
	private TileModel curTile;
	private TileModel turnTile;
	private TDir toDirection;
	private TDir fromDirection;
	boolean driving;
	boolean turning;
	
	public RoadmapCar(ActorModel actor) {
		super(actor);
	}

	public void placeOnRoadmap(LevelModel aLevel) {
		level = aLevel;
		TPos start = actor.getStartingTPos(level);
		toDirection = actor.getDir();
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
	
	public TileModel currTileOnMap() {
		return curTile;
	}
	
	public void setDirection(TDir newDir) {
		toDirection = newDir;
	}

	public boolean atIntersection() {
		updateRoadmapTile();
		return level.isIntersection(toDirection, curTile);
	}
	
	public boolean atDeadEnd() {
		updateRoadmapTile();
		return level.isDeadEnd(toDirection, curTile);
	}


	void updateRoadmapTile() {
		curTile = level.getTile(new TPos(sprite.getX(), sprite.getY()));
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
		Log.v("CARTURN", "Making a turn old heading is:" + toDirection + " to new heading:   " + newDir);
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
		
		if ((f == n && t == e) || (f == w && t == s)) c = level.getSpriteBRPos(curTile);
		if ((f == n && t == w) || (f == e && t == s)) c = level.getSpriteBLPos(curTile);
		if ((f == s && t == w) || (f == e && t == n)) c = level.getSpriteTLPos(curTile);
		if ((f == w && t == n) || (f == s && t == e)) c = level.getSpriteTRPos(curTile);

		assert c != null;
		
		Vector2 centVect = velVector(getBodyPos(), tPos2Vector2(c), TURNFORCE);
		Log.v("CARTURN","Turning "+fromDirection+" to "+toDirection+")");
		Log.v("CARTURN", "   current pos=[" + sprite.getX()+","+sprite.getY()+"], speed= " + body.getLinearVelocity());
		Log.v("CARTURN", "   turn center: "+c.toString()+", Current tile: "+curTile.getCoord());
		Log.v("CARTURN", "   cent vec: "+ centVect);
		
		// Apply turn force of strength TURNFORCE in the direction of the center of the turn "c"
		body.applyForce(centVect, body.getWorldCenter());
		if(level.getTile(new TPos(sprite.getX(), sprite.getY())) != turnTile) {
			// we've left the tile, so the turn is officially over.
			Log.v("CARTURN", "Turn completed.");
			turnTile = null;
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
