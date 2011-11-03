package com.salas;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

import android.util.Log;

import com.salas.Tile.TDir;

public class RoadmapCarBrain implements ITimerCallback {

	RoadmapCar car;
	Roadmap map;

	public RoadmapCarBrain(Roadmap theMap, RoadmapCar theCar) {
		car = theCar;
		map = theMap;
	}

/*
 * This is the key algorithm.
 */
	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		car.updateRoadmapTile();
		if (!car.isDriving()) return;
		if (car.isTurning()) {
			car.handleTurn2();
		} else if (car.atIntersection()) {
			Log.v("BRAIN", "Passing Intersection at: "+car.toString());
			handleIntersection();
		} else if (car.atDeadEnd()) {
			Log.v("BRAIN", "Dead end going: " + car.getDirection() + ", at:"+car.toString());
			car.comeToStop();
		} else {
			car.driveAlong();
		}
	}
	
	public void handleIntersection() {
		TDir curDir = car.getDirection();
		Tile curTile = car.currTileOnMap();
		if (curTile.isOpen(curDir.rightTurn())) {
			Log.v("BRAIN", "Turning right");
			car.makeTurn(curDir.rightTurn());
		} else if (curTile.isOpen(curDir)) {
				Log.v("BRAIN", "Continuing through intersection because cant turn right");
				car.driveAlong();
		} else if (curTile.isOpen(curDir.leftTurn())) {
			Log.v("BRAIN", "Turning left because can't go straight or right");
			car.makeTurn(curDir.leftTurn());
		} else {
			Log.v("BRAIN", "Stopping because there's nowhere to go");
			car.comeToStop();
		}
	}

}
