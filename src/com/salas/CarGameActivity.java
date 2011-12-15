package com.salas;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;

import com.badlogic.gdx.math.Vector2;

public class CarGameActivity extends CommonActivity {

	private LevelMapShapes levelMapShapes;
	private DashTextBox messageBox1, messageBox2;
	private GameContext gameCtx;
	
	private static float INITIAL_ZOOM = 0.25f;

	@Override
	public Engine onLoadEngine() {
		gameCtx = new GameContext();
		configScreenInfo();
		configCamera();
		gameCtx.dash = new Dashboard(this);
		gameCtx.engine = configEngine();
		engine = gameCtx.engine;
		gameCtx.levelMgr = new LevelManager();
		gameCtx.cars = new ArrayList<Car>();
		gameCtx.multiTouch = checkEnableMultiTouch(engine);
		return engine;
	}

	@Override
	public void onLoadResources() {
// Load level descriptors form json file into GameLevel objects
		gameCtx.levelMgr.loadLevelsFromAssets(this);
// Setup currLevel
		gameCtx.currLevel = gameCtx.levelMgr.getLevel(3);
// Load resources for all the actors (vehicles and fires and so on)
		for (ActorModel actor: gameCtx.currLevel.actors()) {
			RoadmapCar newCar = new RoadmapCar(actor);
			newCar.loadResources(this, engine);
			gameCtx.cars.add(newCar);
		}
// Load resources for the Decorations (buildings and so on)
		gameCtx.levelMgr.loadDecorationResources(this, gameCtx);
// Resources for Dashboard
		DashTextBox.loadResources(this);

// Resources for the LevelMapSprites.
		LevelMapSprite.loadResources(this, gameCtx);
	}

	@Override
	public Scene onLoadScene() {		
		gameCtx.scene = new Scene();
		gameCtx.world = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);
		levelMapShapes = new LevelMapShapes(gameCtx);
		prepareDashboard();
		camera.setZoomFactor(INITIAL_ZOOM);
		configSceneTouchNScroll(gameCtx.scene, gameCtx.multiTouch);
		levelMapShapes.setBackgroundColor();
		
		TPos center = gameCtx.currLevel.getCenterPos();
		camera.setCenter(center.x, center.y);
		levelMapShapes.createTiledBackground(screenHeight, screenWidth);
		levelMapShapes.createWalls();
		levelMapShapes.createDecorations();
		gameCtx.scene.registerUpdateHandler(gameCtx.world);

		prepareCars();	

		return gameCtx.scene;
	}

	/**
	 * 
	 */
	private void prepareDashboard() {
		gameCtx.dash.createAndAttach(this, gameCtx.scene, camera);
		messageBox1 = new DashTextBox();
		messageBox1.createBox(5, 10);
		messageBox2 = new DashTextBox();
		messageBox2.createBox(800, 10);
		gameCtx.dash.addTextBox(messageBox1);
		gameCtx.dash.addTextBox(messageBox2);
	}

// Each of the Cars created for this level are attached to the level and started. Also
// We register the variousu update handlers which will animate the particular Car.
	private void prepareCars() {
		for (Car c : gameCtx.cars) {
			RoadmapCar rmc = (RoadmapCar) c;
			rmc.createAndAttach(this, gameCtx.world, gameCtx.scene);
			rmc.placeOnRoadmap(gameCtx.currLevel);
			rmc.start();
			
			gameCtx.scene.registerUpdateHandler(new TimerHandler(1.0f/10, true, new RoadmapCarBrain(gameCtx.currLevel, rmc)));
			gameCtx.scene.registerUpdateHandler(new TimerHandler(1.0f/4, true, new CarFollower(rmc, messageBox1)));
		}
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}
}