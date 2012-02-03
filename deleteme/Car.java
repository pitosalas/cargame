package com.salas;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.salas.TileModel.TDir;

import android.content.Context;

//
// Represents an instantiated GameActor, with the associated Sprite (visual)
// and Body (Physics) representations.
//
public class Car extends EntitySpriteAnd {
	private static final int CAR_SIZE = 16;
	protected ActorModel actor;
	protected Body body;
	protected TiledSprite sprite;
	
	public Car() {
	}
	
	public Car(ActorModel anActor) {
		actor = anActor;
	}
	public Body getBody() {
		return body;
	}

	public void createAndAttach(Context ctx, PhysicsWorld world, Scene scene) {
		sprite = new TiledSprite(0.0f, 0.0f, CAR_SIZE, CAR_SIZE, carTextureRegion);
		sprite.setCurrentTileIndex(0);
		final FixtureDef carFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		body = PhysicsFactory.createBoxBody(world, sprite, BodyType.DynamicBody, carFixtureDef);
		world.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, false));
		scene.attachChild(sprite);
	}
	
	private String toString(Vector2 v) {
		return "["+ String.format("%3.2f", v.x)+", "+String.format("%3.2f", v.y)+"]";
	}
	public void setPosAndRotation(TPos pos, TDir rot) {
		sprite.setPosition(pos.x, pos.y);
		sprite.setRotation(rot.toRadians());
		com.badlogic.gdx.math.Vector2 box2dpos = new com.badlogic.gdx.math.Vector2(pos.x, pos.y);
		body.setTransform(box2dpos.mul(1.0f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT), rot.toDegrees());
	}
	
	public void setSpritePos(float x, float y) {
		sprite.setPosition(x, y);
	}
	
	public TPos getSpritePos() {
		return new TPos(sprite.getX(), sprite.getY()); 
	}
	
	public Vector2 getBodyPos() {
		com.badlogic.gdx.math.Vector2 temp = body.getPosition();
		return new Vector2(temp.x, temp.y);
	}
	
	public String toString() {
		if (body != null) {
			return "Car Body pos:"+toString(getBodyPos())+" spd:"+toString(getBodyPos());
		} else {
			return "No pos yet";
		}
	}
	
}
