package com.salas;

import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class EntityBodyAnd extends EntityBody {
	private Body body;
	final WorldAnd world;
	final WorldBodiesAnd worldBods;
	
	static TweakBox tweakMaxSpeed = new TweakBox("Max Speed", 6.0f, 0.5f);
	
	static TweakBox tweakDensity = new TweakBox("Density", 0.2f, 0.2f) {
		public void apply() { // called when density changes
			for (VehicleEntity ent : World.vehicles) {
				ent.body.recalcMass();
			}
			
		};
	};
	
	public void createBody(EntitySprite sprite) {
		EntitySpriteAnd spriteAnd = (EntitySpriteAnd) sprite;
		final FixtureDef carFixtureDef = PhysicsFactory.createFixtureDef(tweakDensity.getVal(), 0.5f, 0.5f);
		body = PhysicsFactory.createBoxBody(worldBods.worldBox2d, spriteAnd.sprite, BodyType.DynamicBody, carFixtureDef);
		worldBods.worldBox2d.registerPhysicsConnector(new PhysicsConnector(spriteAnd.sprite, body, true, false));
		setMaxSpeed(tweakMaxSpeed.getVal());	
	}

	public EntityBodyAnd(WorldAnd ctx) {
		world = ctx;
		worldBods = (WorldBodiesAnd) (world.bodies);
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

//	@Override
//	void setPosAndRotatation(TPos pos, TDir rot) {
//		Vector2 box2dpos = new Vector2(pos.x, pos.y);
//		Float pixel2meter = new Float(1.0f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
//		Log.i("entity", pixel2meter.toString());
//		body.setTransform(conv(box2dpos.mul(pixel2meter)), rot.toDegrees());		
//	}
//	
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

	@Override
	void setPos(Vector2 pos) {
		float angle = body.getAngle();
		body.setTransform(conv(pos), angle);
	}

	@Override
	void setRotation(float angle) {
		body.setTransform(body.getPosition(), angle);
	}

}
