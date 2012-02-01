package com.salas;

import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.salas.TileModel.TDir;

public class EntityBox2d extends Entity {
	final Body body;
	final GameContext context;
	
	static TweakBox tweakMaxSpeed = new TweakBox("Max Speed", 6.0f, 0.5f);
	
	static TweakBox tweakDensity = new TweakBox("Density", 0.2f, 0.2f) {
		public void apply() { // called when density changes
			for (SteerableCar car: SteerableCar.allCars) {
				car.entity().recalcMass();
			}
		}
	};
	
	public EntityBox2d(TiledSprite sprite, GameContext ctx) {
		context = ctx;
		final FixtureDef carFixtureDef = PhysicsFactory.createFixtureDef(tweakDensity.getVal(), 0.5f, 0.5f);
		body = PhysicsFactory.createBoxBody(ctx.worldBox2d, sprite, BodyType.DynamicBody, carFixtureDef);
		ctx.worldBox2d.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, false));
		setMaxSpeed(tweakMaxSpeed.getVal());
	}

	@Override
	Vector2 getPos() {
		return new Vector2(body.getPosition().x, body.getPosition().y);
	}

	// get heading or angle, in radians
	float getHeading() {
		com.badlogic.gdx.math.Vector2 dirOfMotion = body.getLinearVelocity().nor();
		return (float) Math.atan2(dirOfMotion.y, dirOfMotion.x);
	}
	
	@Override
	Vector2 getVelocity() {
		// This ugly line is to convert from a AndEndgine Vector2 to one of ours
		return new Vector2(body.getLinearVelocity().x, body.getLinearVelocity().y);
	}
	
	float getVelocityFloat() {
		return getVelocity().len();
	}

	@Override
	void applyForce(Vector2 force) {
		body.applyForce(conv(force), body.getWorldCenter());
	}
	
	void applyImpulse(Vector2 imp) {
		body.applyLinearImpulse(conv(imp), body.getWorldCenter());
	}

	@Override
	void setPosAndRotatation(TPos pos, TDir rot) {
		Vector2 box2dpos = new Vector2(pos.x, pos.y);
		Float pixel2meter = new Float(1.0f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		Log.i("entity", pixel2meter.toString());
		body.setTransform(conv(box2dpos.mul(pixel2meter)), rot.toDegrees());		
	}
	
	public static com.badlogic.gdx.math.Vector2 conv(Vector2 v) {
		return new com.badlogic.gdx.math.Vector2(v.x, v.y);
	}

	@Override
	String getTooltTipText() {
		return getPos().toString()+"\n"+String.format("%1.2f", getVelocityFloat());
	}

	@Override
	public float maxSpeed() {
		return tweakMaxSpeed.getVal();
	}
	
	@Override
	protected void setMaxSpeed(float maxSpeed) {
		tweakMaxSpeed.setVal(maxSpeed);
	}

	@Override
	void recalcMass() {
		for (Fixture e: body.getFixtureList()) {
			e.setDensity(tweakDensity.getVal());
		}
		body.resetMassData();
	}

}
