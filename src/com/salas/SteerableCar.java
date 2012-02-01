package com.salas;

import java.util.ArrayList;

import com.salas.TileModel.TDir;

public class SteerableCar extends GameCar {
	static ArrayList<SteerableCar> allCars = new ArrayList<SteerableCar>();
	private SteeringBehaviors steering;
	private Entity entity;
	private GameContext gameContext;
	
	// Initial state
	private TPos startPos;
	private TDir startDir;
	String name;

	
	SteerableCar(GameContext gc) {
		allCars.add(this);
		gameContext = gc;
	}
	
	public Entity entity() {
		return entity;
	}
	
	public SteeringBehaviors steering() {
		return steering;
	}
	
	public void setInitialState(int x, int y, TDir dir, String name) {
		this.startPos = new TPos(x, y);
		startDir = dir;
		this.name = name;
	}
	
	public void initEntity() {
		entity = new EntityBox2d(sprite, gameContext);
	}
	
	public void setPosAndRotation(TPos pos, TDir rot) {
		super.setPosAndRotation(pos, rot);
		entity.setPosAndRotatation(pos, rot);
	}

	public void setInitialPositionAndRotation() {
		setPosAndRotation(startPos, startDir);	
	}

	public void start() {
		steering = new SteeringBehaviors(entity, gameContext);
		steering.path = new Path(true, new Vector2(5, 5), new Vector2(5, 22), new Vector2(22, 22), new Vector2(22, 5));

	}

}
