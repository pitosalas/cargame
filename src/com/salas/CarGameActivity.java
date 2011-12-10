package com.salas;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.util.*;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;

import com.badlogic.gdx.math.Vector2;

import android.app.Activity;
import android.os.Bundle;

public class CarGameActivity extends CommonActivity {

	private ArrayList<Car> cars;
	private GameLevel currLevel;
	private Scene scene;
	private FixedStepPhysicsWorld world;
	private MapBackground bkground;
	private Dashboard dash;
	private DashTextBox messageBox1, messageBox2;
	private LevelManager levels;
	private boolean multiTouch;
	
	private static float INITIAL_ZOOM = 0.25f;

	@Override
	public Engine onLoadEngine() {
		configScreenInfo();
		configCamera();
		dash = new Dashboard(this);
		engine = configEngine();
		levels = new LevelManager();
		cars = new ArrayList<Car>();
		multiTouch = checkEnableMultiTouch(engine);
		return engine;
	}

	@Override
	public void onLoadResources() {
		levels.loadLevelsFromAssets(this);
		currLevel = levels.getLevel(1);
		for (GameActor actor: currLevel.actors()) {
			RoadmapCar newCar = new RoadmapCar(actor);
			newCar.loadResources(this, engine);
			cars.add(newCar);
		}
		DashTextBox.loadResources(this);
		GameMapSprite.loadResources(this, engine);
	}

	@Override
	public Scene onLoadScene() {		
		scene = new Scene();
		world = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);
		bkground = new MapBackground(currLevel, scene);
		prepareDashboard();
		camera.setZoomFactor(INITIAL_ZOOM);
		configSceneTouchNScroll(scene, multiTouch);
		bkground.setBackgroundColor(scene);
		
		TPos center = currLevel.getCenterPos();
		camera.setCenter(center.x, center.y);
		bkground.createTiledBackground(screenHeight, screenWidth);
		bkground.createWalls(world);
		scene.registerUpdateHandler(world);

		prepareCars();	

		return scene;
	}

	/**
	 * 
	 */
	private void prepareDashboard() {
		dash.createAndAttach(this, scene, camera);
		messageBox1 = new DashTextBox();
		messageBox1.createBox(5, 10);
		messageBox2 = new DashTextBox();
		messageBox2.createBox(800, 10);
		dash.addTextBox(messageBox1);
		dash.addTextBox(messageBox2);
	}

// Each of the Cars created for this level are attached to the level and started. Also
// We register the variousu update handlers which will animate the particular Car.
	private void prepareCars() {
		for (Car c : cars) {
			RoadmapCar rmc = (RoadmapCar) c;
			rmc.createAndAttach(this, world, scene);
			rmc.placeOnRoadmap(currLevel);
			rmc.start();

			scene.registerUpdateHandler(new TimerHandler(1.0f/10, true, new RoadmapCarBrain(currLevel, rmc)));
			scene.registerUpdateHandler(new TimerHandler(1.0f/4, true, new CarFollower(rmc, messageBox1)));
		}
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}
}