package com.salas;


import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.util.FPSLogger;

import com.salas.department.*;

import android.util.Log;


public class CarGameActivity extends CommonActivity {

	private LevelMapShapesAnd levelMapShapes;
	private WorldAnd worldA;
	
	private static float INITIAL_ZOOM = 0.4f;

	@Override
	public Engine onLoadEngine() {
			
		worldA = WorldAnd.singleton();
		configScreenInfo();
		configCamera();
		worldA.sprites = new WorldSpritesAnd(worldA);
		worldA.bodies = new WorldBodiesAnd();
		worldA.dash = new Dashboard(this);
		worldA.engine = configEngine();
		worldA.department = new DepartmentEntity(worldA);
		worldA.levelMgr = new LevelManagerAnd(worldA.engine, this);
		worldA.multiTouch = checkEnableMultiTouch(worldA.engine);
		worldA.aeCtx = this;
		
		return worldA.engine;
	}

	@Override
	public void onLoadResources() {
		Log.i("GAME", "onLoadResources");
		
		LevelManagerAnd l = (LevelManagerAnd) worldA.levelMgr;
		l.setCurrentLevel("level8.tmx");
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
		
      LevelManagerAnd l = (LevelManagerAnd) worldA.levelMgr;
      l.attachTiles(scene);
      l.prepareVehicles(worldA);
      l.prepareRoads();
		
		levelMapShapes = new LevelMapShapesAnd(worldA);
		worldA.dash.createAndAttach(this, wsa.scene, camera);
		camera.setZoomFactor(INITIAL_ZOOM);
		configSceneTouchNScroll(wsa.scene, worldA.multiTouch);
		
		Vector2 center = l.getCenterPos();
		camera.setCenter(center.x, center.y);
		
		levelMapShapes.createWalls();
      levelMapShapes.setBackgroundColor();
//		worldA.registerUpdateHandler(worldA.bodies);
		
		worldA.launchVehicles();
		worldA.dash.addTweakboxes();
//		worldA.sprites.showRoadGraph(l.getCurrentLevel());
		worldA.startAllUpdateHandlers();
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