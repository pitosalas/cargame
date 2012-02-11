package com.salas;


import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.util.FPSLogger;

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
		worldA.levelMgr = new LevelManager(worldA.engine, this);
		worldA.multiTouch = checkEnableMultiTouch(worldA.engine);
		worldA.aeCtx = this;
		
		return worldA.engine;
	}

	@Override
	public void onLoadResources() {
		Log.i("GAME", "onLoadResources");
		
		LevelManager l = worldA.levelMgr;
		l.setCurrentLevel("level7.tmx");
		l.loadLevelFromDisk();

		// Resources for Dashboard
		TextBox.loadResources(worldA);

		// Utility sprites
		UtilitySprites.loadResources(this, worldA);
	}

	@Override
	public Scene onLoadScene() {
		Log.i("GAME", "onLoadScene");
		this.mEngine.registerUpdateHandler(new FPSLogger());

		WorldSpritesAnd wsa = (WorldSpritesAnd) worldA.sprites;
		Scene scene = new Scene();
		wsa.scene = scene;

		WorldBodiesAnd wba = (WorldBodiesAnd) worldA.bodies;
      wba.initPhysics();
		
      LevelManager l = worldA.levelMgr;
      l.attachTiles(scene);
      l.prepareVehicles(worldA);
      l.prepareRoads();
		
		levelMapShapes = new LevelMapShapesAnd(worldA);
		worldA.dash.createAndAttach(this, wsa.scene, camera);
		camera.setZoomFactor(INITIAL_ZOOM);
		configSceneTouchNScroll(wsa.scene, worldA.multiTouch);
		
		Vector2 center = worldA.levelMgr.getCenterPos();
		camera.setCenter(center.x, center.y);
		
		levelMapShapes.createWalls();
      levelMapShapes.setBackgroundColor();
		worldA.registerUpdateHandler(worldA.bodies);
		
		worldA.launchVehicles();
		worldA.dash.addTweakboxes();
		worldA.sprites.showRoadGraph(l.getCurrentLevel());
		return scene;
	}

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