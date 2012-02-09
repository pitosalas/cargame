package com.salas;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

import android.content.Context;

public class WorldAnd extends World<WorldBodies, WorldSprites> {
	public Dashboard dash;
	public Engine engine;
	public LevelManager levelMgr;
	public boolean multiTouch;
	public Context aeCtx;

	@Override
	void registerUpdateHandler(WorldBodies bodies) {
		WorldSpritesAnd wsa = (WorldSpritesAnd) this.sprites;
		wsa.scene.registerUpdateHandler(((WorldBodiesAnd)bodies).worldBox2d);
	}

	public void registerUpdateHandler(float secondInterv, VehicleUpdateHandler handlr) {
		WorldSpritesAnd s = (WorldSpritesAnd) sprites;
		VehicleUpdateHandlerAnd handlerAnd = (VehicleUpdateHandlerAnd) handlr;
		s.scene.registerUpdateHandler(new TimerHandler(secondInterv, true, handlerAnd));
	}

	@Override
	public void unloadResources() {
		super.unloadResources();
	}
	
	@Override
	VehicleUpdateHandlerAnd createVehicleUpdateHandler(VehicleEntity v) {
		return new VehicleUpdateHandlerAnd(v);		
	}
}
