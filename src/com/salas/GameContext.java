package com.salas;

import java.util.ArrayList;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;

public class GameContext extends ModelContext {

	public Dashboard dash;
	public Engine engine;
	public FixedStepPhysicsWorld world;
	public ArrayList<Car> cars;
	public LevelManager levelMgr;
	public boolean multiTouch;
	public LevelModel currLevel;
	public Scene scene;

}
