package com.salas;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;

public class PathCar extends Car {

	private Queue<PathSegment> path;
	
	PathCar() {
		super();
		path = new LinkedList<PathSegment>();
	}

	public PathSegment getNextPathSegment() {
		return path.poll();
	}

	public void addPathSegment(Vector2 from, Vector2 to) {
		path.add(new PathSegment(from, to));
	}
}
