package com.salas;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.Debug;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class LevelMapShapesAnd {
	
	private Scene scene;
	private LevelManager lman;
	private Engine engine;
	private FixedStepPhysicsWorld worldBox2d;

	
	LevelMapShapesAnd(WorldAnd world) {
		scene = ((WorldSpritesAnd) world.sprites).scene;
		lman = world.levelMgr;
		engine = world.engine;
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
