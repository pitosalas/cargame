package com.salas;


import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;

import android.util.Log;


public class CarGameActivity extends CommonActivity {

	private LevelMapShapesAnd levelMapShapes;
	private WorldAnd worldA;
	
	private static float INITIAL_ZOOM = 0.8f;

	@Override
	public Engine onLoadEngine() {
		worldA = new WorldAnd();
		configScreenInfo();
		configCamera();
		worldA.sprites = new WorldSpritesAnd(worldA);
		worldA.bodies = new WorldBodiesAnd();
		worldA.dash = new Dashboard(this);
		worldA.engine = configEngine();
		worldA.levelMgr = new LevelManager();
		worldA.multiTouch = checkEnableMultiTouch(worldA.engine);
		worldA.aeCtx = this;
		
		return worldA.engine;
	}

	@Override
	public void onLoadResources() {
		Log.i("GAME", "onLoadResources");

		// Load level descriptors form json file into GameLevel objects
		worldA.levelMgr.loadLevelsFromAssets(this);
		worldA.currLevel = worldA.levelMgr.getLevel(6);

		// Load resources for all the actors (vehicles and fires and so on)
		for (ActorModel actor: worldA.currLevel.actors()) {
			VehicleEntity newVehicle = new VehicleEntity(worldA, new EntityBodyAnd(worldA), new EntitySpriteAnd());
			newVehicle.sprite.loadResources(worldA);
			newVehicle.setName(actor.getName());
			newVehicle.setStartingPos(actor.getStartingPos().x, actor.getStartingPos().y);
			newVehicle.setStartingRotation(actor.getStartingRotation());
		}
		// Load resources for the Decorations (buildings and so on)
		worldA.levelMgr.loadDecorationResources(this, worldA);

		// Resources for Dashboard
		TextBox.loadResources(worldA);

		// Resources for the LevelMapSprites.
		LevelMapSprite.loadResources(this, worldA);

		// Utility sprites
		UtilitySprites.loadResources(this, worldA);
	}

	@Override
	public Scene onLoadScene() {
		Log.i("GAME", "onLoadScene");
		WorldSpritesAnd wsa = (WorldSpritesAnd) worldA.sprites;
		WorldBodiesAnd wba = (WorldBodiesAnd) worldA.bodies;

		Scene scene = new Scene();
		wsa.scene = scene;
		wba.initPhysics();
		levelMapShapes = new LevelMapShapesAnd(worldA);
		worldA.dash.createAndAttach(this, wsa.scene, camera);
		camera.setZoomFactor(INITIAL_ZOOM);
		configSceneTouchNScroll(wsa.scene, worldA.multiTouch);
		levelMapShapes.setBackgroundColor();
		
		TPos center = worldA.currLevel.getCenterPos();
		camera.setCenter(center.x, center.y);
		
		// levelMaps are created either by a referenced TMX file or by the levelx.json file, not both
		if (worldA.currLevel.tmxFileName != "" && worldA.currLevel.tmxFileName != null) levelMapShapes.createTMXTiles(this);
		if (worldA.currLevel.grid == null) levelMapShapes.createTiledBackground();

		levelMapShapes.createWalls();
		levelMapShapes.createDecorations();
		worldA.registerUpdateHandler(worldA.bodies);
		
		worldA.prepareVehicles();
		worldA.dash.addTweakboxes();
		return scene;
	}


//// Each of the Cars created for this level are attached to the level and started. Also
//// We register the variousu update handlers which will animate the particular Car.
//	private void prepareCars() {
//		for (VehicleEntity v : World.vehicles) {
//			v.sprite.createSprite();
//			worldA.sprites.attach(v.sprite);
//			v.body.createBody(v.sprite);
//			v.setPos(v.startingPos);
//			v.setRotation(v.startingRotation);
//			v.start();
//			worldA.registerUpdateHandler(1.0f/10, new VehicleUpdateHandlerAnd(v, worldA));
//		}
//	}
//	
	@Override
	public void onLoadComplete() {
		Log.i("GAME", "onLoadComplete");
	}
	
	public void onUnloadResources() {
		Log.i("GAME", "onUnloadResources");
		UtilitySprites.unloadResources();
		((WorldSpritesAnd) worldA.sprites).scene.reset();
		((WorldSpritesAnd) worldA.sprites).scene = null;
		worldA.unloadResources();
	}

	public void onPauseGame() {
		Log.i("GAME", "onPauseGame");
	}
	public void onResumeGame() {
		Log.i("GAME", "onResumeGame");
	}

}