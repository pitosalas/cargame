package com.salas;

import org.anddev.andengine.entity.primitive.*;
import org.anddev.andengine.entity.scene.*;
import org.anddev.andengine.entity.scene.background.*;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.extension.physics.box2d.*;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.*;

public class LevelMapShapesAnd {
	
	private Scene scene;
	private LevelManager lman;
	private FixedStepPhysicsWorld worldBox2d;

	
	LevelMapShapesAnd(WorldAnd world) {
		scene = ((WorldSpritesAnd) world.sprites).scene;
		lman = world.levelMgr;
		worldBox2d = ((WorldBodiesAnd)world.bodies).worldBox2d;
	}
	
	public void createWalls() {
		
		int height = lman.getMapHeightInPixels();
		int width = lman.getMapWidthInPixels();
		
		// Create the shapes forming each of the walls
		final Shape bottomOuter = new Rectangle(0, height - 2, width, 2);
		final Shape topOuter = new Rectangle(0, 0, width, 2);
		final Shape leftOuter = new Rectangle(0, 0, 2, height);
		final Shape rightOuter = new Rectangle(width - 2, 0, 2, height);

		// Create the Bodys which give the walls substance
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(worldBox2d, bottomOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(worldBox2d, topOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(worldBox2d, leftOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(worldBox2d, rightOuter, BodyType.StaticBody, wallFixtureDef);

		// Attach them to the scene
		scene.attachChild(bottomOuter);
		scene.attachChild(topOuter);
		scene.attachChild(leftOuter);
		scene.attachChild(rightOuter);
	}

	public void setBackgroundColor() {
		scene.setBackground(new ColorBackground(0, 0.5f, 0));		
	}
}
