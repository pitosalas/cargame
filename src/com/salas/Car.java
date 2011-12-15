package com.salas;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.salas.TileModel.TDir;

import android.content.Context;
import android.util.Log;

//
// Represents an instantiated GameActor, with the associated Sprite (visual)
// and Body (Physics) representations.
//
public class Car {
	private static final int CAR_SIZE = 16;

	private BitmapTextureAtlas carTexture;
	private TiledTextureRegion carTextureRegion;
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

	public void loadResources(CommonActivity ctx, Engine engine) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		carTexture = new BitmapTextureAtlas(128, 16, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		carTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(carTexture, ctx, "vehicles.png", 0, 0, 6, 1);
		engine.getTextureManager().loadTexture(carTexture);
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
		Vector2 box2dpos = new Vector2(pos.x, pos.y);
		body.setTransform(box2dpos.mul(1.0f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT), rot.toDegrees());
	}
	
	public void setSpritePos(float x, float y) {
		sprite.setPosition(x, y);
	}
	
	public TPos getSpritePos() {
		return new TPos(sprite.getX(), sprite.getY()); 
	}
	
	public Vector2 getBodyPos() {
		return body.getPosition();
	}
	
	public String toString() {
		if (body != null) {
			return "Car Body pos:"+toString(body.getPosition())+" spd:"+toString(body.getLinearVelocity());
		} else {
			return "No pos yet";
		}
	}
	
}
