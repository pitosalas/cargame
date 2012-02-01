package com.salas;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;


public class CarGameActivity extends CommonActivity {

	private LevelMapShapes levelMapShapes;
	private GameContext gameCtx;
	
	private static float INITIAL_ZOOM = 0.8f;

	@Override
	public Engine onLoadEngine() {
		gameCtx = new GameContext();
		configScreenInfo();
		configCamera();
		gameCtx.dash = new Dashboard(this);
		gameCtx.engine = configEngine();
		engine = gameCtx.engine;
		gameCtx.levelMgr = new LevelManager();
		gameCtx.cars = new ArrayList<GameCar>();
		gameCtx.multiTouch = checkEnableMultiTouch(engine);
		gameCtx.world = new WorldAE(gameCtx);
		return engine;
	}

	@Override
	public void onLoadResources() {
		// Load level descriptors form json file into GameLevel objects
		gameCtx.levelMgr.loadLevelsFromAssets(this);
		
		// Setup currLevel
		gameCtx.currLevel = gameCtx.levelMgr.getLevel(6);

		// Load resources for all the actors (vehicles and fires and so on)
		for (ActorModel actor: gameCtx.currLevel.actors()) {
			SteerableCar newCar = new SteerableCar(gameCtx);
			newCar.setInitialState(actor.getStartingTCoord().col, actor.getStartingTCoord().row, actor.getStartingDir(), actor.getName());
			newCar.loadResources(this, engine);
			gameCtx.cars.add(newCar);
		}
		// Load resources for the Decorations (buildings and so on)
		gameCtx.levelMgr.loadDecorationResources(this, gameCtx);
		// Resources for Dashboard
		TextBox.loadResources(this);
		// Resources for the LevelMapSprites.
		LevelMapSprite.loadResources(this, gameCtx);
		// Utility sprites
		UtilitySprites.loadResources(this, gameCtx);
	}

	@Override
	public Scene onLoadScene() {		
		gameCtx.scene = new Scene();
		gameCtx.worldBox2d = new FixedStepPhysicsWorld(30, new com.badlogic.gdx.math.Vector2(0, 0), false, 8, 1);
		levelMapShapes = new LevelMapShapes(gameCtx);
		gameCtx.dash.createAndAttach(this, gameCtx.scene, camera);
		camera.setZoomFactor(INITIAL_ZOOM);
		configSceneTouchNScroll(gameCtx.scene, gameCtx.multiTouch);
		levelMapShapes.setBackgroundColor();
		
		TPos center = gameCtx.currLevel.getCenterPos();
		camera.setCenter(center.x, center.y);
		
		// levelMaps are created either by a referenced TMX file or by the levelx.json file, not both
		if (gameCtx.currLevel.tmxFileName != "" && gameCtx.currLevel.tmxFileName != null) levelMapShapes.createTMXTiles(this);
		if (gameCtx.currLevel.grid == null) levelMapShapes.createTiledBackground();

		levelMapShapes.createWalls();
		levelMapShapes.createDecorations();
		gameCtx.scene.registerUpdateHandler(gameCtx.worldBox2d);
		
		prepareCars();
		gameCtx.dash.addTweakboxes();
		return gameCtx.scene;
	}


// Each of the Cars created for this level are attached to the level and started. Also
// We register the variousu update handlers which will animate the particular Car.
	private void prepareCars() {
		for (GameCar c : gameCtx.cars) {
			SteerableCar steerCar = (SteerableCar) c;
			steerCar.createSprite();
			gameCtx.scene.attachChild(steerCar.sprite());
			steerCar.initEntity();
			steerCar.setInitialPositionAndRotation();
			steerCar.start();
			
			gameCtx.scene.registerUpdateHandler(new TimerHandler(1.0f/10, true, new SteerableCarBrain(gameCtx.currLevel, steerCar)));
		}
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}
}