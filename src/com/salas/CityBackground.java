package com.salas;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class CityBackground {
	
	public void createWalls(int height, int width, Scene scene, FixedStepPhysicsWorld world) {
		
		// Create the shapes forming each of the walls
		final Shape bottomOuter = new Rectangle(0, height - 2, width, 2);
		final Shape topOuter = new Rectangle(0, 0, width, 2);
		final Shape leftOuter = new Rectangle(0, 0, 2, height);
		final Shape rightOuter = new Rectangle(width - 2, 0, 2, height);

		// Create the Bodys which give the walls substance
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(world, bottomOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(world, topOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(world, leftOuter, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(world, rightOuter, BodyType.StaticBody, wallFixtureDef);

		// Attach them to the scene
		scene.attachChild(bottomOuter);
		scene.attachChild(topOuter);
		scene.attachChild(leftOuter);
		scene.attachChild(rightOuter);
	}

	public void createTiledBackground(int screenHeight, int screenWidth, Scene scene, Roadmap rmap) {
		int columns = rmap.gridColumns;
			int rows = rmap.gridRows;
			for (int row = 0; row < rows; row ++) {
				for (int col=0; col < columns; col ++) {
					Tile tile = rmap.getTile(new TCoord(row, col));
					RoadmapSprite tileSprite = new RoadmapSprite(rmap, tile);
					scene.attachChild(tileSprite);
				}
			}
		}

	public void setBackgroundColor(Scene scene) {
		scene.setBackground(new ColorBackground(0, 0.5f, 0));		
	}



}
