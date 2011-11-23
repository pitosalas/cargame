package com.salas;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.util.*;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;

import com.badlogic.gdx.math.Vector2;
import com.salas.Car.CarCommand;

import android.app.Activity;
import android.os.Bundle;

public class CarGameActivity extends CommonActivity {

	private Car car1;
	private PathCar car2;
	private RoadmapCar car3;
	private GameLevel rmap;
	private GameMapSprite tileMgr;
	private Scene scene;
	private FixedStepPhysicsWorld world;
	private MapBackground bkground;
	private Dashboard dash;
	private DashTextBox messageBox1, messageBox2;
	private LevelManager levels;
	
	private static float INITIAL_ZOOM = 0.2f;

	@Override
	public Engine onLoadEngine() {
		configScreenInfo();
		configCamera();
		car1 = new Car();
		car2 = new PathCar();

		car3 = new RoadmapCar();
		
		dash = new Dashboard(this);
		messageBox1 = new DashTextBox();
		messageBox2 = new DashTextBox();
		engine = configEngine();
		return engine;
	}

	@Override
	public void onLoadResources() {
		car1.loadResources(this, engine);
		car2.loadResources(this, engine);
		car3.loadResources(this, engine);
		DashTextBox.loadResources(this);
		GameMapSprite.loadResources(this, engine);
	}

	@Override
	public Scene onLoadScene() {
		
		scene = new Scene();
		world = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);
		levels = new LevelManager();
		levels.loadLevelsFromAssets(this);
		rmap = levels.getRoadMap(2);
		bkground = new MapBackground(rmap, scene);
		

		dash.createAndAttach(this, scene, camera);
		messageBox1.createBox(5, 10);
		messageBox2.createBox(800, 10);
		dash.addTextBox(messageBox1);
		dash.addTextBox(messageBox2);

		camera.setZoomFactor(INITIAL_ZOOM);
		bkground.setBackgroundColor(scene);
		
		TPos center = rmap.getCenterPos();
		camera.setCenter(center.x, center.y);
		bkground.createTiledBackground(screenHeight, screenWidth);
		bkground.createWalls(world);

//		prepareCar1();
//		prepareCar2();
		prepareCar3();		

		return scene;
	}

	private void prepareCar1() {
		CarCommand[]padArgsForce= {CarCommand.F_FORW, CarCommand.F_BACK, CarCommand.F_LEFT, CarCommand.F_RIGHT};
		dash.createAndAddPadBank(400.0f, 10.0f, 25.0f, 25.0f, car1, padArgsForce);
		CarCommand[]padArgsSteer = {CarCommand.ST_FORW, CarCommand.ST_BACK, CarCommand.ST_LEFT, CarCommand.ST_RIGHT};
		dash.createAndAddPadBank(600.0f, 10.0f, 25.0f, 25.0f, car1, padArgsSteer);
		car1.createAndAttach(this, world, scene);
		car1.setSpritePos(20, 20);
	}

	private void prepareCar2() {
		car2.createAndAttach(this, world, scene);
		Vector2 from = new Vector2(20, 100), to = new Vector2(600, 100);
		car2.addPathSegment(from, to);
		from = new Vector2(600, 100);
		to = new Vector2(600, 500);
		car2.addPathSegment(from, to);
		scene.registerUpdateHandler(new TimerHandler(1.0f/4, true, new CarFollower(car2, messageBox2)));
		scene.registerUpdateHandler(new TimerHandler(1.0f/10, true, new PathCarBrain(car2)));		
	}

	private void prepareCar3() {
		car3.createAndAttach(this, world, scene);
		car3.placeOnRoadmap(rmap);
		car3.start();
		
		scene.registerUpdateHandler(world);
		scene.registerUpdateHandler(new TimerHandler(1.0f/10, true, new RoadmapCarBrain(rmap, car3)));
		scene.registerUpdateHandler(new TimerHandler(1.0f/4, true, new CarFollower(car3, messageBox1)));
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}
}