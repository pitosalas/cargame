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
import com.salas.Tile.TDir;

import android.content.Context;
import android.util.Log;



public class Car {
	private static final int CAR_SIZE = 16;

	private BitmapTextureAtlas carTexture;
	private TiledTextureRegion carTextureRegion;
	private int counter;

	Body body;
	TiledSprite sprite;
	
	public Car() {
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
		return "Car Body pos:"+toString(body.getPosition())+
				  " spd:"+toString(body.getLinearVelocity());
	}
	
	final static int FORCE_RIGHT = 0;
	final static int FORCE_DOWN = 1;
	final static int FORCE_LEFT = 2;
	final static int FORCE_UP = 3;
	final static int IMP_RIGHT = 4;
	final static int IMP_DOWN = 5;
	final static int IMP_LEFT = 6;
	final static int IMP_UP = 7;

	
	public enum CarCommand {
		ST_FORW,
		ST_BACK,
		ST_LEFT,
		ST_RIGHT,
		F_FORW,
		F_BACK,
		F_LEFT,
		F_RIGHT,
		I_FORW,
		I_BACK,
		I_LEFT,
		I_RIGHT
	}
	
	public void do_command(CarCommand cmd) {
		Log.v("CAR", "Command: " + cmd.toString());
	}

	
	public void applyForce(int dir) {
		counter++;
		Vector2 vect = new Vector2();
		switch (dir) {
	        case FORCE_RIGHT: vect = new Vector2(3.0f, 0.0f); break;
	        case IMP_RIGHT: vect = new Vector2(1.0f, 0.0f); break;
	        case FORCE_LEFT: vect = new Vector2(-3.0f, 0.0f); break;
	        case IMP_LEFT: vect = new Vector2(-1.0f, 0.0f); break;
	        case FORCE_UP: vect = new Vector2(0.0f, -3.0f); break;
	        case IMP_UP: vect = new Vector2(0.0f, -1.0f); break;
	        case FORCE_DOWN: vect = new Vector2(0.0f, 3.0f); break;
	        case IMP_DOWN: vect = new Vector2(0.0f, 1.0f); break;
		}
		Vector2 place = body.getWorldCenter();
		if (dir == FORCE_RIGHT || dir == FORCE_LEFT || dir == FORCE_DOWN || dir == FORCE_UP) 
			{ 
			body.applyForce(vect, place);
			}
		else 
			{ 
			body.applyLinearImpulse(vect, place);
			}
		Log.v("CAR", "Applied force: " + dir + "("+counter+")");
	}
	
}
