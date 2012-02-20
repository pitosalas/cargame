package com.salas;

import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;

import com.salas.world.*;

public class WorldBodiesAnd extends WorldBodies {

	public FixedStepPhysicsWorld worldBox2d;
	
	@Override
	public void initPhysics() {
		worldBox2d = new FixedStepPhysicsWorld(30, new com.badlogic.gdx.math.Vector2(0, 0), false, 8, 1);
	}

}
